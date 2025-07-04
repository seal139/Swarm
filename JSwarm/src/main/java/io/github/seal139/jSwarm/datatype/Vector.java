/*
* Swarm - An Extensible and Modular GPGPU framework
* Copyright (C) 2025  Septian Pramana / Mercu Buana University
*
* This file is part of Swarm.
*
* Swarm is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2.
*
* Swarm is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; If not, see <http://www.gnu.org/licenses/>.
*/

package io.github.seal139.jSwarm.datatype;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import io.github.seal139.jSwarm.backend.Context;
import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.misc.NativeCleaner;
import io.github.seal139.jSwarm.misc.NativeCleaner.NativeResources;
import sun.misc.Unsafe;

/**
 * This is the basis class of native-primitive {@linkplain Number} collection
 * (either dynamic or static). <br/>
 *
 * Vector have almost all capabilities of standard {@link List} plus the
 * extension of 64 bit indexing for extremely large dataset and unchecked bound
 * for high performance purpose
 */
public abstract class Vector<T extends Number> extends Number implements NativeResources, List<T> {

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return getNativeAddress();
    }

    @Override
    public float floatValue() {
        return longValue();
    }

    @Override
    public double doubleValue() {
        return longValue();
    }

    private static final long     serialVersionUID = 7218315256186068889L;
    protected static final Unsafe memAlloc         = Common.getMemoryManagement();

    protected class VectorDeallocator implements Deallocator {
        protected long cacheAddress;

        protected boolean isClosed = false;

        protected VectorDeallocator(long cAddress) {
            this.cacheAddress = cAddress;
        }

        protected void reInit(long cAddress) {
            this.cacheAddress = cAddress;
        }

        @Override
        public void clean() {
            if (this.isClosed) {
                return;
            }

            this.isClosed = true;

            memAlloc.freeMemory(this.cacheAddress);
        }
    }

    protected final boolean aligned;
    protected final boolean dynamic;

    protected long lBound;  // [
    protected long uBound;  // )
    protected long indexPtr;

    protected final VectorDeallocator deallocator;

    protected final long size_t;
    protected final long bit_t;

    public long getValueSize() { return this.size_t; }

    Vector(long lBound, long uBound, long size_t, long bit_t) {
        this.size_t = size_t;
        this.bit_t  = bit_t;

        this.aligned = (lBound % size_t) == 0;
        this.dynamic = false;

        this.lBound   = lBound;
        this.uBound   = uBound;
        this.indexPtr = this.uBound;

        this.deallocator = null;
    }

    Vector(long size, boolean aligned, long size_t, long bit_t) {
        final long sz     = size * size_t;
        final long origin = memAlloc.allocateMemory(sz + (aligned ? (size_t - 1) : 0));

        this.size_t = size_t;
        this.bit_t  = bit_t;

        long modulo = aligned ? origin % size_t : 0L;

        this.lBound   = modulo == 0L ? origin : ((origin - modulo) + (origin > 0 ? size_t : 0));
        this.uBound   = this.lBound + sz;
        this.indexPtr = aligned ? this.uBound : this.lBound;

        this.aligned = aligned;
        this.dynamic = !aligned;

        this.deallocator = new VectorDeallocator(origin);
        NativeCleaner.register(this);
    }

    protected void reinit(long size, long size_t) {
        if (this.aligned || !this.dynamic) {
            throw new IndexOutOfBoundsException((this.uBound - this.lBound) / size_t);
        }

        long oldMemory = this.deallocator.cacheAddress;

        final long sz     = size * size_t;
        final long origin = memAlloc.reallocateMemory(oldMemory, sz);

        this.deallocator.reInit(origin);

        long offset = this.indexPtr - this.lBound;

        this.lBound   = origin;
        this.uBound   = this.lBound + sz;
        this.indexPtr = this.lBound + offset;
    }

    @Override
    public void close() {
        this.deallocator.clean();
    }

    @Override
    public boolean isClosed() { return this.deallocator.isClosed; }

    @Override
    public Deallocator getDeallocator() { return this.deallocator; }

    public long getNativeAddress() { return this.lBound; }

    // -----=======~~ Base Functionality ~~~=======-----

    @Override
    public boolean isEmpty() { return longSize() == 0; }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        long index = longIndexOf((T) o);

        if (index == -1) {
            return false;
        }

        remove(index);
        return true;
    }

    @Override
    @Deprecated
    public int size() {
        return (int) longSize();
    }

    @Override
    @Deprecated
    public T remove(int index) {
        return remove((long) index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (longIndexOf((T) obj) < 0L) {
                return false;
            }
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;

        for (Object obj : c) {

            long index = longIndexOf((T) obj);

            while (index != -1) {
                removed = true;
                remove(index);
                index = longIndexOf((T) obj);
            }
        }

        return removed;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    @Deprecated
    public List<T> subList(int fromIndex, int toIndex) {
        return subList((long) fromIndex, (long) toIndex);
    }

    @Override
    @Deprecated
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return longIndexOf((T) o) > -1L;
    }

    @Override
    @Deprecated
    public T get(int index) {
        return get((long) index);
    }

    @Override
    @Deprecated
    public T set(int index, T element) {
        return set((long) index, element);
    }

    @Override
    @Deprecated
    @SuppressWarnings("unchecked")
    public int indexOf(Object o) {
        return (int) longIndexOf((T) o);
    }

    @Override
    @Deprecated
    @SuppressWarnings("unchecked")
    public int lastIndexOf(Object o) {
        return (int) longLastIndexOf((T) o);
    }

    // -----=======~~ Extension ~~~=======-----
    public abstract long longIndexOf(T element);
    public abstract long longLastIndexOf(T element);

    public abstract List<T> subList(long fromIndex, long toIndex);

    public abstract T get(long index);
    public abstract T set(long index, T element);
    public abstract T remove(long index);

    public long longSize() {
        return (this.indexPtr - this.lBound) >> this.bit_t;
    }

    public long capacity() {
        return (this.uBound - this.lBound) >> this.bit_t;
    }

    // -----======= Not Supported =======-----
    @Override
    @Deprecated
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported due to performance issue");
    }

    @Override
    @Deprecated
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Not supported due to performance issue");
    }

    @Override
    @Deprecated
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported due to performance issue");
    }

    @Override
    @Deprecated
    public final Object[] toArray() {
        throw new UnsupportedOperationException("Not supported due to memory efficiency");
    }

    @Override
    @Deprecated
    @SuppressWarnings("hiding")
    public final <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported due to memory efficiency");
    }

    // ========= GPGPU Capabilities =========

    private final Map<Context, Long> buffer = new HashMap<>();

    public long getBufferAddress(Context context) {
        return this.buffer.get(context);
    }

    public void setBuffer(Context context, long address) {
        this.buffer.put(context, address);
    }

    public void removeBuffer(Context context) {
        this.buffer.remove(context);
    }
}

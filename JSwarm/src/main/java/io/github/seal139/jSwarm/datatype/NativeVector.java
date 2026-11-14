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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the basis class of native-primitive {@linkplain Number} collection
 * (either dynamic or static). <br/>
 *
 * Vector have almost all capabilities of standard {@link List} plus the
 * extension of 64 bit indexing for extremely large dataset and unchecked bound
 * for high performance purpose
 */
public abstract class NativeVector<T extends Number> //
        implements NativeList<T> {

    protected final boolean sublist;

    protected final NativeArray container;

    protected long offset;
    protected long uBound;

    protected long capacity;

    protected long initialSize;

    protected NativeVector(long initialSize, long size_t) {
        this.sublist = false;

        this.offset      = 0L;
        this.uBound      = 0L;
        this.capacity    = initialSize;
        this.initialSize = initialSize;

        this.container = new NativeArray(initialSize * size_t);
    }

    protected NativeVector(long address, long size, long size_t) {
        this.sublist = true;

        this.offset = 0L;
        this.uBound = 0L;

        this.capacity    = size;
        this.initialSize = size;

        this.container = new NativeArray(address, size * size_t);
    }

    protected NativeVector(NativeVector<T> original, long offset, long size) {
        if ((offset < 0) || (size <= 0) || ((offset + size) > (original.uBound))) {
            throw new IndexOutOfBoundsException();
        }

        this.sublist = true;

        this.offset = offset + original.offset;
        this.uBound = size;

        this.capacity    = size;
        this.initialSize = size;

        this.container = original.container;
    }

    @Override
    public long size() {
        return this.uBound;
    }

    @Override
    public long capacity() {
        return this.capacity;
    }

    @Override
    public boolean addAll(Collection<T> c) {
        final Iterator<? extends T> iter = c.iterator();

        boolean ok = false;
        while (iter.hasNext() && add(iter.next())) {
            ok = true;
        }

        return ok;
    }

    @Override
    public boolean containsAll(Collection<T> c) {
        for (T val : c) {
            if (!contains(val)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public List<List<T>> asList() {
        final long chunkCount = 1 + (int) (size() / Integer.MAX_VALUE);

        final List<List<T>> retChunk = new ArrayList<>();
        for (long sequence = 0; sequence < chunkCount; sequence++) {
            long from = Integer.MAX_VALUE * sequence;
            long to   = Math.min(size(), from + Integer.MAX_VALUE);

            retChunk.add(new ReadOnlyListMock(subList(from, to)));
        }

        return Collections.unmodifiableList(retChunk);
    }

    private final class ReadOnlyListMock implements List<T> {
        private final NativeList<T> ref;

        private ReadOnlyListMock(NativeList<T> reference) {
            this.ref = reference;
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            return new ReadOnlyListMock(this.ref.subList(fromIndex, toIndex)).ref.asList().get(0);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                return this.ref.contains((T) o);
            }
            catch (ClassCastException e) {
                return false;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean containsAll(Collection<?> c) {
            try {
                return this.ref.containsAll((Collection<T>) c);
            }
            catch (ClassCastException e) {
                return false;
            }
        }

        @Override
        public T get(int index) {
            return this.ref.get(index);
        }

        @Override
        @SuppressWarnings("unchecked")
        public int indexOf(Object o) {
            try {
                return (int) this.ref.indexOf((T) o);
            }
            catch (ClassCastException e) {
                return -1;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public int lastIndexOf(Object o) {
            try {
                return (int) this.ref.lastIndexOf((T) o);
            }
            catch (ClassCastException e) {
                return -1;
            }
        }

        @Override
        public int size() {
            return (int) this.ref.size();
        }

        @Override
        public boolean isEmpty() { return NativeVector.this.isEmpty(); }

        @Override
        public Iterator<T> iterator() {
            return NativeVector.this.iterator();
        }

        // ===========

        @Override
        public boolean add(T arg0) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public void add(int arg0, T arg1) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public ListIterator<T> listIterator() {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public T remove(int index) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public T set(int arg0, T arg1) {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException("List is readonly");
        }

        @Override
        public <T> T[] toArray(T[] arg0) {
            throw new UnsupportedOperationException("List is readonly");
        }

    }
}

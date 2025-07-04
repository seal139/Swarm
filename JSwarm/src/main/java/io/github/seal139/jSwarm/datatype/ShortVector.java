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
import java.util.List;
import java.util.ListIterator;

/**
 * High performance Short Vector.
 */
public final class ShortVector extends Vector<Short> {

    private static final long serialVersionUID = -4190430668549781215L;

    private static final long WORD_SIZE  = 2;
    private static final long SHIFT_SIZE = 1;

    // ============ Allocator - Deallocator ==================

    public ShortVector() {
        super(16, false, WORD_SIZE, SHIFT_SIZE);
    }

    public ShortVector(long initial) {
        super(initial, false, WORD_SIZE, SHIFT_SIZE);
    }

    public ShortVector(long initial, boolean aligned) {
        super(initial, aligned, WORD_SIZE, SHIFT_SIZE);
    }

    public ShortVector(long address, long size) {
        super(address, address + (size * WORD_SIZE), WORD_SIZE, SHIFT_SIZE);
    }

    // ============ Functionality Operation ==================

    @Override
    public List<Short> subList(long fromIndex, long toIndex) {
        // Safety check
        fromIndex = this.lBound + (fromIndex << this.bit_t);
        if ((fromIndex < this.lBound) || (fromIndex >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(fromIndex));
        }

        // Safety check
        toIndex = this.lBound + (toIndex << this.bit_t);
        if ((toIndex < this.lBound) || (toIndex > this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(toIndex));
        }

        // Safety check
        if (toIndex < fromIndex) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(toIndex));
        }

        return new ShortVector(fromIndex, toIndex - fromIndex);
    }

    @Override
    public boolean addAll(Collection<? extends Short> c) {
        long additional = c.size() - capacity();

        if (additional > 0) {
            reinit(capacity() + additional, this.size_t);
        }

        c.forEach(e -> {
            memAlloc.putShort(this.indexPtr, e);
            this.indexPtr += this.size_t;
        });

        return true;
    }

    @Override
    public boolean add(Short e) {
        if (this.indexPtr >= this.uBound) {
            reinit(capacity() * 2L, this.size_t);
        }

        memAlloc.putShort(this.indexPtr, e);
        this.indexPtr += this.size_t;

        return true;
    }

    @Override
    public Short get(long index) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directGet(index);
    }

    @Override
    public Short set(long index, Short element) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directSet(index, element.shortValue());
    }

    @Override
    public Short remove(long index) {
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directRemove(index);
    }

    @Override
    public void clear() {
        if (this.dynamic) {
            this.indexPtr = this.lBound;
            return;
        }

        for (long i = this.lBound; i < this.indexPtr; i += this.size_t) {
            memAlloc.putShort(i, (short) 0);
        }
    }

    @Override
    public long longIndexOf(Short element) {
        final double ref = element.shortValue();
        final long   s   = (longSize() << this.bit_t) + this.lBound;

        for (long i = this.lBound; i < s; i += this.size_t) {
            if (memAlloc.getShort(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public long longLastIndexOf(Short o) {
        final double ref = o.shortValue();
        final long   s   = (longSize() << this.bit_t) + (this.lBound - this.size_t);

        for (long i = s; i >= this.lBound; i -= this.size_t) {
            if (memAlloc.getShort(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<Short> listIterator(int index) {
        return new ListIterator<>() {
            private final long st = ShortVector.this.size_t;
            private final long bt = ShortVector.this.bit_t;
            private final long ub = ShortVector.this.indexPtr - this.st;

            private long memPtr = ShortVector.this.lBound + ((index - 1) << this.bt);

            @Override
            public boolean hasNext() {
                return this.memPtr < this.ub;
            }

            @Override
            public int nextIndex() {
                return (int) ((this.memPtr + this.st) >> this.bt);
            }

            @Override
            public Short next() {
                this.memPtr += this.st;
                return memAlloc.getShort(this.memPtr);
            }

            @Override
            public boolean hasPrevious() {
                return this.memPtr >= ShortVector.this.lBound;
            }

            @Override
            public int previousIndex() {
                return (int) ((this.memPtr - this.st) >> this.bt);
            }

            @Override
            public Short previous() {
                this.memPtr -= this.st;
                return memAlloc.getShort(this.memPtr);
            }

            @Override
            public void set(Short e) {
                memAlloc.putShort(this.memPtr, e);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }

            @Override
            public void add(Short e) {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }
        };
    }

    // -----=======~~ Direct Functionality ~~~=======-----
    public short directGet(long index) {
        return memAlloc.getShort(this.lBound + (index << this.bit_t));
    }

    public short directSet(long index, Short element) {
        final long  memPtr = this.lBound + (index << this.bit_t);
        final short value  = memAlloc.getShort(memPtr);

        memAlloc.putShort(memPtr, element);
        return value;
    }

    public short directRemove(long index) {
        final long idx = this.lBound + (index << this.bit_t);

        final short prevValue = memAlloc.getShort(idx);

        if (this.dynamic) {
            // Shift left
            for (long i = idx; i < this.indexPtr; i += this.size_t) {
                memAlloc.putShort(i, memAlloc.getShort(i + this.size_t));
            }

            this.indexPtr -= this.size_t;
            return prevValue;
        }

        memAlloc.putShort(idx, (short) 0);
        return prevValue;
    }
}

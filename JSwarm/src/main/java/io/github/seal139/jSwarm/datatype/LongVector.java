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
 * High performance Long Vector.
 */
public final class LongVector extends Vector<Long> {

    private static final long serialVersionUID = -8807381871105608144L;

    private static final long WORD_SIZE  = 8;
    private static final long SHIFT_SIZE = 3;

    // ============ Allocator - Deallocator ==================

    public LongVector() {
        super(16, false, WORD_SIZE, SHIFT_SIZE);
    }

    public LongVector(long initial) {
        super(initial, false, WORD_SIZE, SHIFT_SIZE);
    }

    public LongVector(long initial, boolean aligned) {
        super(initial, aligned, WORD_SIZE, SHIFT_SIZE);
    }

    public LongVector(long address, long size) {
        super(address, address + (size * WORD_SIZE), WORD_SIZE, SHIFT_SIZE);
    }

    // ============ Functionality Operation ==================

    @Override
    public List<Long> subList(long fromIndex, long toIndex) {
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

        return new LongVector(fromIndex, toIndex - fromIndex);
    }

    @Override
    public boolean addAll(Collection<? extends Long> c) {
        long additional = c.size() - capacity();

        if (additional > 0) {
            reinit(capacity() + additional, this.size_t);
        }

        c.forEach(e -> {
            memAlloc.putLong(this.indexPtr, e);
            this.indexPtr += this.size_t;
        });

        return true;
    }

    @Override
    public boolean add(Long e) {
        if (this.indexPtr >= this.uBound) {
            reinit(capacity() * 2L, this.size_t);
        }

        memAlloc.putLong(this.indexPtr, e);
        this.indexPtr += this.size_t;

        return true;
    }

    @Override
    public Long get(long index) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directGet(index);
    }

    @Override
    public Long set(long index, Long element) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directSet(index, element.longValue());
    }

    @Override
    public Long remove(long index) {
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
            memAlloc.putLong(i, 0);
        }
    }

    @Override
    public long longIndexOf(Long element) {
        final double ref = element.longValue();
        final long   s   = (longSize() << this.bit_t) + this.lBound;

        for (long i = this.lBound; i < s; i += this.size_t) {
            if (memAlloc.getLong(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public long longLastIndexOf(Long o) {
        final double ref = o.longValue();
        final long   s   = (longSize() << this.bit_t) + (this.lBound - this.size_t);

        for (long i = s; i >= this.lBound; i -= this.size_t) {
            if (memAlloc.getLong(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<Long> listIterator(int index) {
        return new ListIterator<>() {
            private final long st = LongVector.this.size_t;
            private final long bt = LongVector.this.bit_t;
            private final long ub = LongVector.this.indexPtr - this.st;

            private long memPtr = LongVector.this.lBound + ((index - 1) << this.bt);

            @Override
            public boolean hasNext() {
                return this.memPtr < this.ub;
            }

            @Override
            public int nextIndex() {
                return (int) ((this.memPtr + this.st) >> this.bt);
            }

            @Override
            public Long next() {
                this.memPtr += this.st;
                return memAlloc.getLong(this.memPtr);
            }

            @Override
            public boolean hasPrevious() {
                return this.memPtr >= LongVector.this.lBound;
            }

            @Override
            public int previousIndex() {
                return (int) ((this.memPtr - this.st) >> this.bt);
            }

            @Override
            public Long previous() {
                this.memPtr -= this.st;
                return memAlloc.getLong(this.memPtr);
            }

            @Override
            public void set(Long e) {
                memAlloc.putLong(this.memPtr, e);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }

            @Override
            public void add(Long e) {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }
        };
    }

    // -----=======~~ Direct Functionality ~~~=======-----
    public long directGet(long index) {
        return memAlloc.getLong(this.lBound + (index << this.bit_t));
    }

    public long directSet(long index, Long element) {
        final long memPtr = this.lBound + (index << this.bit_t);
        final long value  = memAlloc.getLong(memPtr);

        memAlloc.putLong(memPtr, element);
        return value;
    }

    public long directRemove(long index) {
        final long idx = this.lBound + (index << this.bit_t);

        final long prevValue = memAlloc.getLong(idx);

        if (this.dynamic) {
            // Shift left
            for (long i = idx; i < this.indexPtr; i += this.size_t) {
                memAlloc.putLong(i, memAlloc.getLong(i + this.size_t));
            }

            this.indexPtr -= this.size_t;
            return prevValue;
        }

        memAlloc.putLong(idx, 0);
        return prevValue;
    }
}

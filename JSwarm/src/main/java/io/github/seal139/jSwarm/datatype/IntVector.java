package io.github.seal139.jSwarm.datatype;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * High performance array of float.
 */
public final class IntVector extends Vector<Integer> {

    private static final long serialVersionUID = 613457863113155896L;

    private static final long WORD_SIZE  = 4;
    private static final long SHIFT_SIZE = 2;

    // ============ Allocator - Deallocator ==================

    public IntVector() {
        super(16, false, WORD_SIZE, SHIFT_SIZE);
    }

    public IntVector(long initial) {
        super(initial, false, WORD_SIZE, SHIFT_SIZE);
    }

    public IntVector(long initial, boolean aligned) {
        super(initial, aligned, WORD_SIZE, SHIFT_SIZE);
    }

    public IntVector(long address, long size) {
        super(address, address + (size * WORD_SIZE), WORD_SIZE, SHIFT_SIZE);
    }

    // ============ Functionality Operation ==================

    @Override
    public List<Integer> subList(long fromIndex, long toIndex) {
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

        return new IntVector(fromIndex, toIndex - fromIndex);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        long additional = c.size() - capacity();

        if (additional > 0) {
            reinit(capacity() + additional, this.size_t);
        }

        c.forEach(e -> {
            memAlloc.putInt(this.indexPtr, e);
            this.indexPtr += this.size_t;
        });

        return true;
    }

    @Override
    public boolean add(Integer e) {
        if (this.indexPtr >= this.uBound) {
            reinit(capacity() * 2L, this.size_t);
        }

        memAlloc.putInt(this.indexPtr, e);
        this.indexPtr += this.size_t;

        return true;
    }

    @Override
    public Integer get(long index) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directGet(index);
    }

    @Override
    public Integer set(long index, Integer element) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directSet(index, element.intValue());
    }

    @Override
    public Integer remove(long index) {
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
            memAlloc.putInt(i, 0);
        }
    }

    @Override
    public long longIndexOf(Integer element) {
        final float ref = element.intValue();
        final long  s   = (longSize() << this.bit_t) + this.lBound;

        for (long i = this.lBound; i < s; i += this.size_t) {
            if (memAlloc.getInt(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public long longLastIndexOf(Integer o) {
        final float ref = o.intValue();
        final long  s   = (longSize() << this.bit_t) + (this.lBound - this.size_t);

        for (long i = s; i >= this.lBound; i -= this.size_t) {
            if (memAlloc.getInt(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<Integer> listIterator(int index) {
        return new ListIterator<>() {
            private final long st = IntVector.this.size_t;
            private final long bt = IntVector.this.bit_t;
            private final long ub = IntVector.this.indexPtr - this.st;

            private long memPtr = IntVector.this.lBound + ((index - 1) << this.bt);

            @Override
            public boolean hasNext() {
                return this.memPtr < this.ub;
            }

            @Override
            public int nextIndex() {
                return (int) ((this.memPtr + this.st) >> this.bt);
            }

            @Override
            public Integer next() {
                this.memPtr += this.st;
                return memAlloc.getInt(this.memPtr);
            }

            @Override
            public boolean hasPrevious() {
                return this.memPtr >= IntVector.this.lBound;
            }

            @Override
            public int previousIndex() {
                return (int) ((this.memPtr - this.st) >> this.bt);
            }

            @Override
            public Integer previous() {
                this.memPtr -= this.st;
                return memAlloc.getInt(this.memPtr);
            }

            @Override
            public void set(Integer e) {
                memAlloc.putInt(this.memPtr, e);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }

            @Override
            public void add(Integer e) {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }
        };
    }

    // -----=======~~ Direct Functionality ~~~=======-----
    public int directGet(long index) {
        return memAlloc.getInt(this.lBound + (index << this.bit_t));
    }

    public int directSet(long index, int element) {
        final long memPtr = this.lBound + (index << this.bit_t);
        final int  value  = memAlloc.getInt(memPtr);

        memAlloc.putInt(memPtr, element);
        return value;
    }

    public int directRemove(long index) {
        final long idx = this.lBound + (index << this.bit_t);

        final int prevValue = memAlloc.getInt(idx);

        if (this.dynamic) {
            // Shift left
            for (long i = idx; i < this.indexPtr; i += this.size_t) {
                memAlloc.putInt(i, memAlloc.getInt(i + this.size_t));
            }

            this.indexPtr -= this.size_t;
            return prevValue;
        }

        memAlloc.putInt(idx, 0);
        return prevValue;
    }
}

package io.github.seal139.jSwarm.datatype;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * High performance array of float.
 */
public final class FloatVector extends Vector<Float> {

    private static final long serialVersionUID = 3239625231594541451L;

    private static final long WORD_SIZE  = 4;
    private static final long SHIFT_SIZE = 2;

    // ============ Allocator - Deallocator ==================

    public FloatVector() {
        super(16, false, WORD_SIZE, SHIFT_SIZE);
    }

    public FloatVector(long initial) {
        super(initial, false, WORD_SIZE, SHIFT_SIZE);
    }

    public FloatVector(long initial, boolean aligned) {
        super(initial, aligned, WORD_SIZE, SHIFT_SIZE);
    }

    public FloatVector(long address, long size) {
        super(address, address + (size * WORD_SIZE), WORD_SIZE, SHIFT_SIZE);
    }

    // ============ Functionality Operation ==================

    @Override
    public List<Float> subList(long fromIndex, long toIndex) {
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

        return new FloatVector(fromIndex, toIndex - fromIndex);
    }

    @Override
    public boolean addAll(Collection<? extends Float> c) {
        long additional = c.size() - capacity();

        if (additional > 0) {
            reinit(capacity() + additional, this.size_t);
        }

        c.forEach(e -> {
            memAlloc.putFloat(this.indexPtr, e);
            this.indexPtr += this.size_t;
        });

        return true;
    }

    @Override
    public boolean add(Float e) {
        if (this.indexPtr >= this.uBound) {
            reinit(capacity() * 2L, this.size_t);
        }

        memAlloc.putFloat(this.indexPtr, e);
        this.indexPtr += this.size_t;

        return true;
    }

    @Override
    public Float get(long index) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directGet(index);
    }

    @Override
    public Float set(long index, Float element) {
        // Safety check
        if ((index < 0) || ((this.lBound + (index << this.bit_t)) >= this.indexPtr)) {
            throw new ArrayIndexOutOfBoundsException("Out of bound: " + String.valueOf(index));
        }

        return directSet(index, element.floatValue());
    }

    @Override
    public Float remove(long index) {
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
            memAlloc.putFloat(i, 0.0f);
        }
    }

    @Override
    public long longIndexOf(Float element) {
        final float ref = element.floatValue();
        final long  s   = (longSize() << this.bit_t) + this.lBound;

        for (long i = this.lBound; i < s; i += this.size_t) {
            if (memAlloc.getFloat(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public long longLastIndexOf(Float o) {
        final float ref = o.floatValue();
        final long  s   = (longSize() << this.bit_t) + (this.lBound - this.size_t);

        for (long i = s; i >= this.lBound; i -= this.size_t) {
            if (memAlloc.getFloat(i) == ref) {
                return (i - this.lBound) >> this.bit_t;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<Float> listIterator(int index) {
        return new ListIterator<>() {
            private final long st = FloatVector.this.size_t;
            private final long bt = FloatVector.this.bit_t;
            private final long ub = FloatVector.this.indexPtr - this.st;

            private long memPtr = FloatVector.this.lBound + ((index - 1) << this.bt);

            @Override
            public boolean hasNext() {
                return this.memPtr < this.ub;
            }

            @Override
            public int nextIndex() {
                return (int) ((this.memPtr + this.st) >> this.bt);
            }

            @Override
            public Float next() {
                this.memPtr += this.st;
                return memAlloc.getFloat(this.memPtr);
            }

            @Override
            public boolean hasPrevious() {
                return this.memPtr >= FloatVector.this.lBound;
            }

            @Override
            public int previousIndex() {
                return (int) ((this.memPtr - this.st) >> this.bt);
            }

            @Override
            public Float previous() {
                this.memPtr -= this.st;
                return memAlloc.getFloat(this.memPtr);
            }

            @Override
            public void set(Float e) {
                memAlloc.putFloat(this.memPtr, e);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }

            @Override
            public void add(Float e) {
                throw new UnsupportedOperationException("Not supported due to performance issue");
            }
        };
    }

    // -----=======~~ Direct Functionality ~~~=======-----
    public float directGet(long index) {
        return memAlloc.getFloat(this.lBound + (index << this.bit_t));
    }

    public float directSet(long index, float element) {
        final long  memPtr = this.lBound + (index << this.bit_t);
        final float value  = memAlloc.getFloat(memPtr);

        memAlloc.putFloat(memPtr, element);
        return value;
    }

    public float directRemove(long index) {
        final long idx = this.lBound + (index << this.bit_t);

        final float prevValue = memAlloc.getFloat(idx);

        if (this.dynamic) {
            // Shift left
            for (long i = idx; i < this.indexPtr; i += this.size_t) {
                memAlloc.putFloat(i, memAlloc.getFloat(i + this.size_t));
            }

            this.indexPtr -= this.size_t;
            return prevValue;
        }

        memAlloc.putFloat(idx, 0.0f);
        return prevValue;
    }
}

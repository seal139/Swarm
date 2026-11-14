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
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * High performance Float Vector.
 */
public final class FloatVector extends NativeVector<Float> implements NativeList<Float> {

    protected final class FloatSplitter extends NativeSplitter<Float> {
        protected FloatSplitter(NativeArray source, long lBound, long uBound) {
            super(source, lBound, uBound);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Float> action) {
            if (this.index >= this.maxIndex) {
                return false;
            }

            action.accept(this.source.getFloat(this.index++));
            return true;
        }

        @Override
        public Spliterator<Float> trySplit() {
            final long remaining = estimateSize();
            if (remaining <= 1L) {
                return null;
            }

            final long half = (remaining / 2L) + this.index;

            final FloatSplitter splitter = new FloatSplitter(this.source, this.index, half);
            this.index = half;
            this.size  = this.uBound - half;

            return splitter;
        }
    }

    public FloatVector() {
        super(10L, 4L);
    }

    public FloatVector(long initialSize) {
        super(initialSize, 4L);
    }

    public FloatVector(long address, long size) {
        super(address, size, 4);
    }

    public FloatVector(FloatVector original, long offset, long size) {
        super(original, offset, size);
    }

    // ================

    @Override
    public void set(long index, Float value) {
        setPrimitive(index, value.floatValue());
    }

    public void setPrimitive(long index, float value) {
        this.container.set(this.offset + index, value);
    }

    @Override
    public Float get(long index) {
        return getPrimitive(index);
    }

    public float getPrimitive(long index) {
        return this.container.getFloat(this.offset + index);
    }

    // Secondary

    @Override
    public boolean add(Float value) {
        return addPrimitive(value.floatValue());
    }

    public boolean addPrimitive(float value) {
        if (this.uBound >= capacity()) {
            if (this.sublist || this.container.isStaticArr()) {
                return false;
            }

            final long newSize = ((long) (this.capacity * 0.4) + this.capacity + 1);

            this.container.resize(newSize * 4L);
            this.capacity = newSize;
        }

        setPrimitive(this.uBound++, value);
        return true;
    }

    @Override
    public boolean remove(long index) {
        if ((index < 0L) || (this.uBound <= 0L) || (index >= this.uBound)) {
            return false;
        }

        long lidx = --this.uBound;
        if (lidx > index) {
            setPrimitive(index, getPrimitive(lidx));
        }

        return true;
    }

    @Override
    public boolean remove(Float value) {
        return removePrimitive(value.floatValue());
    }

    public boolean removePrimitive(float value) {
        return remove(indexOf(value));
    }

    @Override
    public void clear() {
        if (!this.sublist) {
            this.container.reset(this.initialSize * 4L);

            this.capacity = this.initialSize;
        }
        else {
            for (long i = 0; i < this.uBound; i++) {
                setPrimitive(i, 0.0f);
            }
        }

        this.uBound = 0L;
    }

    @Override
    public long indexOf(Float value) {
        return indexOfPrimitive(value.floatValue());
    }

    public long indexOfPrimitive(float value) {
        for (long idx = 0; idx < this.uBound; idx++) {
            float val = get(idx);

            if (val == value) {
                return idx;
            }
        }

        return -1;
    }

    @Override
    public long lastIndexOf(Float value) {
        return lastIndexOfPrimitive(value.floatValue());
    }

    public long lastIndexOfPrimitive(float value) {
        for (long idx = this.uBound - 1; idx >= 0; idx--) {
            float val = get(idx);

            if (val == value) {
                return idx;
            }
        }

        return -1;
    }

    // Extension

    public boolean addAll(float... c) {
        if (c == null) {
            return false;
        }

        final int size = c.length;

        if (size == 0) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            add(c[i]);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<Float> c) {
        final long oldSize = this.uBound;

        long maxIdx = this.uBound;
        while (--maxIdx >= 0) {
            final float val = getPrimitive(maxIdx);

            if (c.contains(val)) {
                remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
    }

    @Override
    public boolean retainAll(Collection<Float> c) {
        final long oldSize = this.uBound;

        long maxIdx = this.uBound;
        while (--maxIdx >= 0) {
            final float val = getPrimitive(maxIdx);

            if (!c.contains(val)) {
                remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
    }

    @Override
    public Iterator<Float> iterator() {
        return new Iterator<>() {
            private final NativeArray arr = FloatVector.this.container;

            private long currentIndex = FloatVector.this.offset;
            private long maxIndex     = FloatVector.this.uBound + FloatVector.this.offset;

            @Override
            public boolean hasNext() {
                return this.currentIndex < this.maxIndex;
            }

            @Override
            public Float next() {
                return this.arr.getFloat(this.currentIndex++);
            }

            @Override
            public void forEachRemaining(Consumer<? super Float> action) {
                while (hasNext()) {
                    action.accept(next());
                }
            }
        };
    }

    @Override
    public FloatVector subList(long fromIndex, long size) {
        return new FloatVector(this, fromIndex, size);
    }

    @Override
    public Spliterator<Float> spliterator() {
        return new FloatSplitter(this.container, this.offset, this.offset + this.uBound);
    }

    @Override
    public void forEach(Consumer<? super Float> action) {
        for (long i = 0; i < this.uBound; i++) {
            action.accept(getPrimitive(i));
        }
    }

    public void forEachPrimitive(FloatConsumer action) {
        for (long i = 0; i < this.uBound; i++) {
            action.accept(getPrimitive(i));
        }
    }

    @Override
    public Float[][] toArray() {
        final long maxSize = Integer.MAX_VALUE;

        final Stack<Spliterator<Float>> tmp = new Stack<>();
        tmp.push(spliterator());

        final Float[][] ret = new Float[(int) (size() % maxSize) + 1][];

        Spliterator<Float> splitter;

        int iter = -1;
        do {
            splitter = tmp.pop();

            if (splitter.estimateSize() > maxSize) {
                tmp.push(splitter);
                tmp.push(splitter.trySplit());
                continue;
            }

            final Float[] f = new Float[(int) splitter.estimateSize()];
            ret[++iter] = f;

            int[] indexer = {
                    -1 };
            splitter.forEachRemaining(v -> {
                f[++indexer[0]] = v;
            });
        }
        while (!tmp.isEmpty());

        return ret;
    }

    public interface FloatConsumer {
        void accept(float arg);
    }

}

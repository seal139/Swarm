/*
* Swarm - An Extensible and Modular GPGPU framework
* Copyright (C) 2026  Septian Pramana
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

import java.lang.foreign.MemorySegment;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * High performance Float Vector.
 */
public final class FloatVector extends NativeVector<Float> {

	private static final long serialVersionUID = -4712556364774789210L;

	@Override
	protected long getDataSize() {
		return 4L;
	}
	
    protected final class FloatSplitter extends NativeSplitter<Float> {
        protected FloatSplitter(NativeArray source, long lBound, long uBound) {
            super(source, lBound, uBound);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Float> action) {
            if (this.index >= this.uBound) {
                return false;
            }

            action.accept(this.source.getFloat(this.index++));
            this.size -=1;
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

    public FloatVector(long address, long size, boolean empty) {
        super(address, size, 4, empty);
    }

    public FloatVector(FloatVector original, long offset, long size) {
        super(original, offset, size);
    }

    // ================

    @Override
    public void set(long index, Float value) {
        set(index, value.floatValue());
    }
    
    public void set(long index, float value) {
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
        return add(value.floatValue());
    }
    
    public boolean add(float value) {
    	if (this.uBound >= capacity()) {
            if (this.sublist || this.container.isStaticArr()) {
                return false;
            }

            final long newSize = ((long) (this.capacity * 0.4) + this.capacity + 1);

            this.container.resize(newSize * 4L);
            this.capacity = newSize;
        }

        set(this.uBound++, value);
        return true;
    }

    public boolean addAllPrimitive(float... c) {
    	if(c == null) {
    		return false;
    	}
    	
        final int size = c.length;

        if (size == 0) {
            return false;
        }
        
        final long remaining = this.capacity - (this.size() + c.length);
        if(remaining < 0) {
    		if (this.sublist || this.container.isStaticArr()) {
                return false;
            }
    		
            final long geometricSize = ((long) (this.capacity * 0.4) + this.capacity + 1);
            final long exactSize     = this.capacity - remaining;
    		
    		this.capacity = Math.max(geometricSize, exactSize);
    		container.resize(this.capacity * 4);
    	}
        
        final MemorySegment srcSegment = MemorySegment.ofArray(c);
    	container.memcpy(srcSegment, (this.offset + this.size()) * 4L, 0L, (long)size * 4);

        return true;
    }
    
    @Override
	public boolean addAll(NativeList<Float> c) {
    	if(c instanceof FloatVector o) {
    		return addAll(o);
    	}
    	
    	return super.addAll(c);
    }
    
    public boolean addAll(FloatVector c) {
    	if(c == null) {
    		return false;
    	}
    	
    	 if (c.size() == 0) {
             return false;
         }
    	 
    	 final long totalSize = this.size() + c.size();
    	 
    	final long remaining = this.capacity - totalSize;
    	if(remaining < 0) {
    		if (this.sublist || this.container.isStaticArr()) {
                return false;
            }
    		
            final long geometricSize = ((long) (this.capacity * 0.4) + this.capacity + 1);
            final long exactSize     = this.capacity - remaining;
    		
    		this.capacity = Math.max(geometricSize, exactSize);
    		container.resize(this.capacity * 4);
    	}
    	
    	container.memcpy(c.container, (this.offset + this.size()) * 4, c.offset * 4, c.size() * 4);
    	uBound = totalSize;
		return true;
    }
    
    @Override
    public long indexOf(Float value) {
        return indexOf(value.floatValue());
    }
    
    public long indexOf(float value) {
    	for (long idx = 0; idx < this.uBound; idx++) {
            float val = getPrimitive(idx);

            if (val == value) {
                return idx;
            }
        }

        return -1;
    }

    @Override
    public long lastIndexOf(Float value) {
        return lastIndexOf(value.floatValue());
    }
    
    public long lastIndexOf(float value) {
    	for (long idx = this.uBound - 1; idx >= 0; idx--) {
            float val = getPrimitive(idx);

            if (val == value) {
                return idx;
            }
        }

        return -1;
    }

    @Override
    public boolean remove(long index) {
        if ((index < 0L) || (this.uBound <= 0L) || (index >= this.uBound)) {
            return false;
        }

        long lidx = --this.uBound;
        if (lidx > index) {
            set(index, getPrimitive(lidx));
        }

        return true;
    }
    
    @Override
    public boolean remove(Float value) {
        return remove(value.floatValue());
    }
    
    public boolean remove(float value) {
    	return remove(indexOf(value));
    }
    
    @Override
	public boolean removeAll(NativeList<Float> c) {
    	if(c instanceof FloatVector o) {
    		return removeAll(o);
    	}

    	return super.removeAll(c);
	}
    
    public boolean removeAll(FloatVector c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.size() == 0) {
    		return false;
    	}
    	
    	final long oldSize = this.uBound;
   	 	final long ctr = c.size();
   	 
        long maxIdx = this.uBound;
        next: while (--maxIdx >= 0) {
            final float val = getPrimitive(maxIdx);
            
            for(int i = 0; i < ctr; i++) {
	           	if(c.getPrimitive(i) == val) {
	           		remove(maxIdx);
	           		continue next;
	            }
            }
        }

        return oldSize != this.uBound;
    }
    
    public boolean contains(float o) {
    	return indexOf(o) >= 0;
	}

    @Override
    public boolean containsAll(NativeList<Float> c) {
    	if(c instanceof FloatVector o) {
    		return containsAll(o);
    	}
    	
    	return super.containsAll(c);
    }
    
    public boolean containsAll(FloatVector c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.size() == 0) {
    		return false;
    	}
    	
    	final long totalIter = c.size() * size();
    	
    	if(totalIter > THRESHOLD) {
			return c.parallelStream().allMatch(v -> this.contains(v.floatValue()));
    	}
    	
    	for(long i = 0; i < c.size(); i++) {
    		if(!this.contains(c.getPrimitive(i))) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    @Override
    public void clear() {
        if (!this.sublist) {
            this.container.reset(this.initialSize * 4L);
            this.capacity = this.initialSize;
        }
        else {
            for (long i = 0; i < this.uBound; i++) {
                set(i, 0.0f);
            }
        }

        this.uBound = 0L;
    }

    // Extension

    @Override
   	public boolean retainAll(NativeList<Float> c) {
    	if(c instanceof FloatVector o) {
    		return retainAll(o);
    	}
    	
    	return super.retainAll(c);
   }
    
    public boolean retainAll(FloatVector c) {
    	if(c == null) {
    		clear();
    		return true;
    	}
    	
    	if(c.size() == 0) {
    		clear();
    		return true;
    	}
    	
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
    public FloatVector subList(long fromIndex, long toIndex) {
        return new FloatVector(this, fromIndex, toIndex - fromIndex);
    }

    @Override
    public Spliterator<Float> spliterator() {
        return new FloatSplitter(this.container, this.offset, this.offset + this.uBound);
    }

    @Override
    public void forEach(Consumer<? super Float> action) {
        for (long i = 0; i < this.uBound; i++) {
            action.accept(get(i));
        }
    }
    
    public void forEachPrimitive(FloatConsumer action) {
        for (long i = 0; i < this.uBound; i++) {
            action.accept(getPrimitive(i));
        }
    }

    public interface FloatConsumer {
        void accept(float arg);
    }
}

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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Spliterator;
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
public abstract class NativeVector<T extends Number> implements NativeList<T> {

	private static final long serialVersionUID = 2601589818012298743L;

	protected static final long THRESHOLD = 10_000_000L;
	
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

    protected NativeVector(long address, long size, long size_t, boolean empty) {
        this.sublist = true;

        this.offset = 0L;
        this.uBound = empty? 0L : size;

        this.capacity    = size;
        this.initialSize = size;

        this.container = new NativeArray(address, size * size_t);
    }

    protected NativeVector(NativeVector<T> original, long offset, long size) {
        if ((offset < 0) || (size < 0) || ((offset + size) > (original.uBound))) {
            throw new IndexOutOfBoundsException();
        }

        this.sublist = true;

        this.offset = offset + original.offset;
        this.uBound = size;

        this.capacity    = size;
        this.initialSize = size;

        this.container = original.container;
    }
    
    protected abstract long getDataSize();

    @Override
    public long getNativeAddress() {
    	return container.getNativeAddress() + offset;
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
    public long indexOf(Object o) {
        for (long idx = 0; idx < this.uBound; idx++) {
            T val = get(idx);

            if (o.equals(val)) {
                return idx;
            }
        }

        return -1;
    }

    @Override
    public long lastIndexOf(Object o) {
        for (long idx = this.uBound - 1; idx >= 0; idx--) {
            T val = get(idx);

            if (o.equals(val)) {
                return idx;
            }
        }

        return -1;
    }
    
    @Override
    public boolean removeFirst() {
        return remove(0);
    }

    @Override
    public boolean removeLast() {
    	if (this.uBound <= 0L) {
            return false;
        }
    	
    	uBound -=1;
    	return true;
    }
    
    @Override
	public boolean addAll(@SuppressWarnings("unchecked") T... c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if (c.length == 0) {
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
    		container.resize(this.capacity * getDataSize());
    	}
    	
    	for(T val : c) {
        	set(uBound++, val);
    	}
    	
    	return true;
    }
    
    @Override
    public boolean addAll(Collection<T> c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if (c.size() == 0) {
            return false;
        }
    	
    	final long remaining = this.capacity - (this.size() + c.size());
    	if(remaining < 0) {
    		if (this.sublist || this.container.isStaticArr()) {
                return false;
            }
    		
            final long geometricSize = ((long) (this.capacity * 0.4) + this.capacity + 1);
            final long exactSize     = this.capacity - remaining;
    		
    		this.capacity = Math.max(geometricSize, exactSize);
    		container.resize(this.capacity * getDataSize());
    	}
    	
    	for(T val : c) {
    		set(uBound++, val);
    	}
    	
    	return true;
    }
    
    @Override
	public boolean addAll(NativeList<T> c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if (c.size() == 0) {
            return false;
        }
    	 
    	final long remaining = this.capacity - (this.size() + c.size());
    	if(remaining < 0) {
    		if (this.sublist || this.container.isStaticArr()) {
                return false;
            }
    		
            final long geometricSize = ((long) (this.capacity * 0.4) + this.capacity + 1);
            final long exactSize     = this.capacity - remaining;
    		
    		this.capacity = Math.max(geometricSize, exactSize);
    		container.resize(this.capacity * getDataSize());
    	}
    	
    	for(T val : c) {
    		set(uBound++, val);
    	}
    	
    	return true;
	}
    
    @Override
    public boolean contains(T o) {
        return indexOf(o) >= 0;
    }
    
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(@SuppressWarnings("unchecked") T... c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.length == 0) {
    		return false;
    	}
    	
        for(T val : c) {
        	if (indexOf(val) < 0L) {
                return false;
            }
    	}

        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.size() == 0) {
    		return false;
    	}
    	
    	final long totalIter = c.size() * size();

    	if(totalIter > THRESHOLD) {
			return c.parallelStream().allMatch(v -> this.contains(v));
    	}
    	
    	for(Object val : c) {
    		if(!this.contains(val)) {
    			return false;
    		}
    	}
    	
    	return true;
    }

    @Override
    public boolean containsAll(NativeList<T> c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.size() == 0) {
    		return false;
    	}
    	
    	final long totalIter = c.size() * size();
    	
    	if(totalIter > THRESHOLD) {
			return c.parallelStream().allMatch(v -> this.contains(v));
    	}
    	
    	for(T val : c) {
    		if(!this.contains(val)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    @Override
   	public boolean retainAll(NativeList<T> c) {
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
           final T val = get(maxIdx);
           
           if (!c.contains(val)) {
               remove(maxIdx);
           }
       }

       return oldSize != this.uBound;
   }
   @Override
   public boolean retainAll(@SuppressWarnings("unchecked")    T ... c) {
    	if(c == null) {
    		clear();
    		return true;
    	}
    	
    	if(c.length == 0) {
    		clear();
    		return true;
    	}
    	
    	final Set<T> ref = new HashSet<>();
    	for(T val : c) {
    		ref.add(val);
    	}
    	
    	final long oldSize = this.uBound;
   	 
        long maxIdx = this.uBound;
        while (--maxIdx >= 0) {
        	final T val = get(maxIdx);
        	if(!ref.contains(val)) {
            	remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
    }
   
    @Override
    public boolean retainAll(Collection<T> c) {
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
            final T val = get(maxIdx);

            if (!c.contains(val)) {
                remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
    }

    @Override
    public boolean removeAll(@SuppressWarnings("unchecked") T... c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.length == 0) {
    		return false;
    	}
    	
    	final Set<T> ref = new HashSet<>();
    	for(T val : c) {
    		ref.add(val);
    	}
    	
    	final long oldSize = this.uBound;
    	
        long maxIdx = this.uBound;
        while (--maxIdx >= 0) {
            final T val = get(maxIdx);
            if(ref.contains(val)) {
            	remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
    }
    
    @Override
	public boolean removeAll(NativeList<T> c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.size() == 0) {
    		return false;
    	}
    	
        final long oldSize = this.uBound;

        long maxIdx = this.uBound;
        while (--maxIdx >= 0) {
            final T val = get(maxIdx);

            if (c.contains(val)) {
            	remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
	}
    
    @Override
    public boolean removeAll(Collection<T> c) {
    	if(c == null) {
    		return false;
    	}
    	
    	if(c.size() == 0) {
    		return false;
    	}
    	
        final long oldSize = this.uBound;

        long maxIdx = this.uBound;
        while (--maxIdx >= 0) {
            final T val = get(maxIdx);

            if (c.contains(val)) {
            	remove(maxIdx);
            }
        }

        return oldSize != this.uBound;
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
        final long chunkCount = 1 + (int) ((size() - 1) / Integer.MAX_VALUE);

        final List<List<T>> retChunk = new ArrayList<>();
        for (long sequence = 0; sequence < chunkCount; sequence++) {
        	final long from = Integer.MAX_VALUE * sequence;
            final long to   = Math.min(size(), from + Integer.MAX_VALUE);

            retChunk.add(to == 0 ? List.of() : new ReadOnlyListMock(subList(from, to)));
        }

        return Collections.unmodifiableList(retChunk);
    }

    private final class ReadOnlyListMock implements List<T>, Serializable, RandomAccess {
        private static final long serialVersionUID = -6927485013065693623L;
		private final NativeList<T> ref;

        private ReadOnlyListMock(NativeList<T> reference) {
            this.ref = reference;
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
        	return toIndex == fromIndex ? List.of() : new ReadOnlyListMock(this.ref.subList(fromIndex, toIndex));
        }

        @Override
        public boolean contains(Object o) {
            return this.ref.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return this.ref.containsAll(c);
        }

        @Override
        public T get(int index) {
            return this.ref.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return (int) this.ref.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return (int) this.ref.lastIndexOf(o);
        }

        @Override
        public int size() {
            return (int) this.ref.size();
        }

        @Override
        public boolean isEmpty() { return ref.isEmpty(); }

        @Override
        public Object[] toArray() {
        	return toArray(new Object[] {});        	
        }

		@Override
        @SuppressWarnings("unchecked")
        public <E> E[] toArray(E[] a) {
        	 int currentSize = size();
        	 if(a.length < currentSize) {
        		 a = (E[]) Array.newInstance(a.getClass().getComponentType(), currentSize);
        	 }

             int i = 0;
             for (T item : ReadOnlyListMock.this) {
                 a[i++] = (E) item;
             }

             return a;
		}
        
        @Override
        public Iterator<T> iterator() {
            return ref.iterator();
        }
        
        @Override
        public ListIterator<T> listIterator(int index) {
        	return new ListIterator<T>() {
        		final int offset = index;
        		final int uBound = (int) (ref.size() - 1);
        		
        		int pointer = 0;
        		
				@Override
				public boolean hasNext() {
					return pointer < uBound;
				}

				@Override
				public boolean hasPrevious() {
					return pointer > offset;
				}

				@Override
				public T next() {
					return ref.get(pointer++);
				}

				@Override
				public int nextIndex() {
					return pointer + 1;
				}

				@Override
				public T previous() {
					return ref.get(--pointer);
				}

				@Override
				public int previousIndex() {
					return pointer - 1;
				}
				
				@Override
				public void add(T arg0) {
					throw new UnsupportedOperationException("Operation is not supported");
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("Operation is not supported");					
				}

				@Override
				public void set(T arg0) {
					throw new UnsupportedOperationException("Operation is not supported");
				}

        	};
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
            throw new UnsupportedOperationException("Operation is not supported");
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
    }
    
    protected abstract static class NativeSplitter<E> implements Spliterator<E> {
        protected final NativeArray source;

        protected long size;

        protected long uBound;
        protected long index;

        protected NativeSplitter(NativeArray source, long lBound, long uBound) {
            this.source = source;

            this.index  = lBound;
            this.uBound = uBound;

            this.size = uBound - lBound;
        }

        @Override
        public int characteristics() {
            return Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }

        @Override
        public long estimateSize() {
            return this.size;
        }
    }
}

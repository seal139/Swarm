package io.github.seal139.jSwarm.datatype;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.github.seal139.jSwarm.misc.NativeCleaner;
import io.github.seal139.jSwarm.misc.NativeCleaner.NativeResources;
import io.github.seal139.jSwarm.misc.NativeCleaner.NativeResources.Deallocator;

public interface NativeList<E extends Number> extends Iterable<E> {

    static NativeArray ofStatic(long size) {
        return new NativeArray(size);
    }

    static NativeArray atAddress(long address, long size) {
        return new NativeArray(address, size);
    }

    static final class DeallocatorImpl implements Deallocator {
        private Arena arena;

        @Override
        public void clean() {
        	if(arena != null) {
        		this.arena.close();
        	}
        	
        	arena = null;
        }
    }

    public static final class NativeArray implements NativeResources {
        final DeallocatorImpl deallocator = new DeallocatorImpl();

        @Override
        public Deallocator getDeallocator() { return this.deallocator; }

        @Override
        public boolean isClosed() { return !this.staticArr && deallocator.arena == null; }

        @Override
        public void close() throws Exception {
            this.deallocator.clean();
        }

        // ===========================================================

        private final static Arena GLOBAL_ARENA = Arena.global();

        private final boolean staticArr;

        private long intptr;
        private long size;

        private MemorySegment primarySegment;

        public long getSize() { return this.size; }

        public long getIntptr() { return this.intptr; }

        public boolean isStaticArr() { return this.staticArr; }

        @Override
        public String toString() {
            return "NativeArray @" + String.valueOf(this.intptr);
        }

        protected NativeArray(long address, long size) {
            this.staticArr = true;

            this.primarySegment = MemorySegment.ofAddress(address).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);

            this.size   = size;
            this.intptr = address;
        }

        protected NativeArray(long initialSize) {
            this.staticArr = false;

            this.deallocator.arena = Arena.ofShared();

            this.primarySegment = deallocator.arena.allocate(initialSize).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);

            this.size   = initialSize;
            this.intptr = this.primarySegment.address();

            NativeCleaner.register(this);
        }

        public void reset(long size) {
            if (this.staticArr) {
                throw new IllegalArgumentException("Cannot resize static array");
            }

            final Arena newArena = Arena.ofShared();

            this.primarySegment = newArena.allocate(size).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);
            this.intptr         = this.primarySegment.address();
            this.size           = size;

            deallocator.arena.close();
            this.deallocator.arena = newArena;
        }

        public void resize(long size) {
            if (this.staticArr) {
                throw new IllegalArgumentException("Cannot resize static array");
            }

            final Arena         newArena   = Arena.ofShared();
            final MemorySegment newSegment = newArena.allocate(size).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);

            final long _size = this.size < size ? this.size : size;

            MemorySegment.copy(this.primarySegment, 0, newSegment, 0, _size);

            this.primarySegment    = newSegment;
            this.intptr 		   = this.primarySegment.address();
            this.size              = size;

            deallocator.arena.close();
            this.deallocator.arena = newArena;
        }
        
        public void memcpy(NativeArray source, long destOffset, long srcOffset, long size) {
        	MemorySegment.copy(source.primarySegment, destOffset, primarySegment, srcOffset, size);
        }

        public void set(long index, byte value) {
            this.primarySegment.setAtIndex(ValueLayout.JAVA_BYTE, index, value);
        }

        public void set(long index, short value) {
            this.primarySegment.setAtIndex(ValueLayout.JAVA_SHORT, index, value);
        }

        public void set(long index, int value) {
            this.primarySegment.setAtIndex(ValueLayout.JAVA_INT, index, value);
        }

        public void set(long index, long value) {
            this.primarySegment.setAtIndex(ValueLayout.JAVA_LONG, index, value);
        }

        public void set(long index, float value) {
            this.primarySegment.setAtIndex(ValueLayout.JAVA_FLOAT, index, value);
        }

        public void set(long index, double value) {
            this.primarySegment.setAtIndex(ValueLayout.JAVA_DOUBLE, index, value);
        }

        public byte getByte(long index) {
            return this.primarySegment.getAtIndex(ValueLayout.JAVA_BYTE, index);
        }

        public short getShort(long index) {
            return this.primarySegment.getAtIndex(ValueLayout.JAVA_SHORT, index);
        }

        public int getInt(long index) {
            return this.primarySegment.getAtIndex(ValueLayout.JAVA_INT, index);
        }

        public long getLong(long index) {
            return this.primarySegment.getAtIndex(ValueLayout.JAVA_LONG, index);
        }

        public float getFloat(long index) {
            return this.primarySegment.getAtIndex(ValueLayout.JAVA_FLOAT, index);
        }

        public double getDouble(long index) {
            return this.primarySegment.getAtIndex(ValueLayout.JAVA_DOUBLE, index);
        }

        public long getCapacityByte() { return this.size; }

        public long getCapacityShort() { return this.size / 2; }

        public long getCapacityInt() { return this.size / 4; }

        public long getCapacityLong() { return this.size / 8; }

        public long getCapacityFloat() { return this.size / 4; }

        public long getCapacityDouble() { return this.size / 8; }
    }

    // Primary
    void set(long index, E value);
    E get(long index);
    long size();
    long capacity();

    // Secondary
    boolean add(E arg0);

    boolean remove(long index);
    boolean remove(E o);

    void clear();

    long indexOf(Object o);
    long indexOf(E o);

    long lastIndexOf(Object o);
    long lastIndexOf(E o);

    // Extension
    boolean addAll(Collection<E> c);

    boolean removeAll(Collection<E> c);

    boolean retainAll(Collection<E> c);

    default boolean isEmpty() { return size() == 0L; }

    default E getFirst() { return get(0); }

    default E getLast() { return get(size() - 1); }

    boolean removeFirst() ;
    boolean removeLast() ;

    boolean containsAll(Collection<?> c);
    
    boolean containsAll(NativeList<E> c);

    boolean contains(E o) ;

    boolean contains(Object o) ;

    // Interopability
    List<List<E>> asList();

    Stream<E> stream() ;

    Stream<E> parallelStream() ;

    Iterator<E> iterator();

    NativeList<E> subList(long fromIndex, long toIndex);

    void forEach(Consumer<? super E> action);

    Spliterator<E> spliterator();

    abstract static class NativeSplitter<E> implements Spliterator<E> {
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

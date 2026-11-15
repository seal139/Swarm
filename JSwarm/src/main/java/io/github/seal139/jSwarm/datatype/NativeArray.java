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

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.misc.NativeCleaner;
import io.github.seal139.jSwarm.misc.NativeCleaner.NativeResources;

/**
 * Handles off-heap native array
 */
public final class NativeArray implements NativeResources {
	private static final class DeallocatorImpl implements Deallocator {
        private Arena arena;

        @Override
        public void clean() {
        	if(arena != null) {
        		this.arena.close();
        	}
        	
        	arena = null;
        }
    }
	
	private final DeallocatorImpl deallocator = new DeallocatorImpl();

    @Override
    public Deallocator getDeallocator() { return this.deallocator; }

    /**
     * Get the object state. Return true if object is already closed and memory has been released. Otherwise, false
     */
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

    /**
     * Get the size of allocated heap. The returned value is memory capacity. Not the size of already inserted data.
     * @return Allocated memory capacity in byte
     */
    public long getSize() { return this.size; }

    /**
     * Get native off-heap pointer address. This value is not static and may change upon resize or reset
     * @return Off-heap pointer address
     */
    public long getNativeAddress() { return this.intptr; }

    /**
     * Get the array behavior. Static array is fixed and non-resizeable
     * @return True if array is non-resizeable. Otherwise, false
     */
    public boolean isStaticArr() { return this.staticArr; }

    @Override
    public String toString() {
        return "NativeArray @" + hashCode();
    }

    /**
     * Create new instance of NativeArray on specified memory address and size. 
     * This constructor will result of non-resize-able array
     * @param address Native memory address
     * @param size The size of allocated memory
     */
    protected NativeArray(long address, long size) {
        this.staticArr = true;

        if(Common.isDebugMode()) {
            this.primarySegment = MemorySegment.ofAddress(address).reinterpret(size, GLOBAL_ARENA, null);
        }else {
            this.primarySegment = MemorySegment.ofAddress(address).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);
        }

        this.size   = size;
        this.intptr = address;
    }

    /**
     * Create new instance of NativeArray with specified size. This constructor will result of resize-able array
     * @param initialSize Initial allocation size
     */
    protected NativeArray(long initialSize) {
        this.staticArr = false;

        this.deallocator.arena = Arena.ofShared();

        if(Common.isDebugMode()) {
            this.primarySegment = deallocator.arena.allocate(initialSize);
        }else {
        	this.primarySegment = deallocator.arena.allocate(initialSize).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);
        }
        
        this.size   = initialSize;
        this.intptr = this.primarySegment.address();

        NativeCleaner.register(this);
    }

    /**
     * Delete already allocated memory, removing all data and allocate new fresh ones with specified size. 
     * This behavior is the same as creating new instance of {@link NativeArray}
     * @param size Initial size
     */
    public void reset(long size) {
        if (this.staticArr) {
            throw new IllegalArgumentException("Cannot resize static array");
        }

        final Arena newArena = Arena.ofShared();

        if(Common.isDebugMode()) {
        	this.primarySegment = newArena.allocate(size);
        }else {
        	this.primarySegment = newArena.allocate(size).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);
        }
        
        this.intptr         = this.primarySegment.address();
        this.size           = size;

        deallocator.arena.close();
        this.deallocator.arena = newArena;
    }

    /**
     * Resize allocated memory to specified size and migrate data from old memory into the new one.
     * If the new allocated size is less than previous, the overflowing data will be discarded
     * @param size new size
     */
    public void resize(long size) {
        if (this.staticArr) {
            throw new IllegalArgumentException("Cannot resize static array");
        }

        final Arena         newArena   = Arena.ofShared();
        
        final MemorySegment newSegment;
        if(Common.isDebugMode()) {
        	newSegment = newArena.allocate(size);
        }else {
        	newSegment = newArena.allocate(size).reinterpret(Long.MAX_VALUE, GLOBAL_ARENA, null);
        }

        final long _size = this.size < size ? this.size : size;

        MemorySegment.copy(this.primarySegment, 0, newSegment, 0, _size);

        this.primarySegment    = newSegment;
        this.intptr 		   = this.primarySegment.address();
        this.size              = size;

        deallocator.arena.close();
        this.deallocator.arena = newArena;
    }
    
    /**
     * Bulk copy from another {@link NativeArray}
     * @param source Source data to copy
     * @param destOffset Start offset index where copied data will be placed
     * @param srcOffset Source offset index
     * @param size Count of data to be copy
     */
    public void memcpy(NativeArray source, long destOffset, long srcOffset, long size) {
    	MemorySegment.copy(source.primarySegment, srcOffset, primarySegment, destOffset, size);
    }
    
    /**
     * Bulk copy from another {@link MemorySegment}
     * @param source Source data to copy
     * @param destOffset Start offset index where copied data will be placed
     * @param srcOffset Source offset index
     * @param size Count of data to be copy
     */
    public void memcpy(MemorySegment source, long destOffset, long srcOffset, long size) {
    	MemorySegment.copy(source, srcOffset, primarySegment, destOffset, size);
    }

    /**
     * Set byte value at specified index
     * @param index Memory index where data to be placed
     * @param value Data to be placed
     */
    public void set(long index, byte value) {
        this.primarySegment.setAtIndex(ValueLayout.JAVA_BYTE, index, value);
    }

    /**
     * Set short value at specified index
     * @param index Memory index where data to be placed
     * @param value Data to be placed
     */
    public void set(long index, short value) {
        this.primarySegment.setAtIndex(ValueLayout.JAVA_SHORT, index, value);
    }

    /**
     * Set int value at specified index
     * @param index Memory index where data to be placed
     * @param value Data to be placed
     */
    public void set(long index, int value) {
        this.primarySegment.setAtIndex(ValueLayout.JAVA_INT, index, value);
    }

    /**
     * Set long value at specified index
     * @param index Memory index where data to be placed
     * @param value Data to be placed
     */
    public void set(long index, long value) {
        this.primarySegment.setAtIndex(ValueLayout.JAVA_LONG, index, value);
    }

    /**
     * Set float value at specified index
     * @param index Memory index where data to be placed
     * @param value Data to be placed
     */
    public void set(long index, float value) {
        this.primarySegment.setAtIndex(ValueLayout.JAVA_FLOAT, index, value);
    }

    /**
     * Set double value at specified index
     * @param index Memory index where data to be placed
     * @param value Data to be placed
     */
    public void set(long index, double value) {
        this.primarySegment.setAtIndex(ValueLayout.JAVA_DOUBLE, index, value);
    }

    /**
     * Get value as byte
     * @param index Index of value to be read
     * @return byte value
     */
    public byte getByte(long index) {
        return this.primarySegment.getAtIndex(ValueLayout.JAVA_BYTE, index);
    }

    /**
     * Get value as short
     * @param index Index of value to be read
     * @return shot value
     */
    public short getShort(long index) {
        return this.primarySegment.getAtIndex(ValueLayout.JAVA_SHORT, index);
    }

    /**
     * Get value as int
     * @param index Index of value to be read
     * @return int value
     */
    public int getInt(long index) {
        return this.primarySegment.getAtIndex(ValueLayout.JAVA_INT, index);
    }

    /**
     * Get value as long
     * @param index Index of value to be read
     * @return long value
     */
    public long getLong(long index) {
        return this.primarySegment.getAtIndex(ValueLayout.JAVA_LONG, index);
    }

    /**
     * Get value as float
     * @param index Index of value to be read
     * @return float value
     */
    public float getFloat(long index) {
        return this.primarySegment.getAtIndex(ValueLayout.JAVA_FLOAT, index);
    }

    /**
     * Get value as double
     * @param index Index of value to be read
     * @return double value
     */
    public double getDouble(long index) {
        return this.primarySegment.getAtIndex(ValueLayout.JAVA_DOUBLE, index);
    }

    /**
     * Get capacity of allocated memory as byte
     * @return Number of value capacity for byte
     */
    public long getCapacityByte() { return this.size; }

    /**
     * Get capacity of allocated memory as short
     * @return Number of value capacity for short
     */
    public long getCapacityShort() { return this.size / 2; }

    /**
     * Get capacity of allocated memory as int
     * @return Number of value capacity for int
     */
    public long getCapacityInt() { return this.size / 4; }

    /**
     * Get capacity of allocated memory as long
     * @return Number of value capacity for long
     */
    public long getCapacityLong() { return this.size / 8; }

    /**
     * Get capacity of allocated memory as float
     * @return Number of value capacity for float
     */
    public long getCapacityFloat() { return this.size / 4; }

    /**
     * Get capacity of allocated memory as double
     * @return Number of value capacity for double
     */
    public long getCapacityDouble() { return this.size / 8; }
}

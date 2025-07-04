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

package io.github.seal139.jSwarm.backend.ocl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Context;
import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.backend.Module;
import io.github.seal139.jSwarm.datatype.Vector;
import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.misc.Log;
import io.github.seal139.jSwarm.misc.NativeCleaner;
import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import io.github.seal139.jSwarm.runtime.NdRange;
import io.github.seal139.jSwarm.runtime.Program;
import io.github.seal139.jSwarm.runtime.SyncDirection;
import io.github.seal139.jSwarm.transpiler.Decompiler;
import sun.misc.Unsafe;

public class OclContext implements Context {

    static final class DeallocatorImpl implements Deallocator {
        private final long              address;
        private final List<Deallocator> moduleDec    = new ArrayList<>();
        private final List<Long>        queueAddress = new ArrayList<>();

        private int     queueSize;
        private boolean isClosed = false;

        private DeallocatorImpl(long address) {
            this.address = address;
        }

        @Override
        public void clean() {
            // Delete module and it's corresponding kernel
            this.moduleDec.forEach(Deallocator::clean);

            // Delete queue (stream)
            long[] addr = new long[this.queueSize];
            for (int i = 0; i < this.queueSize; i++) {
                addr[i] = this.queueAddress.get(i).longValue();
            }

            int r1 = OclDriver.oclDeleteQueue(addr, this.queueSize);
            if (r1 != 0L) {
                Log.error(new OclException(r1));
            }

            // Delete this object (context)
            int r2 = OclDriver.oclDeleteContext(this.address);
            if (r2 != 0L) {
                Log.error(new OclException(r2));
            }

            this.isClosed = true;
        }
    }

    // ============ Allocator - Deallocator ==================

    private final DeallocatorImpl deallocator;
    private final Ocl             ocl = Ocl.getInstance();
    private final OclDevice       device;

    @Override
    public Deallocator getDeallocator() { return this.deallocator; }

    OclContext(OclDevice device) throws OclException {
        final Unsafe mem = Common.getMemoryManagement();

        final long intptr = OclDriver.oclCreateContext(device.getDeviceId());

        int errorCode = (int) mem.getLong(intptr);
        if (errorCode != 0) {
            // Don't forget to deallocate memory
            mem.freeMemory(intptr);
            throw new OclException(errorCode);
        }

        this.deallocator = new DeallocatorImpl(mem.getLong(intptr + 8));
        this.device      = device;

        // By default, creating context will include 4 stream queue. this is useful for
        // concurrent data transfer
        this.deallocator.queueAddress.add(mem.getLong(intptr + 16));
        this.deallocator.queueAddress.add(mem.getLong(intptr + 24));
        this.deallocator.queueAddress.add(mem.getLong(intptr + 32));
        this.deallocator.queueAddress.add(mem.getLong(intptr + 40));
        this.deallocator.queueSize = 4;
        this.queueIdx              = 0;

        // Don't forget to deallocate memory
        mem.freeMemory(intptr);
        NativeCleaner.register(this);
    }

    @Override
    public void close() throws Exception {
        this.deallocator.clean();
    }

    @Override
    public boolean isClosed() { return this.deallocator.isClosed; }

    // ============ Functionality Operation ==================

    @Override
    public OclDevice getDevice() { return this.device; }

    @Override
    public void activate() throws BackendException {
        this.ocl.setContext(this.getAddress());
    }

    private int queueIdx;

    @Override
    public int getParallelismLevel() { return this.deallocator.queueSize; }

    @Override
    public void addParallelismLevel(int additionalNumber) throws OclException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        final Unsafe mem    = Common.getMemoryManagement();
        final long   intptr = OclDriver.oclAddQueue(this.device.getDeviceId(), this.ocl.getContextValue(), additionalNumber);

        int errorCode = (int) mem.getLong(intptr);
        if (errorCode != 0) {
            // Don't forget to deallocate memory
            mem.freeMemory(intptr);
            throw new OclException(errorCode);
        }

        this.deallocator.queueSize += additionalNumber;

        long lBound = intptr + 8;
        long uBound = lBound + (8 * additionalNumber);

        for (long queuePtr = lBound; queuePtr < uBound; queuePtr += 8) {
            this.deallocator.queueAddress.add(mem.getLong(queuePtr));
        }

        // Don't forget to deallocate memory
        mem.freeMemory(intptr);
    }

    @Override
    public Module loadProgram(Class<? extends Program> program) throws BackendException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        try {
            return loadProgram(Decompiler.process(new OclTranspiler(), program));
        }
        catch (IOException e) {
            throw new OclException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Module loadProgram(Class<? extends Program>... programs) throws BackendException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        String src = "";
        return loadProgram(src);
    }

    public Module loadProgram(Collection<Class<? extends Program>> programs) throws BackendException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        String src = "";
        return loadProgram(src);
    }

    public Module loadProgram(String program) throws BackendException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        return new OclModule(this, program);
    }

    @Override
    public void launch(Kernel kernel, NdRange ndRange, Number... arguments) throws BackendException, DeallocatedException {
        launchAsync(kernel, ndRange, arguments);
        waitOperation();
    }

    @Override
    public void launchAsync(Kernel kernel, NdRange ndRange, Number... arguments) throws BackendException, DeallocatedException {
        if (this.device.getMaxLocalThread() < (ndRange.getXLocal() * ndRange.getYLocal() * ndRange.getZLocal())) {
            throw new OclException("Local Worksize excedeed maximum thread");
        }

        long[] args   = new long[arguments.length];
        int[]  argRef = new int[arguments.length];

        {
            final int size = arguments.length;
            for (int i = 0; i < size; i++) {
                if (arguments[i] instanceof Vector v) {
                    args[i]   = v.getBufferAddress(this);
                    argRef[i] = 8;
                }
                else if (arguments[i] instanceof Double d) {
                    args[i]   = Double.doubleToRawLongBits(d.doubleValue());
                    argRef[i] = 8;
                }
                else if (arguments[i] instanceof Float f) {
                    args[i]   = Float.floatToRawIntBits(f.floatValue());
                    argRef[i] = 4;
                }
                else if (arguments[i] instanceof Long l) {
                    args[i]   = l.longValue();
                    argRef[i] = 8;
                }
                else if (arguments[i] instanceof Short s) {
                    args[i]   = s.shortValue();
                    argRef[i] = 2;
                }
                else {
                    args[i]   = arguments[i].intValue();
                    argRef[i] = 4;
                }
            }

        }

        OclDriver.oclLaunch(((OclKernel) kernel).getAddress(), hitQueueIndex(), //
                ndRange.getXGlobal() * ndRange.getXLocal(), ndRange.getYGlobal() * ndRange.getYLocal(), ndRange.getZGlobal() * ndRange.getZLocal(), //
                ndRange.getXLocal(), ndRange.getYLocal(), ndRange.getZLocal(), //
                args, argRef, args.length);
    }

    // ==== buffer memory management ====

    @Override
    public void hook(Vector<? extends Number> vector) throws OclException {
        final Unsafe mem = Common.getMemoryManagement();

        long intptr = OclDriver.oclHook(this.ocl.getContextValue(), vector.longSize() * vector.getValueSize());

        int errorCode = (int) mem.getLong(intptr);
        if (errorCode != 0) {
            // Don't forget to deallocate memory
            mem.freeMemory(intptr);
            throw new OclException(errorCode);
        }

        vector.setBuffer(this, mem.getLong(intptr + 8));

        // Don't forget to deallocate memory
        mem.freeMemory(intptr);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sync(SyncDirection direction, Vector<? extends Number>... dataCollection) throws OclException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        if (SyncDirection.TO_DEVICE.equals(direction)) {
            for (Vector<?> vec : dataCollection) {
                int errorCode = OclDriver.oclSyncDataTo(hitQueueIndex(), //
                        vec.getNativeAddress(), vec.getBufferAddress(this), vec.longSize() * vec.getValueSize());

                if (errorCode != 0) {
                    throw new OclException(errorCode);
                }
            }
        }

        for (Vector<?> vec : dataCollection) {
            int errorCode = OclDriver.oclSyncDataFrom(hitQueueIndex(), //
                    vec.getNativeAddress(), vec.getBufferAddress(this), vec.longSize() * vec.getValueSize());

            if (errorCode != 0) {
                throw new OclException(errorCode);
            }
        }
    }

    @Override
    public void unhook(Vector<? extends Number> vector) throws OclException {
        int errorCode = OclDriver.oclUnhook(vector.getBufferAddress(this));
        if (errorCode != 0) {
            throw new OclException(errorCode);
        }

        vector.removeBuffer(this);
    }

    @Override
    public void reHook(Vector<? extends Number> vector) throws OclException {
        int errorCode = OclDriver.oclUnhook(vector.getBufferAddress(this));
        if (errorCode != 0) {
            throw new OclException(errorCode);
        }

        hook(vector);
    }
    // ==== Fence ====

    @Override
    public void waitOperation() throws OclException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        long[] addr = new long[this.deallocator.queueSize];
        for (int i = 0; i < this.deallocator.queueSize; i++) {
            addr[i] = this.deallocator.queueAddress.get(i).longValue();
        }

        int errorCode = OclDriver.oclWaitAll(addr, this.deallocator.queueSize);
        if (errorCode != 0) {
            throw new OclException(errorCode);
        }
    }

    // ==== ====

    protected long hitQueueIndex() {
        if (this.queueIdx == this.deallocator.queueSize) {
            this.queueIdx = 0;
        }

        return this.deallocator.queueAddress.get(this.queueIdx++);
    }

    // =============== Object Operation ======================

    long getAddress() { return this.deallocator.address; }

    @Override
    public int hashCode() {
        // Use native memory address instead
        return (int) getAddress();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.hashCode() == hashCode() //
        ) && (obj instanceof OclContext //
        ) && (((OclModule) obj).getAddress() == getAddress());
    }
}

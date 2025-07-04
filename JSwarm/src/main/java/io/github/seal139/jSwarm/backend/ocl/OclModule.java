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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.seal139.jSwarm.backend.Module;
import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.misc.Log;
import io.github.seal139.jSwarm.misc.NativeCleaner;
import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import sun.misc.Unsafe;

// Status: Development done

public class OclModule implements Module {

    static final class DeallocatorImpl implements Deallocator {
        private final long       address;
        private final List<Long> kernelAddr = new ArrayList<>();

        private boolean isClosed = false;

        private DeallocatorImpl(long address) {
            this.address = address;
        }

        @Override
        public void clean() {
            if (this.isClosed) {
                return;
            }

            // Delete queue (stream)
            long[] addr = new long[this.kernelAddr.size()];
            for (int i = 0; i < this.kernelAddr.size(); i++) {
                addr[i] = this.kernelAddr.get(i).longValue();
            }

            int r1 = OclDriver.oclDeleteKernel(addr, this.kernelAddr.size());
            if (r1 != 0L) {
                Log.error(new OclException(r1));
            }

            int r2 = OclDriver.oclDeleteProgram(this.address);
            if (r2 != 0) {
                Log.error(new OclException(r2));
            }

            this.isClosed = true;
        }
    }

    // ============ Allocator - Deallocator ==================

    private final DeallocatorImpl deallocator;

    @Override
    public Deallocator getDeallocator() { return this.deallocator; }

    private final OclContext ctx;

    OclModule(OclContext ctx, String source) throws OclException {
        final Unsafe mem = Common.getMemoryManagement();

        final long intptr = OclDriver.oclCreateProgram(ctx.getDevice().getDeviceId(), ctx.getAddress(), source);

        int errorCode = (int) mem.getLong(intptr);
        if (errorCode != 0) {
            // Don't forget to deallocate memory
            mem.freeMemory(intptr);
            throw new OclException(errorCode);
        }

        this.ctx = ctx;

        this.deallocator = new DeallocatorImpl(mem.getLong(intptr + 8));

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

    private final Map<String, OclKernel> kernelMap = new HashMap<>();

    @Override
    public OclKernel getKernel(String kernelName) throws OclException, DeallocatedException {
        if (isClosed()) {
            throw new DeallocatedException();
        }

        OclKernel kernel = this.kernelMap.get(kernelName);
        if (kernel == null) {
            kernel = new OclKernel(this.ctx, this, kernelName);
            this.kernelMap.put(kernelName, kernel);

            this.deallocator.kernelAddr.add(kernel.getAddress());

        }

        return kernel;
    }

    // =============== Object Operation ======================

    long getAddress() { return this.deallocator.address; }

    @Override
    public int hashCode() {
        return (int) getAddress();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.hashCode() == hashCode() //
        ) && (obj instanceof OclModule //
        ) && (((OclModule) obj).getAddress() == getAddress());
    }
}

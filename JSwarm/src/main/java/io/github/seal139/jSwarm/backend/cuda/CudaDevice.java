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

package io.github.seal139.jSwarm.backend.cuda;

import java.util.UUID;

import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.runtime.DeviceType;
import sun.misc.Unsafe;

public class CudaDevice implements Executor {
    private final DeviceType type = DeviceType.GPU_ACCELERATOR;

    private final int   deviceId;
    private CudaContext context;

    private final String name;
    private final long   computeUnit;
    private final long   totalMemory;
    private final int    maxNdRange = 3;
    private final long[] maxNdRangeVal;
    private final long[] maxNdRangeValLocal;
    private final long   maxThreadNdRange;
    private final long   flops;
    private final String uuid;

    CudaDevice(int ptr, int index) throws CudaException {
        this.deviceId = ptr;

        final Unsafe mem = Common.getMemoryManagement();

        this.name = CudaDriver.cudaGetDeviceName(index);
        final long infos = CudaDriver.cudaGetDeviceInfo(index);
        {
            this.computeUnit   = mem.getInt(infos);
            this.totalMemory   = mem.getLong(8L + infos);
            this.maxNdRangeVal = new long[] {
                    mem.getInt(16 + infos),              // x
                    mem.getInt(24L + infos),             // y
                    mem.getInt(32L + infos)              // z
            };

            this.maxNdRangeValLocal = new long[] {
                    mem.getInt(40 + infos), // x
                    mem.getInt(48L + infos), // y
                    mem.getInt(56L + infos) // z
            };

            this.maxThreadNdRange = mem.getInt(64L + infos);
            this.flops            = mem.getLong(72L + infos);

            this.uuid = new UUID(mem.getLong(80L + infos), mem.getLong(88L + infos)).toString();
        }
        mem.freeMemory(infos);
    }

    int getDeviceId() { return this.deviceId; }

    @Override
    public CudaContext getDefaultContext() throws CudaException {
        if ((this.context == null) || this.context.isClosed()) {
            this.context = newContext();
        }

        return this.context;
    }

    public CudaContext newContext() throws CudaException {
        return new CudaContext(this);
    }

    // ===== Information only =====

    @Override
    public DeviceType getType() { return this.type; }

    @Override
    public String getName() { return this.name; }

    @Override
    public long getComputeUnit() { return this.computeUnit; }

    @Override
    public long getTotalMemory() { return this.totalMemory; }

    @Override
    public int getMaxNDRange() { return this.maxNdRange; }

    @Override
    public long[] getMaxGlobalSize() { return this.maxNdRangeVal; }

    @Override
    public long[] getMaxLocalSize() { return this.maxNdRangeValLocal; }

    @Override
    public long getMaxLocalThread() { return this.maxThreadNdRange; }

    @Override
    public String getUuid() { return this.uuid; }

    @Override
    public long getFlops() { return this.flops; }

    // ==== Object ====
    @Override
    public String toString() {
        return getName() + "(" + getType().name() + ")";
    }

    @Override
    public int hashCode() {
        return this.deviceId;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.hashCode() == hashCode()) && (obj instanceof CudaDevice);
    }
}

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

import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.backend.Platform;
import io.github.seal139.jSwarm.misc.Common;
import sun.misc.Unsafe;

/**
 * CUDA low level driver. This class cannot be instantiated
 */
public final class Cuda implements Platform {

    private static Cuda obj;

    public static Cuda getInstance() throws Error, CudaException {
        if (obj == null) {
            obj = new Cuda();
        }

        return obj;
    }

    // ===================================

    private final String version;
    private final String platform;

    private CudaDevice[] exec;

    private Cuda() throws Error, CudaException {
        if (!CudaDriver.isLoaded()) {
            throw new Error(CudaDriver.getException());
        }

        this.version  = CudaDriver.cudaGetVersion();
        this.platform = CudaDriver.cudaGetPlatform();

        final Unsafe mem = Common.getMemoryManagement();

        final long intptr = CudaDriver.cudaEnumerateDevices();

        int errorCode = mem.getInt(intptr);
        if (errorCode != 0) {
            // Don't forget to deallocate memory
            mem.freeMemory(intptr);
            throw new CudaException(errorCode);
        }

        final int length = mem.getInt(intptr + 4L);
        this.exec = new CudaDevice[length];

        long lBound = intptr + 8L;
        long uBound = lBound + (length * 4);

        int indexer = -1;
        for (long devicePtr = lBound; devicePtr < uBound; devicePtr += 4) {
            this.exec[++indexer] = new CudaDevice(mem.getInt(devicePtr), indexer);
        }

        // Don't forget to deallocate memory
        mem.freeMemory(intptr);
    }

    @Override
    public String getName() { return this.platform; }

    @Override
    public String getFullName() { return "Nvidia CUDA"; }

    @Override
    public boolean isPrimary() { return true; }

    @Override
    public String getVersion() { return this.version; }

    @Override
    public Executor[] getDevices() { return this.exec.clone(); }
}

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

import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.misc.Common;
import sun.misc.Unsafe;

public class CudaKernel implements Kernel {

    private final long   address;
    private final String name;

    long getAddress() { return this.address; }

    CudaKernel(CudaContext context, CudaModule module, String name) throws CudaException {
        final Unsafe mem = Common.getMemoryManagement();

        long intptr = CudaDriver.cudaGetKernel(module.getAddress(), name);

        int errorCode = (int) mem.getLong(intptr);
        if (errorCode != 0) {
            // Don't forget to deallocate memory
            mem.freeMemory(intptr);
            throw new CudaException(errorCode);
        }

        // Don't forget to deallocate memory
        mem.freeMemory(intptr);

        this.name    = name;
        this.address = mem.getLong(intptr + 8);
    }

    @Override
    public String getName() { return this.name; }

    // ==== Object ====
    @Override
    public String toString() {
        return getName() + "@" + String.valueOf(this.address);
    }

    @Override
    public int hashCode() {
        return (int) this.address;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.hashCode() == hashCode()) //
               && (obj instanceof CudaKernel) //
               && (((CudaKernel) obj).address == this.address);
    }
}

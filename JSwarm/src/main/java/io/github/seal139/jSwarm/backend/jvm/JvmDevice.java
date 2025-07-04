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

package io.github.seal139.jSwarm.backend.jvm;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Context;
import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.runtime.DeviceType;

public class JvmDevice implements Executor {

    private final JvmContext context = new JvmContext(this);

    @Override
    public Context getDefaultContext() throws BackendException { return this.context; }

    @Override
    public DeviceType getType() { return DeviceType.CPU; }

    @Override
    public String getUuid() { return "6e6f4d0e-4b33-4d9e-92e4-fb69b0ef51c6"; }

    @Override
    public String getName() { return "JVM"; }

    @Override
    public long getComputeUnit() { return Runtime.getRuntime().availableProcessors(); }

    @Override
    public long getTotalMemory() { return Runtime.getRuntime().maxMemory(); }

    @Override
    public int getMaxNDRange() { return 3; }

    @Override
    public long[] getMaxGlobalSize() {
        return new long[] {
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };
    }

    @Override
    public long[] getMaxLocalSize() {
        return new long[] {
                1024, 32, 32 };
    }

    @Override
    public long getMaxLocalThread() { return 1024; }

    @Override
    public long getFlops() { return 0; }

}

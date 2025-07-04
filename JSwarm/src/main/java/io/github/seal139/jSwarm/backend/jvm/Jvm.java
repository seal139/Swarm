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

import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.backend.Platform;

public class Jvm implements Platform {

    private static Jvm obj;

    public static Jvm getInstance() {
        if (obj == null) {
            obj = new Jvm();
        }

        return obj;
    }

    // ===================================

    @Override
    public String getName() { return "JVM"; }

    @Override
    public String getFullName() { return "Java Virtual Machine"; }

    @Override
    public boolean isPrimary() { return false; }

    @Override
    public String getVersion() { return "1.0"; }

    private final JvmDevice device = new JvmDevice();

    @Override
    public Executor[] getDevices() {
        return new Executor[] {
                this.device };
    }

}

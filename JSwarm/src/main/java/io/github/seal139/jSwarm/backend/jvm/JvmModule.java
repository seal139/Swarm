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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.backend.Module;
import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import io.github.seal139.jSwarm.runtime.Program;

public class JvmModule implements Module {

    private final Map<String, JvmKernel> kernelMap = new HashMap<>();

    JvmModule(Class<? extends Program> program, Set<String> syncedMethod) throws JvmException {
        final Method[]         methods = program.getDeclaredMethods();
        final Constructor<?>[] constrs = program.getDeclaredConstructors();

        Constructor<?> constructor = null;

        for (Constructor<?> constr : constrs) {
            if (constr.getParameterCount() == 0) {
                constructor = constr;
                break;
            }
        }

        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            if (method.isSynthetic()) {
                continue;
            }

            if (method.isBridge()) {
                continue;
            }

            this.kernelMap.put(method.getName(), new JvmKernel(constructor, method, syncedMethod.contains(method.getName())));
        }
    }

    @Override
    public Deallocator getDeallocator() { return null; }

    @Override
    public boolean isClosed() { return false; }

    @Override
    public void close() throws Exception {
        // NoOp;
    }

    @Override
    public Kernel getKernel(String kernelName) throws BackendException, DeallocatedException {
        return this.kernelMap.get(kernelName);
    }

}

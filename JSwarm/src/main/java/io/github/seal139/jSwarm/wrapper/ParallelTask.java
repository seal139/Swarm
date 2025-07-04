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

package io.github.seal139.jSwarm.wrapper;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Context;
import io.github.seal139.jSwarm.backend.Executor;
import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.backend.Module;
import io.github.seal139.jSwarm.backend.Platform;
import io.github.seal139.jSwarm.backend.cuda.Cuda;
import io.github.seal139.jSwarm.backend.jvm.Jvm;
import io.github.seal139.jSwarm.backend.ocl.Ocl;
import io.github.seal139.jSwarm.datatype.Vector;
import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import io.github.seal139.jSwarm.runtime.NdRange;
import io.github.seal139.jSwarm.runtime.Program;
import io.github.seal139.jSwarm.runtime.SyncDirection;

/**
 * Entry point for GPGPU boilerplate code
 */
public class ParallelTask {

    // =================

    /**
     * Load kernel code from Class
     *
     * @param kernelCode
     * @return
     */
    public static ProgramLoader from(Class<? extends Program> kernelCode) {
        return new ProgramLoader(kernelCode);
    }

    public static class ProgramLoader {

        private final Class<? extends Program> clazz;

        private static final Platform platform;

        static {
            Platform p = Jvm.getInstance();
            try {
                p = Ocl.getInstance();
                p = Cuda.getInstance();
            }
            catch (Throwable e) {
            }

            platform = p;
        }

        ProgramLoader(Class<? extends Program> clazz) {
            this.clazz = clazz;
        }

        public GeneralContext atPlatform(Platform platformTarget) throws BackendException, DeallocatedException {
            if (Common.isDebugMode()) {
                System.out.println("Debug mode detected. Using JVM as platform");
            }

            Platform p = Common.isDebugMode() ? Jvm.getInstance() : platformTarget;

            return new GeneralContext(p, this.clazz);
        }

        public GeneralContext atAnyPlatform() throws BackendException, DeallocatedException {
            return atPlatform(platform);
        }
    }

    public static class GeneralContext implements Closeable {

        private final Platform platform;
        private final Executor device;
        private final Context  ctx;
        private final Module   module;

        GeneralContext(Platform platform, Class<? extends Program> clazz) throws BackendException, DeallocatedException {
            this.platform = platform;
            this.device   = platform.getDevices()[0];
            this.ctx      = this.device.getDefaultContext();

            this.ctx.activate();

            this.module = this.ctx.loadProgram(clazz);
        }

        private Number[] vars = null;

        private final Set<Vector<? extends Number>> vecs = new HashSet<>();

        /**
         * Set data arguments before executing kernel
         *
         * @param vars
         * @return
         * @throws BackendException
         * @throws DeallocatedException
         */
        @SuppressWarnings("unchecked")
        public GeneralContext withArguments(Number... vars) throws BackendException, DeallocatedException {
            if (this.vars != null) {
                for (Number var : this.vars) {

                    if (var instanceof Vector vector) {
                        this.ctx.unhook(vector);
                    }
                }
            }

            this.vars = vars;

            for (Number var : vars) {
                if (var instanceof Vector vector) {
                    this.ctx.hook(vector);
                }
            }

            {
                final Vector<? extends Number>[] vec = new Vector[this.vecs.size()];

                int index = -1;
                for (Vector<? extends Number> vector : this.vecs) {
                    vec[++index] = vector;
                }

                this.ctx.sync(SyncDirection.TO_DEVICE, vec);
            }

            this.ctx.waitOperation();

            return this;
        }

        /**
         * Synchronize memory back to host after processed in device
         *
         * @return {@link ParallelTask.GeneralContext} for continuous execution flow
         *
         * @throws BackendException
         * @throws DeallocatedException
         */
        public GeneralContext fetchData() throws BackendException, DeallocatedException {
            {
                final Vector<? extends Number>[] vec = new Vector[this.vecs.size()];

                int index = -1;
                for (Vector<? extends Number> vector : this.vecs) {
                    vec[++index] = vector;
                }

                this.ctx.sync(SyncDirection.TO_HOST, vec);
            }

            this.ctx.waitOperation();

            return this;
        }

        /**
         * Execute kernel function
         *
         * @param kernel Kernel function name
         * @param range  ND Range used
         * @return {@link ParallelTask.GeneralContext} for continuous execution flow
         *
         * @throws BackendException
         * @throws DeallocatedException
         */
        public GeneralContext execute(String kernel, NdRange range) throws BackendException, DeallocatedException {
            Kernel addKernel = this.module.getKernel(kernel);

            this.ctx.launch(addKernel, range, this.vars);
            return this;
        }

        public Platform getPlatform() { return this.platform; }

        public Executor getDevice() { return this.device; }

        public Context getCtx() { return this.ctx; }

        public Module getModule() { return this.module; }

        @Override
        public void close() throws IOException {
            try {
                if (this.vars != null) {
                    for (Number var : this.vars) {

                        if (var instanceof Vector vector) {
                            this.ctx.unhook(vector);
                        }
                    }
                }

                this.module.close();
            }
            catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

}

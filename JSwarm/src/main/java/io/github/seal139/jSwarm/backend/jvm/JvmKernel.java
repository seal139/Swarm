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

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import io.github.seal139.jSwarm.backend.BackendException;
import io.github.seal139.jSwarm.backend.Kernel;
import io.github.seal139.jSwarm.misc.Common;
import io.github.seal139.jSwarm.runtime.NdRange;
import io.github.seal139.jSwarm.runtime.Program;
import io.github.seal139.jSwarm.runtime.WorkItemController;

public class JvmKernel implements Kernel {

    private final Supplier<Program> ctor;

    private final MethodHandle spreadInvoker;

    private final String  name;
    private final boolean synced;

    final int batch = Runtime.getRuntime().availableProcessors();

    ExecutorService exec = null;

    final MethodType invokedType   = MethodType.methodType(Supplier.class);
    final MethodType samMethodType = MethodType.methodType(Object.class);

    JvmKernel(Constructor<?> ctor, Method method, boolean sync) throws JvmException {

        this.name   = method.getName();
        this.synced = sync;

        MethodHandle hwnd = null;

        Supplier<Program> ivkr = null;

        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();

            ctor.setAccessible(true);
            final MethodHandle cnstor = lookup.unreflectConstructor(ctor);

            method.setAccessible(true);
            final MethodHandle mh = lookup.unreflect(method);

            hwnd = mh.asSpreader(Number[].class, method.getParameterCount()); //

            // Constructor fast-access
            CallSite site = LambdaMetafactory.metafactory(lookup, "get", //
                    this.invokedType, //
                    this.samMethodType, //
                    cnstor, //
                    cnstor.type() //
            );

            ivkr = (Supplier<Program>) site.getTarget().invokeExact();
        }
        catch (Throwable e) {
            throw new JvmException(e);
        }

        this.ctor          = ivkr;
        this.spreadInvoker = hwnd;
    }

    Runnable run(NdRange range, Number... param) throws BackendException {

        // To simulate local thread barrier efficiently, inverse the loop order. Loop
        // through global first, then the local. So we do not need local thread barrier
        // anymore
        // This also can improve performance by loop larger number through smaller
        // thread loop

        int sizeX      = range.getXLocal();
        int sizeY      = range.getYLocal();
        int sizeZ      = range.getZLocal();
        int totalItems = sizeX * sizeY * sizeZ;

        final int itemsPerBatch = this.synced ? 1 : (((totalItems + this.batch) - 1) / this.batch);

        final CountDownLatch latch = new CountDownLatch(this.synced ? totalItems : this.batch);

        final CyclicBarrier synchronizer = new CyclicBarrier(this.synced ? totalItems : 1);

        this.exec = Executors.newFixedThreadPool(this.synced ? totalItems : this.batch);

        for (int start = 0; start < /* endIndex */ totalItems; start += itemsPerBatch) {

            final int _start = start;

            Common.queue(() -> {
                int end = Math.min(totalItems, _start + itemsPerBatch);

                try {
                    this.exec.execute(new WorkItemController(latch, synchronizer, this.ctor, this.spreadInvoker, param, range, _start, end));
                }
                catch (RuntimeException e) {
                    throw new JvmException(e.getCause());
                }
                catch (Throwable e) {
                    throw new JvmException(e);
                }

                return null;
            });
        }

        return (Runnable) () -> {

            try {
                latch.await();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            finally {
                JvmKernel.this.exec.shutdown();
            }
        };

    }

    @Override
    public String getName() { return this.name; }

}

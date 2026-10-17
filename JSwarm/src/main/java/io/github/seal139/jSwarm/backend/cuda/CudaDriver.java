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

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;

import io.github.seal139.jSwarm.misc.Common;

/**
 * Driver native API <br/>
 * <br/>
 *
 * Accessing native method is dangerous and can cause crash or memory leak. This
 * part will is not visible to public and was intended for internal use.
 * However, access through reflection is still possible with extra caution.
 * <br/>
 * <br/>
 * Please bear in mind that this class implementation can be changed with no
 * guarantee of backward compatibility as this class was not intended for public
 * use.
 */
final class CudaDriver {

    private static final boolean      loaded;
    private static final Throwable    exception;
    private static final SymbolLookup nativeLib;

    private static final String PLATFORM = "CUDA";

    static {
        boolean      b   = false;
        Throwable    t   = null;
        SymbolLookup lib = null;

        MethodHandle cuEnumeratedDevice_;
        MethodHandle cuGetDeviceInfo_;
        MethodHandle cuCreateContext_;

        try (final Arena libArena = Arena.ofConfined()) {
            lib = SymbolLookup.libraryLookup(Path.of(Common.getDriverByOs(PLATFORM, "swarmcu")), libArena);

            b = true;

            final Linker linker = Linker.nativeLinker();

            cuEnumeratedDevice_ = linker.downcallHandle(lib.find("cu_enumerate_devices").orElseThrow(), //
                    FunctionDescriptor.of( //
                            MemoryLayout.structLayout(ValueLayout.JAVA_BYTE.withName("uuid"), //

                                    ValueLayout.JAVA_INT.withName("compute_unit"), //
                                    ValueLayout.JAVA_INT.withName("clock_rate"), //
                                    ValueLayout.JAVA_LONG.withName("total_memory"), //

                                    ValueLayout.JAVA_INT.withName("max_global_ndrange_x"), //
                                    ValueLayout.JAVA_INT.withName("max_global_ndrange_y"), //
                                    ValueLayout.JAVA_INT.withName("max_global_ndrange_z"), //

                                    ValueLayout.JAVA_INT.withName("max_local_ndrange_x"), //
                                    ValueLayout.JAVA_INT.withName("max_local_ndrange_y"), //
                                    ValueLayout.JAVA_INT.withName("max_local_ndrange_z"), //

                                    ValueLayout.JAVA_INT.withName("max_thread_perblock") //
                            ) //
                    ));

            cuGetDeviceInfo_ = linker.downcallHandle(lib.find("cu_get_device_info").orElseThrow(), //
                    FunctionDescriptor.of(//
                            MemoryLayout.structLayout(//
                                    ValueLayout.JAVA_BYTE.withName("uuid"), //
                                    ValueLayout.JAVA_BYTE.withName("name"),

                                    ValueLayout.JAVA_INT.withName("compute_unit"), //
                                    ValueLayout.JAVA_INT.withName("clock_rate"), //
                                    ValueLayout.JAVA_LONG.withName("total_memory"), //

                                    ValueLayout.JAVA_INT.withName("max_global_ndrange_x"), //
                                    ValueLayout.JAVA_INT.withName("max_global_ndrange_y"), //
                                    ValueLayout.JAVA_INT.withName("max_global_ndrange_z"), //

                                    ValueLayout.JAVA_INT.withName("max_local_ndrange_x"), //
                                    ValueLayout.JAVA_INT.withName("max_local_ndrange_y"), //
                                    ValueLayout.JAVA_INT.withName("max_local_ndrange_z"), //

                                    ValueLayout.JAVA_INT.withName("max_thread_perblock") //
                            ), //
                            ValueLayout.JAVA_INT //
                    ));

            cuCreateContext_ = linker.downcallHandle(lib.find("cu_create_context").orElseThrow(), //
                    FunctionDescriptor.of(//
                            MemoryLayout.structLayout(//
                                    ValueLayout.ADDRESS.withName("addr"), //
                                    ValueLayout.ADDRESS.withName("queue1_addr"), //
                                    ValueLayout.ADDRESS.withName("queue2_addr"), //
                                    ValueLayout.ADDRESS.withName("queue3_addr"), //
                                    ValueLayout.ADDRESS.withName("queue4_addr") //
                            ), //
                            ValueLayout.JAVA_INT //
                    ));

            final MethodHandle cuDeleteContext = linker.downcallHandle(lib.find("cu_delete_context").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS //
                    ));

            final MethodHandle cuSetContext = linker.downcallHandle(lib.find("cu_set_context").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS //
                    ));

            final MethodHandle cuAddQueue = linker.downcallHandle(lib.find("cu_add_queue").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_INT //
                    ));

            final MethodHandle cuDeleteQueue = linker.downcallHandle(lib.find("cu_delete_queue").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_INT //
                    ));

            final MethodHandle cuCreateProgram = linker.downcallHandle(lib.find("cu_create_program").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS //
                    ));

            final MethodHandle cuDeleteProgram = linker.downcallHandle(lib.find("cu_delete_program").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS //
                    ));

            final MethodHandle cuGetKernel = linker.downcallHandle(lib.find("cu_get_kernel").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS //
                    ));

            final MethodHandle cuLaunchKernel = linker.downcallHandle(lib.find("cu_launch_kernel").orElseThrow(), //
                    FunctionDescriptor.ofVoid(//
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_INT, //
                            ValueLayout.JAVA_INT, //
                            ValueLayout.JAVA_INT, //
                            ValueLayout.JAVA_INT, //
                            ValueLayout.JAVA_INT, //
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_INT //
                    ));

            final MethodHandle cuUploadToDevice = linker.downcallHandle(lib.find("cu_upload_to_device").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_LONG //
                    ));

            final MethodHandle cuDownloadFromDevice = linker.downcallHandle(lib.find("cu_downlad_to_device").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_LONG //
                    ));

            final MethodHandle cuHook = linker.downcallHandle(lib.find("cu_hook").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.ADDRESS, //
                            ValueLayout.JAVA_LONG //
                    ));

            final MethodHandle cuUnhook = linker.downcallHandle(lib.find("cu_unhook").orElseThrow(), //
                    FunctionDescriptor.of(//
                            ValueLayout.JAVA_INT, //
                            ValueLayout.ADDRESS //
                    ));

        }
        catch (Throwable e) {
            t = e;
        }

        loaded    = b;
        nativeLib = lib;
        exception = t;

    }

    static boolean isLoaded() { return loaded; }

    static Throwable getException() { return exception; }

    // ==== Functionality ====
    static native String cudaGetVersion();

    static String cudaGetPlatform() {
        return PLATFORM;
    }

    // Device (n Device)
    static native long cudaEnumerateDevices(); // Ok

    static native long cudaGetDeviceInfo(int deviceIndex); // ok
    static native String cudaGetDeviceName(int deviceIndex);

    // Context (1 device : n Context [1 default])
    static native long cudaCreateContext(int device); // Ok
    static native int cudaDeleteContext(long context); // Ok

    static native int cudaSetContext(long context); // ok

    // Queue (1 Context : n Queue)
    static native long cudaAddQueue(int count); // ok
    static native int cudaDeleteQueue(long[] queue, int count); // ok

    // Module (1 Context : n Module)
    static native long cudaCreateProgram(String source); // ok
    static native int cudaDeleteProgram(long module); // ok

    // Kernel (1 Module : n Kernel)
    static native long cudaGetKernel(long program, String name); // ok

    // ==== Launcher ====
    static native void cudaLaunch(long kernel, long queue, //
                                  int x, int y, int z, //
                                  int lx, int ly, int lz, //
                                  long[] arguments, int count); // Ok

    // ==== buffer memory management ====

    static native long cudaHook(long size); // ok

    static native int cudaSyncDataTo(long queue, long hostMemory, long deviceMemory, long size); // ok
    static native int cudaSyncDataFrom(long queue, long hostMemory, long deviceMemory, long size); // ok

    static native int cudaUnhook(long hookAddress); // ok

    // ==== Fence ====
    static native int cudaWaitAll(); // Native not yet
}

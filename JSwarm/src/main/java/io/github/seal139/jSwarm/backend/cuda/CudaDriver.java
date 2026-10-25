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
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.StructLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    private static final class Struct {
        private final List<Long>   offset;
        private final StructLayout layout;

        Struct(List<Long> offset, StructLayout layout) {
            this.offset = offset;
            this.layout = layout;
        }
    }

    private static final class StructBuilder {
        private final List<MemoryLayout> layouts = new ArrayList<>();

        // Menggunakan Long karena FFM API (seperti byteOffset dan byteSize)
        // mengembalikan tipe long
        private final List<Long> offsets = new ArrayList<>();

        public StructBuilder add(long offset, MemoryLayout layout) {
            // Catat offset saat ini sebelum layout baru ditambahkan
            this.offsets.add(offset);
            this.layouts.add(layout);
            return this;
        }

        public StructBuilder addPadding(long bytes) {
            this.layouts.add(MemoryLayout.paddingLayout(bytes));
            return this;
        }

        public Struct build() {
            return new Struct(this.offsets, MemoryLayout.structLayout(this.layouts.toArray(new MemoryLayout[0])));
        }
    }

    private static final MethodHandle cu_get_device_count;
    private static final MethodHandle cu_get_device_info;

    private static final MethodHandle cu_create_context;
    private static final MethodHandle cu_delete_context;
    private static final MethodHandle cu_delete_contexts;
    private static final MethodHandle cu_set_active_context;

    private static final MethodHandle cu_add_executor;
    private static final MethodHandle cu_delete_executor;
    private static final MethodHandle cu_delete_executors;

    private static final MethodHandle cu_create_program;
    private static final MethodHandle cu_delete_program;
    private static final MethodHandle cu_get_kernel;
    private static final MethodHandle cu_launch;

    private static final MethodHandle cu_allocate_device;        // Deallocate with cu_deallocate_device, accept size
    private static final MethodHandle cu_allocate_device_managed;// Deallocate with cu_deallocate_device, accept size
    private static final MethodHandle cu_allocate_device_mapped; //
    private static final MethodHandle cu_deallocate_device;

    private static final MethodHandle cu_allocate_host;
    private static final MethodHandle cu_allocate_host_mapped;
    private static final MethodHandle cu_deallocate_host;

    private static final String PLATFORM = "CUDA";

    private static final boolean   loaded;
    private static final Throwable exception;

    private static final int driverMajorVer;
    private static final int driverMinorVer;

    private static final Arena        globalArena = Common.getGlobalArena();
    private static final Linker       linker      = Common.getLinker();
    private static final SymbolLookup lib         = SymbolLookup.libraryLookup(Path.of(Common.getDriverByOs(PLATFORM, "libswarmcu")), globalArena);

    private final static Struct cu_devices = new StructBuilder() //
            .add(0, ValueLayout.JAVA_INT.withName("err_code")) // 4
            .add(4, ValueLayout.JAVA_INT.withName("device_count")) // 4
            .build();

    private final static Struct cu_device_info = new StructBuilder() //
            .add(0, ValueLayout.JAVA_INT.withName("err_code")) // 4
            .add(4, ValueLayout.JAVA_INT.withName("device_idx")) // 4

            .add(8, MemoryLayout.sequenceLayout(16, ValueLayout.JAVA_BYTE).withName("uuid")) // 16

            .add(24, ValueLayout.JAVA_INT.withName("major")) // 4
            .add(28, ValueLayout.JAVA_INT.withName("minor")) // 4

            .add(32, MemoryLayout.sequenceLayout(256, ValueLayout.JAVA_BYTE).withName("name")) // 256

            .add(288, ValueLayout.JAVA_LONG.withName("total_memory")) // 8

            .add(296, ValueLayout.JAVA_INT.withName("clock_rate")) // 4
            .add(300, ValueLayout.JAVA_INT.withName("memory_clock_rate")) // 4
            .add(304, ValueLayout.JAVA_INT.withName("memory_bus_width")) // 4
            .add(308, ValueLayout.JAVA_INT.withName("l2_cache_size")) // 4

            .add(312, ValueLayout.JAVA_INT.withName("compute_unit")) // 4
            .add(316, ValueLayout.JAVA_INT.withName("max_thread_per_block")) // 4
            .add(320, ValueLayout.JAVA_INT.withName("warp_size")) // 4
            .add(324, ValueLayout.JAVA_INT.withName("max_shared_memory_per_block")) // 4

            .add(328, ValueLayout.JAVA_INT.withName("concurrent_kernels")) // 4
            .add(332, ValueLayout.JAVA_INT.withName("async_engine_count")) // 4

            .add(336, ValueLayout.JAVA_INT.withName("max_global_ndrange_x")) // 4
            .add(340, ValueLayout.JAVA_INT.withName("max_global_ndrange_y")) // 4
            .add(344, ValueLayout.JAVA_INT.withName("max_global_ndrange_z")) // 4

            .add(348, ValueLayout.JAVA_INT.withName("max_local_ndrange_x")) // 4
            .add(352, ValueLayout.JAVA_INT.withName("max_local_ndrange_y")) // 4
            .add(356, ValueLayout.JAVA_INT.withName("max_local_ndrange_z"))// 4

            .add(360, ValueLayout.JAVA_INT.withName("device"))// 4
            .add(364, ValueLayout.JAVA_BOOLEAN.withName("support_uva"))// 4
            .addPadding(3) //
            .build();

    private final static Struct cu_ctx = new StructBuilder() //
            .add(0, ValueLayout.JAVA_INT.withName("err_code")) // 4
            .addPadding(4) //
            .add(8, ValueLayout.ADDRESS.withName("context")) // 8

            .add(16, ValueLayout.ADDRESS.withName("exec_to_host_mem_cpy"))// 8
            .add(24, ValueLayout.ADDRESS.withName("exec_to_device_mem_cpy")) // 8
            .add(32, ValueLayout.ADDRESS.withName("exec_kernel"))// 8
            .add(40, ValueLayout.ADDRESS.withName("exec_extra")) // 8
            .build();

    private final static Struct cu_ptr = new StructBuilder() //
            .add(0, ValueLayout.JAVA_INT.withName("err_code")) // 4
            .addPadding(4) //
            .add(8, ValueLayout.ADDRESS.withName("pointer")) // 8
            .build();

    private final static Struct cu_compile = new StructBuilder() //
            .add(0, ValueLayout.JAVA_INT.withName("err_code")) // 4
            .add(0, ValueLayout.JAVA_INT.withName("err__compile_code")) // 4
            .add(8, ValueLayout.ADDRESS.withName("pointer")) // 8
            .build();

    private static MethodHandle cu_copy_host_to_device;
    private static MethodHandle cu_copy_device_to_host;

    private static MethodHandle cu_is_executor_busy;
    private static MethodHandle cu_wait_executor;
    private static MethodHandle cu_wait_all;

    public static void main(String... args) throws CudaException {

        cu_device_info dev = getDeviceInfo(0);

        cu_ctx context = createContext(dev.devicePtr);

        setActiveContext(context.context);
        long exec = addExecutor();

        final long devAddress = allocateDeviceMemory(16);
        long       addr1      = allocateHostMemory(16, true);

        final long devOut = allocateDeviceMemory(16);
        long       addr2  = allocateHostMemory(16, false);

        long addr3 = allocateHostMemoryMapped(16, true);
        long addr4 = allocateHostMemoryMapped(16, false);

        long addr4a = allocateDeviceMemoryMapped(addr4);
        long addr3a = allocateDeviceMemoryMapped(addr3);

        long addr5 = allocateDeviceMemoryManaged(16);

        long program = createProgram("""
                extern "C" {
                    __global__
                    void vectorAdd(int n, long long int *x, long long int *y) {
                        int index = blockIdx.x * blockDim.x + threadIdx.x;
                        int stride = blockDim.x * gridDim.x;

                        for (int i = index; i < n; i += stride) {
                            y[i] = x[i] * n;
                        }
                    }
                }
                                    """, dev.major, dev.minor);
        long kernel  = getKernelByName(program, "vectorAdd");

        CudaVector<Long> input  = new CudaVector<>(2, 8, addr1);
        CudaVector<Long> output = new CudaVector<>(2, 8, addr2);

        // input.add(10L);
        // input.set(1, 13L);

        final MemorySegment ptrArray = globalArena.allocate(ValueLayout.JAVA_LONG, 2);
        ptrArray.setAtIndex(ValueLayout.JAVA_LONG, 0, 20L);
        ptrArray.setAtIndex(ValueLayout.JAVA_LONG, 1, 15L);

        copyMemoryHostToDevice(context.exec_to_device_mem_cpy, ptrArray.address(), devAddress, 16);
        waitExecutor(context.exec_to_device_mem_cpy);
        launch(kernel, context.exec_kernel, 1, 1, 1, 1, 1, 2, 2, devAddress, devOut);
        waitExecutor(context.exec_kernel);
        copyMemoryDeviceToHost(context.exec_to_host_mem_cpy, devOut, addr2, 16);
        waitExecutor(context.exec_to_host_mem_cpy);

        System.out.println(output.get(0));
        System.out.println(output.get(1));

        waitExecutor(context.exec_to_device_mem_cpy);
        waitAllExecutor();

        deallocateDeviceMemory(devAddress);

        deallocateHostMemory(addr1);
        deallocateHostMemory(addr2);

        deallocateHostMemory(addr3);
        deallocateHostMemory(addr4);

        deallocateDeviceMemory(addr5);

        deleteProgram(program);

        deleteExecutor(exec);
        deleteExecutors(context.exec_extra, context.exec_kernel, context.exec_to_device_mem_cpy, context.exec_to_host_mem_cpy);

        deleteContext(context.context);

    }

    static {
        Throwable excp = null;
        boolean   load = false;

        int major = 0;
        int minor = 0;

        try (final Arena libArena = Arena.ofConfined()) {
            final Arena        globalArena = Arena.global();
            final Linker       linker      = Linker.nativeLinker();
            final SymbolLookup lib         = SymbolLookup.libraryLookup(Path.of(Common.getDriverByOs(PLATFORM, "libswarmcu")), globalArena);

            final MethodHandle cu_init = linker.downcallHandle(lib.find("cu_init").orElseThrow(), //
                    FunctionDescriptor.of( //
                            MemoryLayout.structLayout(//
                                    ValueLayout.JAVA_INT.withName("err_code"), //
                                    ValueLayout.JAVA_INT.withName("major"), //
                                    ValueLayout.JAVA_INT.withName("minor") //
                            ) //
                    ));

            final MemorySegment resultStruct = (MemorySegment) cu_init.invokeExact((SegmentAllocator) libArena);

            final int errCode = resultStruct.get(ValueLayout.JAVA_INT, 0);

            major = resultStruct.get(ValueLayout.JAVA_INT, 4);
            minor = resultStruct.get(ValueLayout.JAVA_INT, 8);

            if (errCode != 0) {
                throw new CudaException(errCode);
            }

            load = true;
        }
        catch (Throwable e) {
            excp = e;
        }

        loaded    = load;
        exception = excp;

        driverMajorVer = major;
        driverMinorVer = minor;

        cu_get_device_count = linker.downcallHandle(lib.find("cu_get_device_count").orElseThrow(), //
                FunctionDescriptor.of(cu_devices.layout));

        cu_get_device_info = linker.downcallHandle(lib.find("cu_get_device_info").orElseThrow(), //
                FunctionDescriptor.of(cu_device_info.layout, ValueLayout.JAVA_INT));

        cu_create_context = linker.downcallHandle(lib.find("cu_create_context").orElseThrow(), //
                FunctionDescriptor.of(cu_ctx.layout, ValueLayout.JAVA_LONG));

        cu_delete_context = linker.downcallHandle(lib.find("cu_delete_context").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_delete_contexts = linker.downcallHandle(lib.find("cu_delete_contexts").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

        cu_set_active_context = linker.downcallHandle(lib.find("cu_set_active_context").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_add_executor = linker.downcallHandle(lib.find("cu_add_executor").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout));

        cu_delete_executor = linker.downcallHandle(lib.find("cu_delete_executor").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_delete_executors = linker.downcallHandle(lib.find("cu_delete_executors").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

        cu_create_program = linker.downcallHandle(lib.find("cu_create_program").orElseThrow(), //
                FunctionDescriptor.of(cu_compile.layout, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

        cu_delete_program = linker.downcallHandle(lib.find("cu_delete_program").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_get_kernel = linker.downcallHandle(lib.find("cu_get_kernel").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

        cu_launch = linker.downcallHandle(lib.find("cu_launch").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, //
                        ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, //
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, //
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, //
                        ValueLayout.ADDRESS));

        // ========= Memory Management =========
        cu_allocate_device = linker.downcallHandle(lib.find("cu_allocate_device").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout, ValueLayout.JAVA_LONG));

        cu_allocate_device_managed = linker.downcallHandle(lib.find("cu_allocate_device_managed").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout, ValueLayout.JAVA_LONG));

        cu_allocate_device_mapped = linker.downcallHandle(lib.find("cu_allocate_device_mapped").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout, ValueLayout.JAVA_LONG));

        cu_deallocate_device = linker.downcallHandle(lib.find("cu_deallocate_device").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_allocate_host = linker.downcallHandle(lib.find("cu_allocate_host").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout, ValueLayout.JAVA_LONG, ValueLayout.JAVA_BOOLEAN));

        cu_allocate_host_mapped = linker.downcallHandle(lib.find("cu_allocate_host_mapped").orElseThrow(), //
                FunctionDescriptor.of(cu_ptr.layout, ValueLayout.JAVA_LONG, ValueLayout.JAVA_BOOLEAN));

        cu_deallocate_host = linker.downcallHandle(lib.find("cu_deallocate_host").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        // ========= Memory Transfer =========
        cu_copy_host_to_device = linker.downcallHandle(lib.find("cu_copy_host_to_device").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG,
                        ValueLayout.JAVA_LONG));

        cu_copy_device_to_host = linker.downcallHandle(lib.find("cu_copy_device_to_host").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG,
                        ValueLayout.JAVA_LONG));

        // ======== Async Controller
        cu_is_executor_busy = linker.downcallHandle(lib.find("cu_is_executor_busy").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_wait_executor = linker.downcallHandle(lib.find("cu_wait_executor").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));

        cu_wait_all = linker.downcallHandle(lib.find("cu_wait_all").orElseThrow(), //
                FunctionDescriptor.of(ValueLayout.JAVA_INT));
    }

    static int getDeviceCount() throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_get_device_count.invokeExact((SegmentAllocator) libArena);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_devices.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_INT, cu_devices.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static cu_device_info getDeviceInfo(int index) throws CudaException {
        try (final Arena arena = Arena.ofConfined()) {
            final MemorySegment info = (MemorySegment) cu_get_device_info.invokeExact((SegmentAllocator) arena, index);

            final int err_code = info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return new cu_device_info(//
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(1)), //
                    info.asSlice(cu_device_info.offset.get(2), 16).toArray(ValueLayout.JAVA_BYTE), //

                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(3)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(4)), //
                    info.asSlice(cu_device_info.offset.get(5), 256).getString(0), //

                    info.get(ValueLayout.JAVA_LONG, cu_device_info.offset.get(6)), //
                    // Specs:
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(7)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(8)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(9)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(10)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(11)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(12)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(13)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(14)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(15)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(16)), //

                    // NDRange:
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(17)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(18)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(19)), //

                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(20)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(21)), //
                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(22)), //

                    info.get(ValueLayout.JAVA_INT, cu_device_info.offset.get(23)), //
                    info.get(ValueLayout.JAVA_BOOLEAN, cu_device_info.offset.get(24))//
            );
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static cu_ctx createContext(long devicePointer) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_create_context.invokeExact((SegmentAllocator) libArena, devicePointer);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ctx.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return new cu_ctx( //
                    result.get(ValueLayout.JAVA_LONG, cu_ctx.offset.get(1)), //
                    result.get(ValueLayout.JAVA_LONG, cu_ctx.offset.get(2)), //
                    result.get(ValueLayout.JAVA_LONG, cu_ctx.offset.get(3)), //
                    result.get(ValueLayout.JAVA_LONG, cu_ctx.offset.get(4)), //
                    result.get(ValueLayout.JAVA_LONG, cu_ctx.offset.get(5)) //
            );
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void setActiveContext(long contextPointer) throws CudaException {
        try {
            final int err_code = (int) cu_set_active_context.invokeExact(contextPointer);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deleteContext(long contextPointer) throws CudaException {
        try {
            final int err_code = (int) cu_delete_context.invokeExact(contextPointer);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deleteContexts(long... contextPointers) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment ptrArray = libArena.allocate(ValueLayout.ADDRESS, contextPointers.length);

            for (int i = 0; i < contextPointers.length; i++) {
                ptrArray.setAtIndex(ValueLayout.ADDRESS, i, MemorySegment.ofAddress(contextPointers[i]));
            }

            final int err_code = (int) cu_delete_contexts.invokeExact(ptrArray, contextPointers.length);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long addExecutor() throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_add_executor.invokeExact((SegmentAllocator) libArena);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deleteExecutor(long executorPointer) throws CudaException {
        try {
            final int err_code = (int) cu_delete_executor.invokeExact(executorPointer);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deleteExecutors(long... executorPointers) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment ptrArray = libArena.allocate(ValueLayout.ADDRESS, executorPointers.length);

            for (int i = 0; i < executorPointers.length; i++) {
                ptrArray.setAtIndex(ValueLayout.ADDRESS, i, MemorySegment.ofAddress(executorPointers[i]));
            }

            final int err_code = (int) cu_delete_executors.invokeExact(ptrArray, executorPointers.length);
            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long createProgram(String source, int major, int minor) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {

            final MemorySegment cSource = libArena.allocateFrom(source);
            final MemorySegment result  = (MemorySegment) cu_create_program.invokeExact((SegmentAllocator) libArena, cSource, major, minor);

            final int compile_err = result.get(ValueLayout.JAVA_INT, cu_compile.offset.get(1));
            if (compile_err != 0) {
                throw new CudaException("Compilation error");
            }

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_compile.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_compile.offset.get(2));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deleteProgram(long programPointer) throws CudaException {
        try {
            final int err_code = (int) cu_delete_program.invokeExact(programPointer);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long getKernelByName(long programPointer, String name) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {

            final MemorySegment kernelName = libArena.allocateFrom(name);
            final MemorySegment result     = (MemorySegment) cu_get_kernel.invokeExact((SegmentAllocator) libArena, programPointer, kernelName);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void launch(long kernel, long exec_kernel, //
                       int gx, int gy, int gz, //
                       int lx, int ly, int lz, //
                       Number... arguments)
            throws CudaException {

        try (final Arena libArena = Arena.ofConfined()) {

            final MemorySegment ptrArray = libArena.allocate(ValueLayout.ADDRESS, arguments.length);

            for (int i = 0; i < arguments.length; i++) {
                final Number   num           = arguments[i];
                final Class<?> componentType = num.getClass();

                // int8
                if ((byte.class.equals(componentType)) || (Byte.class.equals(componentType)) || //
                    (componentType == byte.class) || (componentType == Byte.class)) {

                    final MemorySegment subArray = libArena.allocate(ValueLayout.JAVA_BYTE, 1);

                    subArray.setAtIndex(ValueLayout.JAVA_BYTE, 0, num.byteValue());
                    ptrArray.setAtIndex(ValueLayout.ADDRESS, i, subArray);
                }

                // int16
                else if ((short.class.equals(componentType)) || (Short.class.equals(componentType)) || //
                         (componentType == short.class) || (componentType == Short.class)) {

                    final MemorySegment subArray = libArena.allocate(ValueLayout.JAVA_SHORT, 1);

                    subArray.setAtIndex(ValueLayout.JAVA_SHORT, 0, num.shortValue());
                    ptrArray.setAtIndex(ValueLayout.ADDRESS, i, subArray);
                }

                // int32
                else if ((int.class.equals(componentType)) || (Integer.class.equals(componentType)) || //
                         (componentType == int.class) || (componentType == Integer.class)) {

                    final MemorySegment subArray = libArena.allocate(ValueLayout.JAVA_INT, 1);

                    subArray.setAtIndex(ValueLayout.JAVA_INT, 0, num.intValue());
                    ptrArray.setAtIndex(ValueLayout.ADDRESS, i, subArray);
                }

                // int64
                else if ((long.class.equals(componentType)) || (Long.class.equals(componentType)) || //
                         (componentType == long.class) || (componentType == Long.class)) {

                    final MemorySegment subArray = libArena.allocate(ValueLayout.JAVA_LONG, 1);

                    subArray.setAtIndex(ValueLayout.JAVA_LONG, 0, num.longValue());
                    ptrArray.setAtIndex(ValueLayout.ADDRESS, i, subArray);
                }

                // fp32
                else if ((float.class.equals(componentType)) || (Float.class.equals(componentType)) || //
                         (componentType == float.class) || (componentType == Float.class)) {

                    final MemorySegment subArray = libArena.allocate(ValueLayout.JAVA_FLOAT, 1);

                    subArray.setAtIndex(ValueLayout.JAVA_FLOAT, 0, num.floatValue());
                    ptrArray.setAtIndex(ValueLayout.ADDRESS, i, subArray);
                }

                // fp64
                else if ((double.class.equals(componentType)) || (Double.class.equals(componentType)) || //
                         (componentType == double.class) || (componentType == Double.class)) {

                    final MemorySegment subArray = libArena.allocate(ValueLayout.JAVA_DOUBLE, 1);

                    subArray.setAtIndex(ValueLayout.JAVA_DOUBLE, 0, num.doubleValue());
                    ptrArray.setAtIndex(ValueLayout.ADDRESS, i, subArray);
                }

            }

            final int err_code = (int) cu_launch.invokeExact( //
                    kernel, exec_kernel, //
                    gx, gy, gz, //
                    lx, ly, lz, //
                    ptrArray //
            );

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(e);
        }
    }

    static long allocateDeviceMemory(long size) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_allocate_device.invokeExact((SegmentAllocator) libArena, size);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long allocateDeviceMemoryMapped(long hostPointer) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_allocate_device_mapped.invokeExact((SegmentAllocator) libArena, hostPointer);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long allocateDeviceMemoryManaged(long size) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_allocate_device_managed.invokeExact((SegmentAllocator) libArena, size);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deallocateDeviceMemory(long memoryPointer) throws CudaException {
        try {
            final int err_code = (int) cu_deallocate_device.invokeExact(memoryPointer);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long allocateHostMemory(long size, boolean write_optimized) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_allocate_host.invokeExact((SegmentAllocator) libArena, size, write_optimized);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static long allocateHostMemoryMapped(long size, boolean write_optimized) throws CudaException {
        try (final Arena libArena = Arena.ofConfined()) {
            final MemorySegment result = (MemorySegment) cu_allocate_host_mapped.invokeExact((SegmentAllocator) libArena, size, write_optimized);

            final int err_code = result.get(ValueLayout.JAVA_INT, cu_ptr.offset.get(0));
            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return result.get(ValueLayout.JAVA_LONG, cu_ptr.offset.get(1));
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void deallocateHostMemory(long memoryPointer) throws CudaException {
        try {
            final int err_code = (int) cu_deallocate_host.invokeExact(memoryPointer);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void copyMemoryHostToDevice(long executor, long hostMemory, long deviceMemory, long size) throws CudaException {
        try {
            final int err_code = (int) cu_copy_host_to_device.invokeExact(executor, hostMemory, deviceMemory, size);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static void copyMemoryDeviceToHost(long executor, long deviceMemory, long hostMemory, long size) throws CudaException {
        try {
            final int err_code = (int) cu_copy_device_to_host.invokeExact(executor, deviceMemory, hostMemory, size);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static boolean isExecutorBusy(long executor) throws CudaException {
        try {
            final int err_code = (int) cu_is_executor_busy.invokeExact(executor);

            // CUDA_ERROR_NOT_READY
            if (err_code == 600) {
                return false;
            }

            if (err_code != 0) {
                throw new CudaException(err_code);
            }

            return true;
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    /**
     * Wait for specific executor within the active context to finish it's task
     * (blocker)
     *
     * @param executor
     * @throws CudaException
     */
    static void waitExecutor(long executor) throws CudaException {
        try {
            final int err_code = (int) cu_wait_executor.invokeExact(executor);

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    /**
     * Wait for all executor within the active context to finish it's task (blocker)
     *
     * @throws CudaException
     */
    static void waitAllExecutor() throws CudaException {
        try {
            final int err_code = (int) cu_wait_all.invokeExact();

            if (err_code != 0) {
                throw new CudaException(err_code);
            }
        }
        catch (Throwable e) {
            throw new CudaException(exception);
        }
    }

    static boolean isLoaded() { return loaded; }

    static Throwable getException() { return exception; }

    static record cu_ctx(//
            long context, //

            long exec_to_host_mem_cpy, //
            long exec_to_device_mem_cpy, //
            long exec_kernel, //
            long exec_extra//
    ) {
    }

    static record cu_device_info(//
            int device_idx, //
            byte[] uuid, //

            int major, //
            int minor, //

            String name, //

            long totalMemory, //
            int clockRate, //
            int memory_clock_rate, // 4
            int memory_bus_width, // 4
            int l2_cache_size, // 4

            int compute_unit, // 4
            int max_thread_per_block, // 4
            int warp_size, // 4
            int max_shared_memory_per_block, // 4

            int concurrent_kernels, // 4
            int async_engine_count, //

            int max_global_ndrange_x, //
            int max_global_ndrange_y, //
            int max_global_ndrange_z, //

            int max_local_ndrange_x, //
            int max_local_ndrange_y, //
            int max_local_ndrange_z, //

            int devicePtr, //
            boolean uvaSupported //
    ) {
    }

    public static class CudaVector<T extends Number> implements List<T> {
        final long address;

        final MemorySegment segment;

        final int sizeT;
        final int byteSize;

        int currentPointer = -1;

        public CudaVector(int sizeT, int byteSize, long address) {
            this.sizeT    = sizeT;
            this.byteSize = byteSize;

            this.address = address;
            this.segment = MemorySegment.ofAddress(this.address).reinterpret(sizeT * this.byteSize, globalArena, null);
        }

        @Override
        public boolean add(T arg0) {
            this.segment.set(ValueLayout.JAVA_LONG, ++this.currentPointer, arg0.longValue());

            return true;
        }

        @Override
        public T set(int arg0, T arg1) {
            final Long r = this.segment.get(ValueLayout.JAVA_LONG, arg0 * 8);

            this.segment.set(ValueLayout.JAVA_LONG, arg0 * 8, arg1.longValue());
            return (T) r;
        }

        @Override
        public T get(int index) {
            final Long r = this.segment.get(ValueLayout.JAVA_LONG, index * 8);
            return (T) r;
        }

        @Override
        public void add(int arg0, T arg1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEmpty() { return false; }

        @Override
        public Iterator<T> iterator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int lastIndexOf(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListIterator<T> listIterator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return this.sizeT;
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <Y> Y[] toArray(Y[] arg0) {
            throw new UnsupportedOperationException();
        }

    }

}

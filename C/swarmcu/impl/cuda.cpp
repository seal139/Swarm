#include <cstddef>
#include <cstdio>
#include <vector>

#include "../header/struct_header.h"

#ifdef __cplusplus
extern "C" {
#endif

using namespace std;

/**
 * Initialize CUDA and return driver version
 */
cu_driver_info cu_init() {
  cu_driver_info ret;
  ret.err_code = cuInit(0);

  if (ret.err_code != CUDA_SUCCESS) {
    return ret;
  }

  int version;
  ret.err_code = cuDriverGetVersion(&version);

  if (ret.err_code != CUDA_SUCCESS) {
    return ret;
  }

  ret.major = version / 1000;
  ret.minor = (version % 1000) / 10;

  return ret;
}

/**
 * Get device count
 */
cu_devices cu_get_device_count() {
  cu_devices ret;
  ret.err_code = cuDeviceGetCount(&ret.device_count);

  return ret;
}

/**
 * Get device info based on device index
 */
cu_device_info cu_get_device_info(int device_idx) {
  cu_device_info info;

  // Select device

  info.err_code = cuDeviceGet(&info.device, device_idx);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  CUdevice dev = info.device;

  // Name
  info.device_idx = device_idx;
  info.err_code = cuDeviceGetName(info.name, sizeof(info.name), dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // UUID
  info.err_code = cuDeviceGetUuid(&info.uuid, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Major - Minor
  info.err_code = cuDeviceGetAttribute(
      &info.major, CU_DEVICE_ATTRIBUTE_COMPUTE_CAPABILITY_MAJOR, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.minor, CU_DEVICE_ATTRIBUTE_COMPUTE_CAPABILITY_MINOR, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Total global memory
  info.err_code = cuDeviceTotalMem(&info.total_memory, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Clock and memory specs
  info.err_code = cuDeviceGetAttribute(&info.clock_rate,
                                       CU_DEVICE_ATTRIBUTE_CLOCK_RATE, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.memory_clock_rate, CU_DEVICE_ATTRIBUTE_MEMORY_CLOCK_RATE, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.memory_bus_width, CU_DEVICE_ATTRIBUTE_GLOBAL_MEMORY_BUS_WIDTH, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(&info.l2_cache_size,
                                       CU_DEVICE_ATTRIBUTE_L2_CACHE_SIZE, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Multiprocessor & Warp execution specs
  info.err_code = cuDeviceGetAttribute(
      &info.compute_unit, CU_DEVICE_ATTRIBUTE_MULTIPROCESSOR_COUNT, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code =
      cuDeviceGetAttribute(&info.max_thread_per_block,
                           CU_DEVICE_ATTRIBUTE_MAX_THREADS_PER_BLOCK, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code =
      cuDeviceGetAttribute(&info.warp_size, CU_DEVICE_ATTRIBUTE_WARP_SIZE, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.max_shared_memory_per_block,
      CU_DEVICE_ATTRIBUTE_MAX_SHARED_MEMORY_PER_BLOCK, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Concurrency & Async features
  info.err_code = cuDeviceGetAttribute(
      &info.concurrent_kernels, CU_DEVICE_ATTRIBUTE_CONCURRENT_KERNELS, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.async_engine_count, CU_DEVICE_ATTRIBUTE_ASYNC_ENGINE_COUNT, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Grid (Global NDRange) limits
  info.err_code = cuDeviceGetAttribute(&info.max_global_ndrange_x,
                                       CU_DEVICE_ATTRIBUTE_MAX_GRID_DIM_X, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(&info.max_global_ndrange_y,
                                       CU_DEVICE_ATTRIBUTE_MAX_GRID_DIM_Y, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(&info.max_global_ndrange_z,
                                       CU_DEVICE_ATTRIBUTE_MAX_GRID_DIM_Z, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Block (Local NDRange) limits
  info.err_code = cuDeviceGetAttribute(
      &info.max_local_ndrange_x, CU_DEVICE_ATTRIBUTE_MAX_BLOCK_DIM_X, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.max_local_ndrange_y, CU_DEVICE_ATTRIBUTE_MAX_BLOCK_DIM_Y, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  info.err_code = cuDeviceGetAttribute(
      &info.max_local_ndrange_z, CU_DEVICE_ATTRIBUTE_MAX_BLOCK_DIM_Z, dev);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  int uva_supported = 0;
  info.err_code = cuDeviceGetAttribute(
      &uva_supported, CU_DEVICE_ATTRIBUTE_UNIFIED_ADDRESSING, dev);
  info.support_uva = (bool)uva_supported;

  return info;
}

/**
 * Create context along with 4 default executors.
 */
cu_ctx cu_create_context(CUdevice device) {
  cu_ctx ret;

  // Create context
  ret.err_code = cuCtxCreate(&ret.context, nullptr, CU_CTX_SCHED_YIELD, device);
  if (ret.err_code != CUDA_SUCCESS) {
    return ret;
  }

  // Create executor for D2H memcpy
  ret.err_code =
      cuStreamCreate(&ret.exec_to_host_mem_cpy, CU_STREAM_NON_BLOCKING);
  if (ret.err_code != CUDA_SUCCESS) {

    // Deallocate on error
    cuCtxDestroy(ret.context);

    return ret;
  }

  // Create executor for H2D memcpy
  ret.err_code =
      cuStreamCreate(&ret.exec_to_device_mem_cpy, CU_STREAM_NON_BLOCKING);
  if (ret.err_code != CUDA_SUCCESS) {

    // Deallocate on error
    cuStreamDestroy(ret.exec_to_host_mem_cpy);
    cuCtxDestroy(ret.context);

    return ret;
  }

  // Create executor for kernel
  ret.err_code = cuStreamCreate(&ret.exec_kernel, CU_STREAM_NON_BLOCKING);
  if (ret.err_code != CUDA_SUCCESS) {

    // Deallocate on error
    cuStreamDestroy(ret.exec_to_host_mem_cpy);
    cuStreamDestroy(ret.exec_to_device_mem_cpy);
    cuCtxDestroy(ret.context);

    return ret;
  }

  // Create extra executor
  ret.err_code = cuStreamCreate(&ret.exec_extra, CU_STREAM_NON_BLOCKING);
  if (ret.err_code != CUDA_SUCCESS) {

    // Deallocate on error
    cuStreamDestroy(ret.exec_to_host_mem_cpy);
    cuStreamDestroy(ret.exec_to_device_mem_cpy);
    cuStreamDestroy(ret.exec_kernel);
    cuCtxDestroy(ret.context);

    return ret;
  }

  ret.err_code = CUDA_SUCCESS;
  return ret;
}

/**
 * Delete context
 */
CUresult cu_delete_context(CUcontext context) { return cuCtxDestroy(context); }

/**
 * Delete multiple context
 */
CUresult cu_delete_contexts(CUcontext *contexts, int count) {
  CUresult err = CUDA_SUCCESS;

  for (int i = 0; i < count; i++) {
    CUresult lerr = cuCtxDestroy(contexts[i]);

    if (lerr != CUDA_SUCCESS) {
      err = lerr;
    }
  }

  return err;
}

/**
 * Set active contex at current thread. Context must be set before begin
 * operation
 */
CUresult cu_set_active_context(CUcontext context) {
  return cuCtxSetCurrent(context);
}

/**
 * Create extra executor at current context
 */
cu_executor cu_add_executor() {
  cu_executor ret;

  ret.err_code = cuStreamCreate(&ret.stream, CU_STREAM_NON_BLOCKING);
  return ret;
}

CUresult cu_delete_executor(CUstream stream) { return cuStreamDestroy(stream); }

CUresult cu_delete_executors(CUstream *streams, int count) {
  CUresult err = CUDA_SUCCESS;

  for (int i = 0; i < count; i++) {
    CUresult lerr = cuStreamDestroy(streams[i]);

    if (lerr != CUDA_SUCCESS) {
      err = lerr;
    }
  }

  return err;
}

cu_program cu_create_program(const char *source, int dev_major, int dev_minor) {
  cu_program ret;

  nvrtcProgram prog;

  ret.compiler_err_code =
      nvrtcCreateProgram(&prog, source, "kernel.cu", 0, nullptr, nullptr);
  if (ret.compiler_err_code != NVRTC_SUCCESS) {
    return ret;
  }

  char arch_opt[32];
  snprintf(arch_opt, sizeof(arch_opt), "-arch=compute_%d%d", dev_major,
           dev_minor);
  const char *opts[] = {arch_opt};

  ret.compiler_err_code = nvrtcCompileProgram(prog, 1, opts);
  if (ret.compiler_err_code != NVRTC_SUCCESS) {
    // Check for compilation error here
    // size_t logSize;
    // nvrtcGetProgramLogSize(prog, &logSize);

    // vector<char> log(logSize);
    // nvrtcGetProgramLog(prog, log.data());

    // ret.compiler_err_code = NVRTC_ERROR_COMPILATION;

    nvrtcDestroyProgram(&prog);

    return ret;
  }

  // ======

  size_t ptxSize;
  ret.compiler_err_code = nvrtcGetPTXSize(prog, &ptxSize);
  if (ret.compiler_err_code != NVRTC_SUCCESS) {
    nvrtcDestroyProgram(&prog);

    return ret;
  }

  vector<char> ptx(ptxSize);
  ret.compiler_err_code = nvrtcGetPTX(prog, ptx.data());

  nvrtcDestroyProgram(&prog);
  if (ret.compiler_err_code != NVRTC_SUCCESS) {
    return ret;
  }

  ret.err_code = cuModuleLoadData(&ret.module, ptx.data());

  return ret;
}

CUresult cu_delete_program(CUmodule program) { return cuModuleUnload(program); }

cu_kernel cu_get_kernel(CUmodule program, const char *name) {
  cu_kernel ret;

  ret.err_code = cuModuleGetFunction(&ret.function, program, name);
  return ret;
}

CUresult cu_launch(CUfunction kernel, CUstream exec_kernel,        //
                   int gx, int gy, int gz, int lx, int ly, int lz, //
                   void **args) {

  return cuLaunchKernel(kernel,     //
                        gx, gy, gz, // Grid dimensions
                        lx, ly, lz, // Block dimensions
                        0,          // Shared Memory - Currently unavailable
                        exec_kernel, args, 0);
}

// Zero Copy Memory

// High speed memory
// Host Memory <--> Device Memory
// cu_allocate_host <h2d -- d2h> cu_allocate

CUresult cu_deallocate_device(CUdeviceptr pointer) {
  return cuMemFree(pointer);
}

cu_device_mem_ptr cu_allocate_device(size_t size) {
  cu_device_mem_ptr ret;

  ret.err_code = cuMemAlloc(&ret.pointer, size);

  return ret;
}

cu_device_mem_ptr cu_allocate_device_managed(size_t size) {

  cu_device_mem_ptr ret;

  ret.err_code = cuMemAllocManaged(&ret.pointer, size, CU_MEM_ATTACH_GLOBAL);

  return ret;
}

cu_host_mem_ptr cu_allocate_host(size_t size, bool write_optimized) {
  cu_host_mem_ptr ret;

  int flag = CU_MEMHOSTALLOC_PORTABLE;
  if (write_optimized) {
    flag |= CU_MEMHOSTALLOC_WRITECOMBINED;
  }

  ret.err_code = cuMemHostAlloc(&ret.pointer, size, flag);

  return ret;
}

cu_host_mem_ptr cu_allocate_host_mapped(size_t size, bool write_optimized) {
  cu_host_mem_ptr ret;

  int flag = CU_MEMHOSTALLOC_PORTABLE | CU_MEMHOSTALLOC_DEVICEMAP;
  if (write_optimized) {
    flag |= CU_MEMHOSTALLOC_WRITECOMBINED;
  }

  ret.err_code = cuMemHostAlloc(&ret.pointer, size, flag);

  if (ret.err_code != CUDA_SUCCESS) {
    return ret;
  }

  return ret;
}

cu_device_mem_ptr cu_allocate_device_mapped(void *host_pointer) {
  cu_device_mem_ptr ret;

  ret.err_code = cuMemHostGetDevicePointer(&ret.pointer, host_pointer, 0);

  return ret;
}

CUresult cu_deallocate_host(void *host_ptr) { return cuMemFreeHost(host_ptr); }

CUresult cu_copy_host_to_device(CUstream exec_to_device_mem_cpy,
                                const void *host_ptr, CUdeviceptr device_ptr,
                                size_t size) {

  return cuMemcpyHtoDAsync(device_ptr, host_ptr, size, exec_to_device_mem_cpy);
}

CUresult cu_copy_device_to_host(CUstream exec_to_host_mem_cpy,
                                CUdeviceptr device_ptr, void *host_ptr,
                                size_t size) {

  return cuMemcpyDtoHAsync(host_ptr, device_ptr, size, exec_to_host_mem_cpy);
}

// ======================================

CUresult cu_is_executor_busy(CUstream stream) { return cuStreamQuery(stream); }

CUresult cu_wait_executor(CUstream stream) {
  return cuStreamSynchronize(stream);
}

CUresult cu_wait_all() { return cuCtxSynchronize(); }

#ifdef __cplusplus
}
#endif
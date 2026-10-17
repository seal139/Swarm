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
cu_devices get_device_count() {
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
  CUdevice dev;
  info.err_code = cuDeviceGet(&dev, device_idx);
  if (info.err_code != CUDA_SUCCESS) {
    return info;
  }

  // Name
  info.device_idx = device_idx;
  cuDeviceGetName(info.name, sizeof(info.name), dev);

  // UUID
  cuDeviceGetUuid(&info.uuid, dev);

  // Major - Minor
  cuDeviceGetAttribute(&info.major,
                       CU_DEVICE_ATTRIBUTE_COMPUTE_CAPABILITY_MAJOR, dev);
  cuDeviceGetAttribute(&info.minor,
                       CU_DEVICE_ATTRIBUTE_COMPUTE_CAPABILITY_MINOR, dev);

  // Total global memory
  cuDeviceTotalMem(&info.total_memory, dev);

  // Clock and memory specs
  cuDeviceGetAttribute(&info.clock_rate, CU_DEVICE_ATTRIBUTE_CLOCK_RATE, dev);
  cuDeviceGetAttribute(&info.memory_clock_rate,
                       CU_DEVICE_ATTRIBUTE_MEMORY_CLOCK_RATE, dev);
  cuDeviceGetAttribute(&info.memory_bus_width,
                       CU_DEVICE_ATTRIBUTE_GLOBAL_MEMORY_BUS_WIDTH, dev);
  cuDeviceGetAttribute(&info.l2_cache_size, CU_DEVICE_ATTRIBUTE_L2_CACHE_SIZE,
                       dev);

  // Multiprocessor & Warp execution specs
  cuDeviceGetAttribute(&info.compute_unit,
                       CU_DEVICE_ATTRIBUTE_MULTIPROCESSOR_COUNT, dev);
  cuDeviceGetAttribute(&info.max_thread_per_block,
                       CU_DEVICE_ATTRIBUTE_MAX_THREADS_PER_BLOCK, dev);
  cuDeviceGetAttribute(&info.warp_size, CU_DEVICE_ATTRIBUTE_WARP_SIZE, dev);
  cuDeviceGetAttribute(&info.max_shared_memory_per_block,
                       CU_DEVICE_ATTRIBUTE_MAX_SHARED_MEMORY_PER_BLOCK, dev);

  // Concurrency & Async features
  cuDeviceGetAttribute(&info.concurrent_kernels,
                       CU_DEVICE_ATTRIBUTE_CONCURRENT_KERNELS, dev);
  cuDeviceGetAttribute(&info.async_engine_count,
                       CU_DEVICE_ATTRIBUTE_ASYNC_ENGINE_COUNT, dev);

  // Grid (Global NDRange) limits
  cuDeviceGetAttribute(&info.max_global_ndrange_x,
                       CU_DEVICE_ATTRIBUTE_MAX_GRID_DIM_X, dev);
  cuDeviceGetAttribute(&info.max_global_ndrange_y,
                       CU_DEVICE_ATTRIBUTE_MAX_GRID_DIM_Y, dev);
  cuDeviceGetAttribute(&info.max_global_ndrange_z,
                       CU_DEVICE_ATTRIBUTE_MAX_GRID_DIM_Z, dev);

  // Block (Local NDRange) limits
  cuDeviceGetAttribute(&info.max_local_ndrange_x,
                       CU_DEVICE_ATTRIBUTE_MAX_BLOCK_DIM_X, dev);
  cuDeviceGetAttribute(&info.max_local_ndrange_y,
                       CU_DEVICE_ATTRIBUTE_MAX_BLOCK_DIM_Y, dev);
  cuDeviceGetAttribute(&info.max_local_ndrange_z,
                       CU_DEVICE_ATTRIBUTE_MAX_BLOCK_DIM_Z, dev);

  info.err_code = CUDA_SUCCESS;
  return info;
}

/**
 * Create context along with 4 default executors.
 */
cu_ctx create_context(CUdevice device) {
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
CUresult delete_context(CUcontext context) { return cuCtxDestroy(context); }

/**
 * Delete multiple context
 */
CUresult delete_contexts(CUcontext *contexts, int count) {
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
CUresult set_active_context(CUcontext context) {
  return cuCtxSetCurrent(context);
}

/**
 * Create extra executor at current context
 */
cu_executor add_executor() {
  cu_executor ret;

  ret.err_code = cuStreamCreate(&ret.stream, CU_STREAM_NON_BLOCKING);
  return ret;
}

CUresult delete_executor(CUstream stream) { return cuStreamDestroy(stream); }

CUresult delete_executors(CUstream *streams, int count) {
  CUresult err = CUDA_SUCCESS;

  for (int i = 0; i < count; i++) {
    CUresult lerr = cuStreamDestroy(streams[i]);

    if (lerr != CUDA_SUCCESS) {
      err = lerr;
    }
  }

  return err;
}

cu_program create_program(const char *source, int dev_major, int dev_minor) {
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

CUresult delete_program(CUmodule program) { return cuModuleUnload(program); }

cu_kernel get_kernel(CUmodule program, const char *name) {
  cu_kernel ret;

  ret.err_code = cuModuleGetFunction(&ret.function, program, name);
  return ret;
}

CUresult launch(CUfunction kernel, CUstream exec_kernel,        //
                int gx, int gy, int gz, int lx, int ly, int lz, //
                void **args) {

  return cuLaunchKernel(kernel,     //
                        gx, gy, gz, // Grid dimensions
                        lx, ly, lz, // Block dimensions
                        0,          // Shared Memory - Currently unavailable
                        exec_kernel, args, 0);
}

cu_mem_ptr allocate(size_t size) {
  cu_mem_ptr ret;

  ret.err_code = cuMemAlloc(&ret.pointer, size);

  return ret;
}

CUresult deallocate(CUdeviceptr pointer) { return cuMemFree(pointer); }

CUresult copy_host_to_device(CUstream exec_to_device_mem_cpy,
                             const void *host_ptr, CUdeviceptr device_ptr,
                             size_t size) {

  return cuMemcpyHtoDAsync(device_ptr, host_ptr, size, exec_to_device_mem_cpy);
}

CUresult copy_device_to_host(CUstream exec_to_host_mem_cpy,
                             CUdeviceptr device_ptr, void *host_ptr,
                             size_t size) {

  return cuMemcpyDtoHAsync(host_ptr, device_ptr, size, exec_to_host_mem_cpy);
}

CUresult is_executor_busy(CUstream stream) { return cuStreamQuery(stream); }

CUresult wait_executor(CUstream stream) { return cuStreamSynchronize(stream); }

CUresult wait_all() { return cuCtxSynchronize(); }

CUresult allocate_host(void **host_ptr, size_t size) {
  return cuMemAllocHost(host_ptr, size);
}
CUresult deallocate_host(void *host_ptr) { return cuMemFreeHost(host_ptr); }

#ifdef __cplusplus
}
#endif
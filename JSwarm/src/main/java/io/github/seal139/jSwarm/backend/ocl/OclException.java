package io.github.seal139.jSwarm.backend.ocl;

import io.github.seal139.jSwarm.backend.BackendException;

/**
 * CUDA-related exception
 */
public final class OclException extends BackendException {

    private static final long serialVersionUID = -3271554093353725232L;

    OclException(Throwable e) {
        super(e.getMessage());

        this.setStackTrace(e.getStackTrace());
        this.initCause(e.getCause());
    }

    OclException(String e) {
        super(e);
    }

    OclException(int i) {
        super(constructMessage(i));
    }

    private static String constructMessage(int i) {
        String hex = "0x" + String.format("%016X", i);

        // see cuda.h files
        return switch (i) {
        case 1 -> hex + " CUDA_ERROR_INVALID_VALUE : "//
                + "This indicates that one or more of the parameters passed to the API call is not within an acceptable range of values."; // //

        case 2 -> hex + " CUDA_ERROR_OUT_OF_MEMORY : " //
                + "The API call failed because it was unable to allocate enough memory or other resources to perform the requested operation."; //

        case 3 -> hex + " CUDA_ERROR_NOT_INITIALIZED : //"
                + "This indicates that the CUDA driver has not been initialized with ::cuInit() or that initialization has failed.";

        case 4 -> hex + " CUDA_ERROR_DEINITIALIZED : "//
                + "This indicates that the CUDA driver is in the process of shutting down."; //

        case 5 -> hex + " CUDA_ERROR_PROFILER_DISABLED : "//
                + "This indicates profiler is not initialized for this run. This can happen when the application is running with external profiling tools like visual profiler."; //

        case 6 -> hex + " CUDA_ERROR_PROFILER_NOT_INITIALIZED : "//
                + "deprecated This error return is deprecated as of CUDA 5.0. It is no longer an error to attempt to enable/disable the profiling via ::cuProfilerStart or ::cuProfilerStop without initialization."; //

        case 7 -> hex + " CUDA_ERROR_PROFILER_ALREADY_STARTED : "//
                + "deprecated This error return is deprecated as of CUDA 5.0. It is no longer an error to call cuProfilerStart() when profiling is already enabled."; //

        case 8 -> hex + " CUDA_ERROR_PROFILER_ALREADY_STOPPED : "//
                + "deprecated This error return is deprecated as of CUDA 5.0. It is no longer an error to call cuProfilerStop() when profiling is already disabled."; //

        case 34 -> hex + " CUDA_ERROR_STUB_LIBRARY : " //
                + "This indicates that the CUDA driver that the application has loaded is a stub library. Applications that run with the stub rather than a real driver loaded will result in CUDA API returning this error."; //

        case 46 -> hex + " CUDA_ERROR_DEVICE_UNAVAILABLE : "//
                + "This indicates that requested CUDA device is unavailable at the current time. Devices are often unavailable due to use of ::CU_COMPUTEMODE_EXCLUSIVE_PROCESS or ::CU_COMPUTEMODE_PROHIBITED."; //

        case 100 -> hex + " CUDA_ERROR_NO_DEVICE : "//
                + "This indicates that no CUDA-capable devices were detected by the installed CUDA driver."; //

        case 101 -> hex + " CUDA_ERROR_INVALID_DEVICE : "//
                + "This indicates that the device ordinal supplied by the user does not correspond to a valid CUDA device or that the action requested is invalid for the specified device."; //

        case 102 -> hex + " CUDA_ERROR_DEVICE_NOT_LICENSED : "//
                + "This error indicates that the Grid license is not applied"; //

        case 200 -> hex + " CUDA_ERROR_INVALID_IMAGE : "//
                + "This indicates that the device kernel image is invalid. This can also indicate an invalid CUDA module."; //

        case 201 -> hex + " CUDA_ERROR_INVALID_CONTEXT : "//
                + "This most frequently indicates that there is no context bound to the current thread. This can also be returned if the context passed to an API call is not a valid handle (such as a context that has had ::cuCtxDestroy() invoked on it). This can also be returned if a user mixes different API versions (i.e. 3010 context with 3020 API calls). See ::cuCtxGetApiVersion() for more details. This can also be returned if the green context passed to an API call was not converted to a ::CUcontext using ::cuCtxFromGreenCtx API."; //

        case 202 -> hex + " CUDA_ERROR_CONTEXT_ALREADY_CURRENT : "//
                + "This indicated that the context being supplied as a parameter to the API call was already the active context. \\deprecated This error return is deprecated as of CUDA 3.2. It is no longer an error to attempt to push the active context via ::cuCtxPushCurrent()."; //

        case 205 -> hex + " CUDA_ERROR_MAP_FAILED : "//
                + "This indicates that a map or register operation has failed."; //

        case 206 -> hex + " CUDA_ERROR_UNMAP_FAILED : "//
                + "This indicates that an unmap or unregister operation has failed."; //

        case 207 -> hex + " CUDA_ERROR_ARRAY_IS_MAPPED : "//
                + "This indicates that the specified array is currently mapped and thus cannot be destroyed."; //

        case 208 -> hex + " CUDA_ERROR_ALREADY_MAPPED : "//
                + "This indicates that the resource is already mapped."; //

        case 209 -> hex + " CUDA_ERROR_NO_BINARY_FOR_GPU : "//
                + "This indicates that there is no kernel image available that is suitable for the device. This can occur when a user specifies code generation options for a particular CUDA source file that do not include the corresponding device configuration."; //

        case 210 -> hex + " CUDA_ERROR_ALREADY_ACQUIRED : "//
                + "This indicates that a resource has already been acquired."; //

        case 211 -> hex + " CUDA_ERROR_NOT_MAPPED : "//
                + "This indicates that a resource is not mapped."; //

        case 212 -> hex + " CUDA_ERROR_NOT_MAPPED_AS_ARRAY : "//
                + "This indicates that a mapped resource is not available for access as an array."; //

        case 213 -> hex + " CUDA_ERROR_NOT_MAPPED_AS_POINTER : "//
                + "This indicates that a mapped resource is not available for access as a pointer."; //

        case 214 -> hex + " CUDA_ERROR_ECC_UNCORRECTABLE : "//
                + "This indicates that an uncorrectable ECC error was detected during execution."; //

        case 215 -> hex + " CUDA_ERROR_UNSUPPORTED_LIMIT : "//
                + "This indicates that the ::CUlimit passed to the API call is not supported by the active device."; //

        case 216 -> hex + " CUDA_ERROR_CONTEXT_ALREADY_IN_USE : "//
                + "This indicates that the ::CUcontext passed to the API call can only be bound to a single CPU thread at a time but is already bound to a CPU thread."; //

        case 217 -> hex + " CUDA_ERROR_PEER_ACCESS_UNSUPPORTED : "//
                + "This indicates that peer access is not supported across the given devices."; //

        case 218 -> hex + " CUDA_ERROR_INVALID_PTX : "//
                + "This indicates that a PTX JIT compilation failed."; //

        case 219 -> hex + " CUDA_ERROR_INVALID_GRAPHICS_CONTEXT : "//
                + "This indicates an error with OpenGL or DirectX context."; //

        case 220 -> hex + " CUDA_ERROR_NVLINK_UNCORRECTABLE : "//
                + "This indicates that an uncorrectable NVLink error was detected during the execution."; //

        case 221 -> hex + " CUDA_ERROR_JIT_COMPILER_NOT_FOUND : "//
                + "This indicates that the PTX JIT compiler library was not found."; //

        case 222 -> hex + " CUDA_ERROR_UNSUPPORTED_PTX_VERSION : "//
                + "This indicates that the provided PTX was compiled with an unsupported toolchain."; //

        case 223 -> hex + " CUDA_ERROR_JIT_COMPILATION_DISABLED : "//
                + "This indicates that the PTX JIT compilation was disabled."; //

        case 224 -> hex + " CUDA_ERROR_UNSUPPORTED_EXEC_AFFINITY : "//
                + "This indicates that the ::CUexecAffinityType passed to the API call is not supported by the active device."; //

        case 225 -> hex + " CUDA_ERROR_UNSUPPORTED_DEVSIDE_SYNC : "//
                + "This indicates that the code to be compiled by the PTX JIT contains unsupported call to cudaDeviceSynchronize."; //

        case 300 -> hex + " CUDA_ERROR_INVALID_SOURCE : "//
                + "This indicates that the device kernel source is invalid. This includes compilation/linker errors encountered in device code or user error."; //

        case 301 -> hex + " CUDA_ERROR_FILE_NOT_FOUND : "//
                + "This indicates that the file specified was not found."; //

        case 302 -> hex + " CUDA_ERROR_SHARED_OBJECT_SYMBOL_NOT_FOUND : "//
                + "This indicates that a link to a shared object failed to resolve."; //

        case 303 -> hex + " CUDA_ERROR_SHARED_OBJECT_INIT_FAILED : "//
                + "This indicates that initialization of a shared object failed."; //

        case 304 -> hex + " CUDA_ERROR_OPERATING_SYSTEM : "//
                + "This indicates that an OS call failed."; //

        case 400 -> hex + " CUDA_ERROR_INVALID_HANDLE : "//
                + "This indicates that a resource handle passed to the API call was not valid. Resource handles are opaque types like ::CUstream and ::CUevent."; //

        case 401 -> hex + " CUDA_ERROR_ILLEGAL_STATE : "//
                + "This indicates that a resource required by the API call is not in a valid state to perform the requested operation.";

        case 402 -> hex + " CUDA_ERROR_LOSSY_QUERY : "//
                + "This indicates an attempt was made to introspect an object in a way that would discard semantically important information. This is either due to the object using funtionality newer than the API version used to introspect it or omission of optional return arguments.";

        case 500 -> hex + " CUDA_ERROR_NOT_FOUND : "//
                + "This indicates that a named symbol was not found. Examples of symbols are global/constant variable names, driver function names, texture names, and surface names.";

        case 600 -> hex + " CUDA_ERROR_NOT_READY : "//
                + "This indicates that asynchronous operations issued previously have not completed yet. This result is not actually an error, but must be indicated differently than ::CUDA_SUCCESS (which indicates completion). Calls that may return this value include ::cuEventQuery() and ::cuStreamQuery().";

        case 700 -> hex + " CUDA_ERROR_ILLEGAL_ADDRESS : "//
                + "While executing a kernel, the device encountered a load or store instruction on an invalid memory address. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 701 -> hex + " CUDA_ERROR_LAUNCH_OUT_OF_RESOURCES : "//
                + "This indicates that a launch did not occur because it did not have appropriate resources. This error usually indicates that the user has attempted to pass too many arguments to the device kernel, or the kernel launch specifies too many threads for the kernel's register count. Passing arguments of the wrong size (i.e. a 64-bit pointer when a 32-bit int is expected) is equivalent to passing too many arguments and can also result in this error.";

        case 702 -> hex + " CUDA_ERROR_LAUNCH_TIMEOUT : "//
                + "This indicates that the device kernel took too long to execute. This can only occur if timeouts are enabled - see the device attribute ::CU_DEVICE_ATTRIBUTE_KERNEL_EXEC_TIMEOUT for more information. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 703 -> hex + " CUDA_ERROR_LAUNCH_INCOMPATIBLE_TEXTURING : "//
                + "This error indicates a kernel launch that uses an incompatible texturing mode.";

        case 704 -> hex + " CUDA_ERROR_PEER_ACCESS_ALREADY_ENABLED : "//
                + "This error indicates that a call to ::cuCtxEnablePeerAccess() is trying to re-enable peer access to a context which has already had peer access to it enabled.";

        case 705 -> hex + " CUDA_ERROR_PEER_ACCESS_NOT_ENABLED : "//
                + "This error indicates that ::cuCtxDisablePeerAccess() is trying to disable peer access which has not been enabled yet via ::cuCtxEnablePeerAccess().";

        case 708 -> hex + " CUDA_ERROR_PRIMARY_CONTEXT_ACTIVE : "
                + "This error indicates that the primary context for the specified device has already been initialized.";

        case 709 -> hex + " CUDA_ERROR_CONTEXT_IS_DESTROYED : "
                + "This error indicates that the context current to the calling thread has been destroyed using ::cuCtxDestroy, or is a primary context which has not yet been initialized.";

        case 710 -> hex + " CUDA_ERROR_ASSERT : "//
                + "A device-side assert triggered during kernel execution. The context cannot be used anymore, and must be destroyed. All existing device memory allocations from this context are invalid and must be reconstructed if the program is to continue using CUDA.";

        case 711 -> hex + " CUDA_ERROR_TOO_MANY_PEERS : "//
                + "This error indicates that the hardware resources required to enable peer access have been exhausted for one or more of the devices passed to ::cuCtxEnablePeerAccess().";

        case 712 -> hex + " CUDA_ERROR_HOST_MEMORY_ALREADY_REGISTERED : "//
                + "This error indicates that the memory range passed to ::cuMemHostRegister() has already been registered.";

        case 713 -> hex + " CUDA_ERROR_HOST_MEMORY_NOT_REGISTERED : "//
                + "This error indicates that the pointer passed to ::cuMemHostUnregister() does not correspond to any currently registered memory region.";

        case 714 -> hex + " CUDA_ERROR_HARDWARE_STACK_ERROR : "//
                + "While executing a kernel, the device encountered a stack error. This can be due to stack corruption or exceeding the stack size limit. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 715 -> hex + " CUDA_ERROR_ILLEGAL_INSTRUCTION : "//
                + "While executing a kernel, the device encountered an illegal instruction. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 716 -> hex + " CUDA_ERROR_MISALIGNED_ADDRESS : "//
                + "While executing a kernel, the device encountered a load or store instruction on a memory address which is not aligned. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 717 -> hex + " CUDA_ERROR_INVALID_ADDRESS_SPACE : "//
                + "While executing a kernel, the device encountered an instruction which can only operate on memory locations in certain address spaces (global, shared, or local), but was supplied a memory address not belonging to an allowed address space. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 718 -> hex + " CUDA_ERROR_INVALID_PC :  "//
                + "While executing a kernel, the device program counter wrapped its address space. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 719 -> hex + " CUDA_ERROR_LAUNCH_FAILED : "//
                + "An exception occurred on the device while executing a kernel. Common cause include dereferencing an invalid device pointer and accessing out of bound shared memory. Less common cases can be system specific - more information about these cases can be found in the system specific user guide. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 720 -> hex + " CUDA_ERROR_COOPERATIVE_LAUNCH_TOO_LARGE : "//
                + "This error indicates that the number of blocks launched per grid for a kernel that was launched via either ::cuLaunchCooperativeKernel or ::cuLaunchCooperativeKernelMultiDevice exceeds the maximum number of blocks as allowed by ::cuOccupancyMaxActiveBlocksPerMultiprocessor or ::cuOccupancyMaxActiveBlocksPerMultiprocessorWithFlags times the number of multiprocessors as specified by the device attribute ::CU_DEVICE_ATTRIBUTE_MULTIPROCESSOR_COUNT.";

        case 800 -> hex + " CUDA_ERROR_NOT_PERMITTED : " //
                + "This error indicates that the attempted operation is not permitted.";

        case 801 -> hex + " CUDA_ERROR_NOT_SUPPORTED : "//
                + "This error indicates that the attempted operation is not supported on the current system or device.";

        case 802 -> hex + " CUDA_ERROR_SYSTEM_NOT_READY : "//
                + "This error indicates that the system is not yet ready to start any CUDA work. To continue using CUDA, verify the system configuration is in a valid state and all required driver daemons are actively running. More information about this error can be found in the system specific user guide.";

        case 803 -> hex + " CUDA_ERROR_SYSTEM_DRIVER_MISMATCH : "//
                + "This error indicates that there is a mismatch between the versions of the display driver and the CUDA driver. Refer to the compatibility documentation for supported versions.";

        case 804 -> hex + " CUDA_ERROR_COMPAT_NOT_SUPPORTED_ON_DEVICE : "//
                + "This error indicates that the system was upgraded to run with forward compatibility but the visible hardware detected by CUDA does not support this configuration. Refer to the compatibility documentation for the supported hardware matrix or ensure that only supported hardware is visible during initialization via the CUDA_VISIBLE_DEVICES environment variable.";

        case 805 -> hex + " CUDA_ERROR_MPS_CONNECTION_FAILED : "//
                + "This error indicates that the MPS client failed to connect to the MPS control daemon or the MPS server.";

        case 806 -> hex + " CUDA_ERROR_MPS_RPC_FAILURE : "//
                + "This error indicates that the remote procedural call between the MPS server and the MPS client failed.";

        case 807 -> hex + " CUDA_ERROR_MPS_SERVER_NOT_READY : " //
                + "This error indicates that the MPS server is not ready to accept new MPS client requests. This error can be returned when the MPS server is in the process of recovering from a fatal failure.";

        case 808 -> hex + " CUDA_ERROR_MPS_MAX_CLIENTS_REACHED : "//
                + "This error indicates that the hardware resources required to create MPS client have been exhausted.";

        case 809 -> hex + " CUDA_ERROR_MPS_MAX_CONNECTIONS_REACHED : "//
                + "This error indicates the the hardware resources required to support device connections have been exhausted.";

        case 810 -> hex + " CUDA_ERROR_MPS_CLIENT_TERMINATED : "//
                + "This error indicates that the MPS client has been terminated by the server. To continue using CUDA, the process must be terminated and relaunched.";

        case 811 -> hex + " CUDA_ERROR_CDP_NOT_SUPPORTED : "//
                + "This error indicates that the module is using CUDA Dynamic Parallelism, but the current configuration, like MPS, does not support it.";

        case 812 -> hex + " CUDA_ERROR_CDP_VERSION_MISMATCH : "//
                + "This error indicates that a module contains an unsupported interaction between different versions of CUDA Dynamic Parallelism.";

        case 900 -> hex + " CUDA_ERROR_STREAM_CAPTURE_UNSUPPORTED : "//
                + "This error indicates that the operation is not permitted when the stream is capturing.";

        case 901 -> hex + " CUDA_ERROR_STREAM_CAPTURE_INVALIDATED : "//
                + "This error indicates that the current capture sequence on the stream has been invalidated due to a previous error.";

        case 902 -> hex + " CUDA_ERROR_STREAM_CAPTURE_MERGE : "//
                + "This error indicates that the operation would have resulted in a merge of two independent capture sequences.";

        case 903 -> hex + " CUDA_ERROR_STREAM_CAPTURE_UNMATCHED : " //
                + "This error indicates that the capture was not initiated in this stream.";

        case 904 -> hex + " CUDA_ERROR_STREAM_CAPTURE_UNJOINED : "//
                + "This error indicates that the capture sequence contains a fork that was not joined to the primary stream.";

        case 905 -> hex + " CUDA_ERROR_STREAM_CAPTURE_ISOLATION : "//
                + "This error indicates that a dependency would have been created which crosses the capture sequence boundary. Only implicit in-stream ordering dependencies are allowed to cross the boundary.";

        case 906 -> hex + " CUDA_ERROR_STREAM_CAPTURE_IMPLICIT : "//
                + "This error indicates a disallowed implicit dependency on a current capture sequence from cudaStreamLegacy.";

        case 907 -> hex + " CUDA_ERROR_CAPTURED_EVENT : "//
                + "This error indicates that the operation is not permitted on an event which was last recorded in a capturing stream.";

        case 908 -> hex + " CUDA_ERROR_STREAM_CAPTURE_WRONG_THREAD : "//
                + "A stream capture sequence not initiated with the ::CU_STREAM_CAPTURE_MODE_RELAXED argument to ::cuStreamBeginCapture was passed to ::cuStreamEndCapture in a different thread.";

        case 909 -> hex + " CUDA_ERROR_TIMEOUT : " + "This error indicates that the timeout specified for the wait operation has lapsed.";

        case 910 -> hex + " CUDA_ERROR_GRAPH_EXEC_UPDATE_FAILURE : "//
                + "This error indicates that the graph update was not performed because it included changes which violated constraints specific to instantiated graph update.";

        case 911 -> hex + " CUDA_ERROR_EXTERNAL_DEVICE : "//
                + "This indicates that an async error has occurred in a device outside of CUDA If CUDA was waiting for an external device's signal before consuming shared data, the external device signaled an error indicating that the data is not valid for consumption. This leaves the process in an inconsistent state and any further CUDA work will return the same error. To continue using CUDA, the process must be terminated and relaunched.";

        case 912 -> hex + " CUDA_ERROR_INVALID_CLUSTER_SIZE : " + "Indicates a kernel launch error due to cluster misconfiguration.";

        case 913 -> hex + " CUDA_ERROR_FUNCTION_NOT_LOADED : "//
                + "Indiciates a function handle is not loaded when calling an API that requires a loaded function.";

        case 914 -> hex + " CUDA_ERROR_INVALID_RESOURCE_TYPE : "//
                + "This error indicates one or more resources passed in are not valid resource types for the operation.";

        case 915 -> hex + " CUDA_ERROR_INVALID_RESOURCE_CONFIGURATION : "//
                + "This error indicates one or more resources are insufficient or non-applicable for the operation.";

        default -> hex + " CUDA_ERROR_UNKNOWN : "//
                + "This indicates that an unknown internal error has occurred.";
        };
    }

}

# CMakeList.txt : Top-level CMake project file, do global configuration
# and include sub-projects here.

cmake_minimum_required (VERSION 3.18)

project ("swarmcu" LANGUAGES CXX CUDA)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_CXX_STANDARD 17)

find_package(CUDAToolkit REQUIRED) # Cuda

file(GLOB_RECURSE HDR "header/*" "header/*.hpp" "/opt/cuda/include/")  # Source code - Include DIR
file(GLOB_RECURSE SRC "impl/*" "impl/*.cu")    # Source code - Implementation DIR

add_library (swarmcu SHARED
    ${HDR}
    ${SRC}
)

target_include_directories(swarmcu PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}/header
)

target_link_libraries(swarmcu PRIVATE
    CUDA::toolkit
    CUDA::cuda_driver
    CUDA::nvrtc
)

target_compile_options(swarmcu PRIVATE
    $<$<CXX_COMPILER_ID:Clang>:-O3 -m64>
    $<$<CXX_COMPILER_ID:GNU>:-O3 -m64>
    $<$<CXX_COMPILER_ID:MSVC>:/O3 -m64>
)
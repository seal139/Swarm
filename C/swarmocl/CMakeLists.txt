# CMakeList.txt : Top-level CMake project file, do global configuration
# and include sub-projects here.
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

cmake_minimum_required (VERSION 3.18)

project ("swarmocl" LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)

find_package(OpenCL REQUIRED) # OpenCL
find_package(Java REQUIRED)   # Java
find_package(JNI REQUIRED)    # JNI

include_directories(${JNI_INCLUDE_DIRS}) # JNI - Include DIR

file(GLOB_RECURSE HDR "header/*")  # Source code - Include DIR
file(GLOB_RECURSE SRC "impl/*")    # Source code - Implementation DIR

add_library (swarmocl SHARED
    ${HDR}
    ${SRC}
)

target_link_libraries(swarmocl OpenCL::OpenCL)

target_compile_options(swarmocl PRIVATE
    $<$<CXX_COMPILER_ID:MSVC>:/O2>
    $<$<CXX_COMPILER_ID:GNU>:-O2>
    $<$<CXX_COMPILER_ID:Clang>:-O2>
)
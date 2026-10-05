cmake_minimum_required(VERSION 3.22.1)
project("NativeImageProcessor")

# 16 KB page size support recommendation for modern NDK / Android
set(CMAKE_ANDROID_ENABLE_16K_PAGE_SUPPORT ON)

add_library(NativeImageProcessor SHARED
        NativeImageProcessor.cpp)

target_link_libraries(NativeImageProcessor
        android
        log)

#!/usr/bin/env bash

# 关于 ： 语法 是在 PermissionDispatcher 学的，这个语法还是可以继续研究一下的
# 在 `lib_system_bar/` 目录下执行命令
./gradlew :lib_system_bar:clean
./gradlew :lib_system_bar:build
./gradlew :lib_system_bar:publish

#pwd
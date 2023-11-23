#!/bin/bash

# 脚本名: compile_and_link.sh

# 复制 testfile.txt 到 testfile.c
cp testfile.txt testfile.c

# 使用 clang 将 testfile.c 编译为 LLVM IR
clang -emit-llvm -S testfile.c -o testfile.ll

# 使用 llvm-link 链接 testfile.ll 和 lib.ll
llvm-link testfile.ll lib.ll -S -o testfile.ll

# 删除 testfile.c
rm testfile.c


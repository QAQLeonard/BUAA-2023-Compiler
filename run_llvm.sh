#!/bin/bash

if [ -f out.ll ]; then
    rm out.ll
fi

# 使用 llvm-link 链接 llvm-frontend.ir.ll 和 lib.ll
llvm-link llvm_ir.ll lib.ll -S -o out.ll

# 使用 lli 解释运行 out.ll
lli out.ll


declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
declare void @putstr(i8*)

define dso_local i32 @main(){
;<label>:0:
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 49)
    call void @putch(i32 56)
    call void @putch(i32 50)
    call void @putch(i32 54)
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 10)
    %1 = alloca i32
    store i32 5, i32* %1
    %2 = alloca i32
    store i32 1, i32* %2
    br label %3

;<label>:3:
    br label %4

;<label>:4:
    br label %5

;<label>:5:
    br label %6

;<label>:6:
    %7 = load i32, i32* %2
    %8 = icmp sle i32 %7, 5
    br i1 %8, label %9, label %10

;<label>:9:
    br label %10

;<label>:10:
    br label %11

;<label>:11:
    br label %12

;<label>:12:
    br label %13

;<label>:13:
    br label %17

;<label>:14:
    %15 = load i32, i32* %2
    %16 = add i32 %15, 1
    store i32 %16, i32* %2
    br label %11

;<label>:17:
    br label %18

;<label>:18:
    br label %19

;<label>:19:
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 49)
    call void @putch(i32 56)
    call void @putch(i32 50)
    call void @putch(i32 54)
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 10)
    ret i32 0
}

declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
declare void @putstr(i8*)

define dso_local i32 @main(){
0:
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 51)
    call void @putch(i32 55)
    call void @putch(i32 49)
    call void @putch(i32 50)
    call void @putch(i32 57)
    call void @putch(i32 53)
    call void @putch(i32 10)
    %1 = alloca i32
    store i32 0, i32* %1
    %2 = add i32 0, 1

    %3 = load i32, i32* %1
    %4 = icmp sgt i32 %3, 0

    br i1 %4, label %5, label %6


5:
    store i32 1, i32* %1
    br label %7


6:
    store i32 0, i32* %1
    br label %7


7:
    %8 = alloca i32
    store i32 1, i32* %8
    %9 = alloca i32
    store i32 2, i32* %9
    %10 = load i32, i32* %8
    %11 = icmp sge i32 %10, 0

    br i1 %11, label %12, label %13


12:
    store i32 1, i32* %8
    br label %13


13:
    %14 = load i32, i32* %8
    %15 = icmp sge i32 %14, 0

    br i1 %15, label %16, label %17


16:
    br label %17


17:
    %18 = load i32, i32* %8
    %19 = icmp eq i32 %18, 0

    br i1 %19, label %20, label %21


20:
    br label %22


21:
    br label %22


22:
    %23 = load i32, i32* %9
    %24 = icmp sle i32 %23, 0

    br i1 %24, label %25, label %26


25:
    store i32 2, i32* %9
    br label %26


26:
    %27 = load i32, i32* %8
    %28 = load i32, i32* %9
    %29 = icmp eq i32 %27, %28

    br i1 %29, label %30, label %31


30:
    store i32 1, i32* %1
    br label %31


31:
    %32 = load i32, i32* %8
    %33 = load i32, i32* %9
    %34 = icmp ne i32 %32, %33

    br i1 %34, label %35, label %36


35:
    store i32 0, i32* %1
    br label %36


36:
    %37 = load i32, i32* %8
    %38 = load i32, i32* %9
    %39 = icmp ne i32 %37, %38

    br i1 %39, label %45, label %41


40:
    store i32 1, i32* %1
    br label %41


41:
    %42 = load i32, i32* %8
    %43 = load i32, i32* %9
    %44 = icmp ne i32 %42, %43

    br i1 %44, label %48, label %58


45:
    %46 = load i32, i32* %8
    %47 = icmp sgt i32 %46, 0

    br i1 %47, label %40, label %41


48:
    store i32 2, i32* %1
    br label %49


49:
    %50 = load i32, i32* %8
    %51 = add i32 %50, 1

    %52 = mul i32 3, 4

    %53 = sdiv i32 %52, 3

    %54 = srem i32 %53, 2

    %55 = sub i32 %51, %54

    store i32 %55, i32* %8
    %56 = add i32 1, 1

    store i32 %56, i32* %8
    %57 = sub i32 0, 1

    store i32 %57, i32* %8
    ret i32 0

58:
    %59 = load i32, i32* %8
    %60 = icmp sgt i32 %59, 0

    br i1 %60, label %48, label %49

}

@c_var1_g = constant i32 1
@c_var2_g = constant i32 2
@c_var3_g = constant i32 3
@D_g = constant i32 4
@E_g = constant i32 5
@F_g = constant i32 6
@c_arr1_g = constant [5 x i32] 
@c_arr2_g = constant [2 x [2 x i32]] 
@var1_g = global i32 0
@temp = global i32 0
@arr1_g = global [5 x i32] 
@arr2_g = global [2 x [2 x i32]] 

declare i32 @getint()
declare void @putint(i32)
declare void @putch(i32)
declare void @putstr(i8*)

define dso_local void @func2(){
0:
    ret void
}

define dso_local i32 @func1(i32, i32*, [2 x i32]*){
3:
    %4 = alloca i32
    store i32 %0, i32* %4
    %5 = alloca i32*
    store i32* %1, i32** %5
    %6 = alloca [2 x i32]*
    store [2 x i32]* %2, [2 x i32]** %6
    %7 = load i32, i32* %4
    %8 = mul i32 %7, 2

    %9 = load i32*, i32** %5
    %10 = getelementptr i32, i32* %9, i32 0
    %11 = load i32, i32* %10
    %12 = srem i32 %11, 2

    %13 = add i32 %8, %12

    %14 = load [2 x i32]*, [2 x i32]** %6
    %15 = getelementptr [2 x i32], [2 x i32]* %14, i32 0, i32 0
    %16 = load i32, i32* %15
    %17 = srem i32 %16, 3

    %18 = add i32 %13, %17

    %19 = alloca i32
    store i32 %18, i32* %19
    %20 = load i32, i32* %19
    %21 = load i32, i32* %4
    %22 = add i32 %21, 2

    %23 = mul i32 %20, %22

    store i32 %23, i32* @var1_g
    call void @func2()
    %24 = load i32, i32* @var1_g
    %25 = icmp slt i32 %24, 30

    br i1 %25, label %26, label %28


26:
    %27 = load i32, i32* @var1_g
    call void @putint(i32 %27)
    call void @putch(i32 32)
    call void @putch(i32 60)
    call void @putch(i32 32)
    call void @putch(i32 51)
    call void @putch(i32 48)
    call void @putch(i32 46)
    call void @putch(i32 10)
    br label %30


28:
    %29 = load i32, i32* @var1_g
    call void @putint(i32 %29)
    call void @putch(i32 32)
    call void @putch(i32 62)
    call void @putch(i32 61)
    call void @putch(i32 32)
    call void @putch(i32 51)
    call void @putch(i32 48)
    call void @putch(i32 46)
    call void @putch(i32 10)
    br label %30


30:
    ret i32 0
}

define dso_local void @vfunc1(i32, i32*, [2 x i32]*){
3:
    %4 = alloca i32
    store i32 %0, i32* %4
    %5 = alloca i32*
    store i32* %1, i32** %5
    %6 = alloca [2 x i32]*
    store [2 x i32]* %2, [2 x i32]** %6
    %7 = load i32, i32* %4
    %8 = mul i32 %7, 2

    %9 = load i32*, i32** %5
    %10 = getelementptr i32, i32* %9, i32 0
    %11 = load i32, i32* %10
    %12 = srem i32 %11, 2

    %13 = add i32 %8, %12

    %14 = load [2 x i32]*, [2 x i32]** %6
    %15 = getelementptr [2 x i32], [2 x i32]* %14, i32 0, i32 0
    %16 = load i32, i32* %15
    %17 = srem i32 %16, 3

    %18 = add i32 %13, %17

    %19 = alloca i32
    store i32 %18, i32* %19
    %20 = load i32, i32* %19
    %21 = load i32, i32* %4
    %22 = add i32 %21, 2

    %23 = mul i32 %20, %22

    store i32 %23, i32* @var1_g
    call void @func2()
    %24 = load i32, i32* @var1_g
    %25 = icmp slt i32 %24, 30

    br i1 %25, label %26, label %28


26:
    %27 = load i32, i32* @var1_g
    call void @putint(i32 %27)
    call void @putch(i32 32)
    call void @putch(i32 60)
    call void @putch(i32 32)
    call void @putch(i32 51)
    call void @putch(i32 48)
    call void @putch(i32 46)
    call void @putch(i32 10)
    br label %30


28:
    %29 = load i32, i32* @var1_g
    call void @putint(i32 %29)
    call void @putch(i32 32)
    call void @putch(i32 62)
    call void @putch(i32 61)
    call void @putch(i32 32)
    call void @putch(i32 51)
    call void @putch(i32 48)
    call void @putch(i32 46)
    call void @putch(i32 10)
    br label %30


30:
    ret void
}

define dso_local void @func3(i32){
1:
    %2 = alloca i32
    store i32 %0, i32* %2
    call void @putch(i32 68)
    call void @putch(i32 78)
    call void @putch(i32 77)
    call void @putch(i32 68)
    call void @putch(i32 10)
    ret void
}

define dso_local void @no_meaning_fun(){
0:
    %1 = alloca i32
    store i32 114514, i32* %1
    %2 = alloca i32
    store i32 6, i32* %2
    %3 = alloca i32
    store i32 6, i32* %3
    %4 = alloca i32
    store i32 6, i32* %4
    %5 = load i32, i32* %1
    store i32 %5, i32* %1
    %6 = sub i32 0, 11

    store i32 %6, i32* %1
    %7 = add i32 1, 1

    %8 = load i32, i32* %1
    %9 = add i32 2, 5

    %10 = load i32, i32* %1
    %11 = sub i32 %9, %10

    %12 = getelementptr [5 x i32], [5 x i32]* @arr1_g, i32 0, i32 1
    %13 = load i32, i32* %12
    %14 = load i32, i32* %1
    %15 = load i32, i32* %2
    %16 = sdiv i32 %14, %15

    %17 = load i32, i32* %3
    %18 = icmp slt i32 %16, %17

    br i1 %18, label %19, label %20


19:
    br label %20


20:
    %21 = load i32, i32* %1
    %22 = load i32, i32* %2
    %23 = srem i32 %21, %22

    %24 = load i32, i32* %3
    %25 = icmp sgt i32 %23, %24

    br i1 %25, label %26, label %27


26:
    br label %27


27:
    %28 = load i32, i32* %1
    %29 = load i32, i32* %2
    %30 = srem i32 %28, %29

    %31 = load i32, i32* %3
    %32 = icmp sge i32 %30, %31

    br i1 %32, label %33, label %34


33:
    br label %34


34:
    %35 = load i32, i32* %1
    %36 = load i32, i32* %2
    %37 = srem i32 %35, %36

    %38 = load i32, i32* %3
    %39 = icmp sle i32 %37, %38

    br i1 %39, label %40, label %41


40:
    br label %41


41:
    %42 = icmp eq i32 3, 2

    br i1 %42, label %43, label %44


43:
    br label %44


44:
    %45 = icmp ne i32 3, 2

    br i1 %45, label %46, label %47


46:
    br label %47


47:
    %48 = icmp ne i32 3, 0

    br i1 %48, label %52, label %50


49:
    br label %50


50:
    %51 = icmp ne i32 3, 0

    br i1 %51, label %54, label %58


52:
    %53 = icmp ne i32 2, 0

    br i1 %53, label %49, label %50


54:
    br label %55


55:
    %56 = load i32, i32* @temp
    %57 = icmp eq i32 %56, 0

    br i1 %57, label %60, label %63


58:
    %59 = icmp ne i32 2, 0

    br i1 %59, label %54, label %55


60:
    br label %61


61:
    %62 = sub i32 0, 1

    ret void

63:
    %64 = load i32, i32* @c_var1_g
    %65 = icmp ne i32 %64, 0

    br i1 %65, label %60, label %61

}

define dso_local void @for_loop(){
0:
    %1 = alloca i32
    %2 = alloca i32
    store i32 0, i32* %1
    br label %3


3:
    %4 = load i32, i32* %1
    %5 = icmp slt i32 %4, 3

    br i1 %5, label %6, label %7


6:
    store i32 0, i32* %2
    br label %11


7:
    br label %19


8:
    %9 = load i32, i32* %1
    %10 = add i32 %9, 1

    store i32 %10, i32* %1
    br label %3


11:
    %12 = load i32, i32* %2
    %13 = icmp slt i32 %12, 3

    br i1 %13, label %14, label %15


14:
    br label %16


15:
    br label %8


16:
    %17 = load i32, i32* %2
    %18 = add i32 %17, 1

    store i32 %18, i32* %2
    br label %11


19:
    br label %20


20:
    br label %21


21:
    store i32 0, i32* %1
    br label %22


22:
    br label %23


23:
    br label %24


24:
    br label %25


25:
    %26 = load i32, i32* %1
    %27 = icmp slt i32 %26, 10

    br i1 %27, label %28, label %29


28:
    br label %29


29:
    br label %30


30:
    br label %31


31:
    br label %32


32:
    store i32 0, i32* %1
    br label %36


33:
    %34 = load i32, i32* %1
    %35 = add i32 %34, 1

    store i32 %35, i32* %1
    br label %30


36:
    %37 = load i32, i32* %1
    %38 = icmp slt i32 %37, 10

    br i1 %38, label %39, label %42


39:
    %40 = load i32, i32* %1
    %41 = add i32 %40, 1

    store i32 %41, i32* %1
    br label %36


42:
    br label %43


43:
    %44 = load i32, i32* %1
    %45 = icmp slt i32 %44, 10

    br i1 %45, label %46, label %47


46:
    br label %48


47:
    store i32 0, i32* %1
    br label %51


48:
    %49 = load i32, i32* %1
    %50 = add i32 %49, 1

    store i32 %50, i32* %1
    br label %43


51:
    br label %52


52:
    %53 = load i32, i32* %1
    %54 = icmp sgt i32 %53, 10

    br i1 %54, label %59, label %60


55:
    ret void

56:
    %57 = load i32, i32* %1
    %58 = add i32 %57, 1

    store i32 %58, i32* %1
    br label %51


59:
    br label %55


60:
    br label %56

}

define dso_local i32 @is_palindrome(i32){
1:
    %2 = alloca i32
    store i32 %0, i32* %2
    %3 = load i32, i32* %2
    %4 = alloca i32
    store i32 %3, i32* %4
    %5 = alloca i32
    store i32 0, i32* %5
    %6 = load i32, i32* %4
    %7 = add i32 %6, 2

    store i32 %7, i32* %4
    %8 = icmp ne i32 1, 0

    br i1 %8, label %9, label %10


9:
    br label %10


10:
    %11 = load i32, i32* %4
    %12 = icmp ne i32 %11, 0

    br i1 %12, label %13, label %14


13:
    br label %15


14:
    br label %15


15:
    %16 = load i32, i32* %2
    %17 = icmp sge i32 %16, 0

    br i1 %17, label %20, label %19


18:
    ret i32 1

19:
    ret i32 0

20:
    %21 = load i32, i32* %2
    %22 = icmp slt i32 %21, 10

    br i1 %22, label %18, label %19


23:
    br label %24


24:
    %25 = load i32, i32* %4
    %26 = icmp sgt i32 %25, 0

    br i1 %26, label %27, label %34


27:
    %28 = load i32, i32* %5
    %29 = mul i32 %28, 10

    store i32 %29, i32* %5
    %30 = load i32, i32* %5
    %31 = load i32, i32* %4
    %32 = srem i32 %31, 10

    %33 = add i32 %30, %32

    store i32 %33, i32* %5
    br label %38


34:
    %35 = load i32, i32* %5
    %36 = load i32, i32* %2
    %37 = icmp eq i32 %35, %36

    br i1 %37, label %41, label %42


38:
    %39 = load i32, i32* %4
    %40 = sdiv i32 %39, 10

    store i32 %40, i32* %4
    br label %24


41:
    ret i32 1

42:
    ret i32 0
}

define dso_local i32 @main(){
0:
    %1 = alloca i32
    %2 = call i32 @getint()
    store i32 %2, i32* %1
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 51)
    call void @putch(i32 55)
    call void @putch(i32 51)
    call void @putch(i32 51)
    call void @putch(i32 51)
    call void @putch(i32 57)
    call void @putch(i32 10)
    %3 = add i32 1, 2

    call void @func3(i32 %3)
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 51)
    call void @putch(i32 55)
    call void @putch(i32 51)
    call void @putch(i32 51)
    call void @putch(i32 51)
    call void @putch(i32 57)
    call void @putch(i32 10)
    call void @putch(i32 50)
    call void @putch(i32 49)
    call void @putch(i32 51)
    call void @putch(i32 55)
    call void @putch(i32 51)
    call void @putch(i32 51)
    call void @putch(i32 51)
    call void @putch(i32 57)
    call void @putch(i32 10)
    %4 = alloca i32
    store i32 6, i32* %4
    %5 = alloca i32
    store i32 6, i32* %5
    %6 = alloca i32
    store i32 6, i32* %6
    %7 = alloca [2 x i32]
    %8 = getelementptr [2 x i32], [2 x i32]* %7, i32 0, i32 0
    store i32 0, i32* %8
    %9 = getelementptr [2 x i32], [2 x i32]* %7, i32 0, i32 1
    store i32 1, i32* %9
    %10 = alloca [2 x [2 x i32]]
    %11 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %10, i32 0, i32 0, i32 0
    store i32 1, i32* %11
    %12 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %10, i32 0, i32 0, i32 1
    store i32 1, i32* %12
    %13 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %10, i32 0, i32 1, i32 0
    store i32 2, i32* %13
    %14 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %10, i32 0, i32 1, i32 1
    store i32 2, i32* %14
    %15 = alloca i32
    %16 = alloca i32
    %17 = alloca i32
    %18 = alloca [5 x i32]
    %19 = alloca [2 x [2 x i32]]
    %20 = alloca i32
    store i32 114514, i32* %20
    %21 = alloca i32
    store i32 1919810, i32* %21
    %22 = alloca i32
    store i32 810, i32* %22
    %23 = alloca [5 x i32]
    %24 = getelementptr [5 x i32], [5 x i32]* %23, i32 0, i32 0
    store i32 8, i32* %24
    %25 = getelementptr [5 x i32], [5 x i32]* %23, i32 0, i32 1
    store i32 7, i32* %25
    %26 = getelementptr [5 x i32], [5 x i32]* %23, i32 0, i32 2
    store i32 6, i32* %26
    %27 = getelementptr [5 x i32], [5 x i32]* %23, i32 0, i32 3
    store i32 5, i32* %27
    %28 = getelementptr [5 x i32], [5 x i32]* %23, i32 0, i32 4
    store i32 4, i32* %28
    %29 = alloca [2 x [2 x i32]]
    %30 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 0, i32 0
    store i32 1, i32* %30
    %31 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 0, i32 1
    store i32 1, i32* %31
    %32 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 1, i32 0
    store i32 2, i32* %32
    %33 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 1, i32 1
    store i32 2, i32* %33
    %34 = alloca [1 x i32]
    %35 = getelementptr [1 x i32], [1 x i32]* %34, i32 0, i32 0
    store i32 0, i32* %35
    %36 = alloca i32
    store i32 0, i32* %36
    %37 = getelementptr [5 x i32], [5 x i32]* %23, i32 0, i32 0
    %38 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 0
    %39 = call i32 @func1(i32 1, i32* %37, [2 x i32]* %38)
    store i32 %39, i32* %36
    %40 = load i32, i32* %36
    call void @putint(i32 %40)
    call void @putch(i32 10)
    %41 = alloca i32
    store i32 0, i32* %41
    br label %42


42:
    %43 = load i32, i32* %41
    %44 = icmp slt i32 %43, 2

    br i1 %44, label %45, label %49


45:
    %46 = load i32, i32* %41
    %47 = getelementptr [2 x i32], [2 x i32]* %7, i32 0, i32 %46
    %48 = load i32, i32* %47
    call void @putint(i32 %48)
    call void @putch(i32 10)
    br label %69


49:
    call void @func2()
    %50 = getelementptr [5 x i32], [5 x i32]* @arr1_g, i32 0, i32 1
    store i32 2, i32* %50
    %51 = getelementptr [5 x i32], [5 x i32]* @arr1_g, i32 0, i32 4
    %52 = load i32, i32* @c_var1_g
    store i32 %52, i32* %51
    %53 = getelementptr [5 x i32], [5 x i32]* @arr1_g, i32 0, i32 2
    %54 = getelementptr [5 x i32], [5 x i32]* @arr1_g, i32 0, i32 1
    %55 = load i32, i32* %54
    store i32 %55, i32* %53
    %56 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* @arr2_g, i32 0, i32 1, i32 1
    %57 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* @arr2_g, i32 0, i32 1, i32 2
    %58 = load i32, i32* %57
    store i32 %58, i32* %56
    %59 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* @arr2_g, i32 0, i32 1, i32 1
    %60 = getelementptr [5 x i32], [5 x i32]* @arr1_g, i32 0, i32 1
    %61 = load i32, i32* %60
    %62 = mul i32 %61, 3

    store i32 %62, i32* %59
    %63 = load i32, i32* %1
    call void @putch(i32 120)
    call void @putch(i32 32)
    call void @putch(i32 101)
    call void @putch(i32 113)
    call void @putch(i32 117)
    call void @putch(i32 97)
    call void @putch(i32 108)
    call void @putch(i32 115)
    call void @putch(i32 32)
    call void @putint(i32 %63)
    call void @putch(i32 10)
    %64 = alloca i32
    store i32 0, i32* %64
    %65 = load i32, i32* %1
    %66 = call i32 @is_palindrome(i32 %65)
    store i32 %66, i32* %64
    %67 = load i32, i32* %64
    %68 = icmp eq i32 %67, 1

    br i1 %68, label %72, label %74


69:
    %70 = load i32, i32* %41
    %71 = add i32 %70, 1

    store i32 %71, i32* %41
    br label %42


72:
    %73 = load i32, i32* %1
    call void @putint(i32 %73)
    call void @putch(i32 32)
    call void @putch(i32 105)
    call void @putch(i32 115)
    call void @putch(i32 32)
    call void @putch(i32 97)
    call void @putch(i32 32)
    call void @putch(i32 112)
    call void @putch(i32 97)
    call void @putch(i32 108)
    call void @putch(i32 105)
    call void @putch(i32 110)
    call void @putch(i32 100)
    call void @putch(i32 114)
    call void @putch(i32 111)
    call void @putch(i32 109)
    call void @putch(i32 101)
    call void @putch(i32 10)
    br label %76


74:
    %75 = load i32, i32* %1
    call void @putint(i32 %75)
    call void @putch(i32 32)
    call void @putch(i32 105)
    call void @putch(i32 115)
    call void @putch(i32 32)
    call void @putch(i32 110)
    call void @putch(i32 111)
    call void @putch(i32 116)
    call void @putch(i32 32)
    call void @putch(i32 97)
    call void @putch(i32 32)
    call void @putch(i32 112)
    call void @putch(i32 97)
    call void @putch(i32 108)
    call void @putch(i32 105)
    call void @putch(i32 110)
    call void @putch(i32 100)
    call void @putch(i32 114)
    call void @putch(i32 111)
    call void @putch(i32 109)
    call void @putch(i32 101)
    call void @putch(i32 10)
    br label %76


76:
    ret i32 0
}

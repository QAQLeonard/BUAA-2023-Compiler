declare i32 @getint(...) 
declare void @putint(i32)
declare void @putstr(i8* )

@.str0 = constant [10 x i8] c"21371295
\00"



define dso_local i32 @main() {
b0:
	call void @putstr(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str0, i64 0, i64 0))
	%1 = alloca i32
	store i32 0, i32* %1
	%2 = load i32, i32* %1
	%3 = icmp sgt i32 %2, 0
	br i1 %3, label %b1, label %b2
b1:
	store i32 1, i32* %1
	br label %b3
b2:
	store i32 0, i32* %1
	br label %b3
b3:
	%4 = alloca i32
	store i32 1, i32* %4
	%5 = alloca i32
	store i32 2, i32* %5
	%6 = load i32, i32* %4
	%7 = icmp sge i32 %6, 0
	br i1 %7, label %b4, label %b5
b4:
	store i32 1, i32* %4
	br label %b5
b5:
	%8 = load i32, i32* %4
	%9 = icmp sge i32 %8, 0
	br i1 %9, label %b6, label %b7
b6:
	br label %b7
b7:
	%10 = load i32, i32* %4
	%11 = icmp eq i32 0, %10
	%12 = zext i1 %11 to i32
	%13 = icmp ne i32 %12, 0
	br i1 %13, label %b8, label %b9
b8:
	br label %b10
b9:
	br label %b10
b10:
	%14 = load i32, i32* %5
	%15 = icmp sle i32 %14, 0
	br i1 %15, label %b11, label %b12
b11:
	store i32 2, i32* %5
	br label %b12
b12:
	%16 = load i32, i32* %4
	%17 = icmp ne i32 %16, 0
	%18 = zext i1 %17 to i32
	%19 = load i32, i32* %5
	%20 = icmp eq i32 %18, %19
	br i1 %20, label %b13, label %b14
b13:
	store i32 1, i32* %1
	br label %b14
b14:
	%21 = load i32, i32* %4
	%22 = icmp ne i32 %21, 0
	%23 = zext i1 %22 to i32
	%24 = load i32, i32* %5
	%25 = icmp ne i32 %23, %24
	br i1 %25, label %b15, label %b16
b15:
	store i32 0, i32* %1
	br label %b16
b16:
	%26 = load i32, i32* %4
	%27 = icmp sgt i32 %26, 0
	br i1 %27, label %b17, label %b18
b17:
	store i32 1, i32* %1
	br label %b18
b18:
	%28 = load i32, i32* %4
	%29 = icmp sgt i32 %28, 0
	br i1 %29, label %b19, label %b20
b19:
	store i32 2, i32* %1
	br label %b20
b20:
	%30 = load i32, i32* %4
	%31 = add i32 %30, 1
	%32 = mul i32 3, 4
	%33 = sdiv i32 %32, 3
	%34 = srem i32 %33, 2
	%35 = sub i32 %31, %34
	store i32 %35, i32* %4
	%36 = add i32 1, 1
	store i32 %36, i32* %4
	%37 = sub i32 0, 1
	store i32 %37, i32* %4
	ret i32 0
}
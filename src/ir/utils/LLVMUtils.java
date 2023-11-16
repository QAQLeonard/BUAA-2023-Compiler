package ir.utils;

import ir.value.instructions.Operator;

public class LLVMUtils
{
    public static int calculate(Operator op, int a, int b)
    {
        switch (op)
        {
            case Add:
                return a + b;
            case Sub:
                return a - b;
            case Mul:
                return a * b;
            case Div:
                return a / b;
            case Mod:
                return a % b;
            default:
                return 0;
        }
    }
}

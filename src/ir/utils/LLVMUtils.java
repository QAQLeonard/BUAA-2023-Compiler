package ir.utils;

import ir.type.FunctionType;
import ir.type.IntegerType;
import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.BuildFactory;
import ir.value.ConstInt;
import ir.value.Value;
import ir.value.instructions.Operator;
import ir.value.instructions.terminator.BrInst;
import ir.value.instructions.terminator.RetInst;

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

    public static void checkBlockEnd(BasicBlock basicBlock)
    {
        Type retType = ((FunctionType) basicBlock.getNode().getParentList().getContainer().getType()).getReturnType();
        if (!basicBlock.getInstructionList().isEmpty())
        {
            Value lastInst = basicBlock.getInstructionList().getTail().getValue();
            if (lastInst instanceof RetInst || lastInst instanceof BrInst)
            {
                return;
            }
        }
        BuildFactory.buildRetInst(basicBlock, retType instanceof IntegerType ? ConstInt.ZERO : null);
    }
}

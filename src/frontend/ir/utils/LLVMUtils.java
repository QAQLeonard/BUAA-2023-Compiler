package frontend.ir.utils;

import frontend.ir.type.FunctionType;
import frontend.ir.type.IntegerType;
import frontend.ir.type.Type;
import frontend.ir.value.BasicBlock;
import frontend.ir.value.BuildFactory;
import frontend.ir.value.ConstInt;
import frontend.ir.value.Value;
import frontend.ir.value.instructions.Operator;
import frontend.ir.value.instructions.terminator.BrInst;
import frontend.ir.value.instructions.terminator.RetInst;

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

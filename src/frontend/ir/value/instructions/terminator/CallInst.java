package frontend.ir.value.instructions.terminator;

import frontend.ir.type.FunctionType;
import frontend.ir.type.IntegerType;
import frontend.ir.type.Type;
import frontend.ir.type.VoidType;
import frontend.ir.value.BasicBlock;
import frontend.ir.value.BuildFactory;
import frontend.ir.value.Function;
import frontend.ir.value.Value;
import frontend.ir.value.instructions.Instruction;
import frontend.ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CallInst extends Instruction
{
    public CallInst(BasicBlock basicBlock, Function function, List<Value> args)
    {
        super(((FunctionType) function.getType()).getReturnType(), Operator.Call);
        if (!(((FunctionType) function.getType()).getReturnType() instanceof VoidType))
        {
            this.setName("%" + REG_NUMBER++);
        }
        this.addOperand(function);

        for (int i = 0; i < args.size(); i++)
        {
            Type curType = args.get(i).getType();
            Type realType = ((FunctionType) function.getType()).getParametersType().get(i);
            Value tmp = convType(args.get(i), basicBlock, curType, realType);
            this.addOperand(tmp);
        }

        Function curFunction = basicBlock.getNode().getParentList().getContainer();
        function.addPredecessor(curFunction);
        curFunction.addSuccessor(function);
    }

    private Value convType(Value value, BasicBlock basicBlock, Type curType, Type realType)
    {
        boolean isCurI1 = curType instanceof IntegerType && ((IntegerType) curType).isI1();
        boolean isCurI32 = curType instanceof IntegerType && ((IntegerType) curType).isI32();
        boolean isRealI1 = realType instanceof IntegerType && ((IntegerType) realType).isI1();
        boolean isRealI32 = realType instanceof IntegerType && ((IntegerType) realType).isI32();
        if (!isCurI1 && !isCurI32 && !isRealI1 && !isRealI32)
        {
            return value;
        }
        else if ((isCurI1 && isRealI1) || (isCurI32 && isRealI32))
        {
            return value;
        }
        else if (isCurI1 && isRealI32)
        {
            return BuildFactory.buildZext(value, basicBlock);
        }
        else if (isCurI32 && isRealI1)
        {
            return BuildFactory.buildConvToI1(value, basicBlock);
        }
        else
        {
            return value;
        }
    }

    public Function getCalledFunction()
    {
        return (Function) this.getOperands().get(0);
    }

    @Override
    public void output(File destFile) throws IOException
    {
        StringBuilder s = new StringBuilder();
        Type returnType = ((FunctionType) this.getCalledFunction().getType()).getReturnType();
        if (returnType instanceof VoidType)
        {
            s.append("call ");
        }
        else
        {
            s.append(this.getName()).append(" = call ");
        }
        s.append(returnType).append(" @").append(this.getCalledFunction().getName()).append("(");
        for (int i = 1; i < this.getOperands().size(); i++)
        {
            s.append(this.getOperands().get(i).getType()).append(" ").append(this.getOperands().get(i).getName());
            if (i != this.getOperands().size() - 1)
            {
                s.append(", ");
            }
        }
        s.append(")");
        FileOperate.outputFileUsingUsingBuffer(destFile, s.toString(), true);
    }
}

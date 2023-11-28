package ir.value.instructions.terminator;

import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class RetInst extends Instruction
{
    public RetInst(Value ret)
    {
        super(ret != null ? ret.getType() : VoidType.voidType, Operator.Ret);
        if (ret != null)
        {
            this.addOperand(ret);
        }
    }

    @Override
    public void outputIR(File destFile) throws IOException
    {
        if (getOperands().size() == 1)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "ret " + getOperands().get(0).getType() + " " + getOperands().get(0).getName(), true);
        }
        else
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "ret void", true);
        }
    }

}


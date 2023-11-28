package ir.value.instructions;

import ir.type.IntegerType;
import ir.type.PointerType;
import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.Value;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class ConvInst extends Instruction
{
    public ConvInst(BasicBlock basicBlock, Operator op, Value value)
    {
        super(VoidType.voidType, op);
        this.setName("%" + REG_NUMBER++);
        if (op == Operator.Zext)
        {
            setType(IntegerType.i32);
        }
        else if (op == Operator.Bitcast)
        {
            setType(new PointerType(IntegerType.i32));
        }
        addOperand(value);
    }

    @Override
    public void outputIR(File destFile) throws IOException
    {
        if(op == Operator.Zext)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, getName() + " = zext i1 " + getOperands().get(0).getName() + " to i32", true);
        }
        else if(op == Operator.Bitcast)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, getName() + " = bitcast " + getOperands().get(0).getType() + getOperands().get(0).getName() + " to i32*", true);
        }
    }
}

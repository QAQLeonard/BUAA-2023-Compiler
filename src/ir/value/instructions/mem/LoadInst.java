package ir.value.instructions.mem;

import ir.type.ArrayType;
import ir.type.PointerType;
import ir.value.BasicBlock;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class LoadInst extends Instruction
{

    public LoadInst(BasicBlock basicBlock, Value pointer)
    {
        super(((PointerType) pointer.getType()).getTargetType(), Operator.Load);
        this.setName("%" + REG_NUMBER++);
        if (getType() instanceof ArrayType)
        {
            setType(new PointerType(((ArrayType) getType()).getElementType()));
        }
        this.addOperand(pointer);

    }

    public Value getPointer()
    {
        return getOperands().get(0);
    }

    @Override
    public void output(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, getName() + " = load " + getType() + ", " + getPointer().getType() + " " + getPointer().getName(), true);
    }
}

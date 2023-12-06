package ir.value.instructions.mem;

import ir.type.ArrayType;
import ir.type.PointerType;
import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class AllocaInst extends Instruction
{
    boolean isConst;
    Type allocaType;

    public AllocaInst(BasicBlock basicBlock, boolean isConst, Type allocaType)
    {
        super(new PointerType(allocaType), Operator.Alloca);
        this.setName("%" + REG_NUMBER++);
        this.isConst = isConst;
        this.allocaType = allocaType;
        if (allocaType instanceof ArrayType)
        {
            if (((ArrayType) allocaType).getLength() == -1)
            {
                this.allocaType = new PointerType(((ArrayType) allocaType).getElementType());
                setType(new PointerType(this.allocaType));
            }
        }
    }

    public boolean isConst()
    {
        return isConst;
    }

    public Type getAllocaType()
    {
        return allocaType;
    }

    @Override
    public void output(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.getName() + " = alloca " + this.getAllocaType(), true);
    }
}

package frontend.ir.value.instructions.mem;

import frontend.ir.type.ArrayType;
import frontend.ir.type.PointerType;
import frontend.ir.type.Type;
import frontend.ir.value.instructions.Instruction;
import frontend.ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class AllocaInst extends Instruction
{
    boolean isConst;
    Type allocaType;

    public AllocaInst(boolean isConst, Type allocaType)
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

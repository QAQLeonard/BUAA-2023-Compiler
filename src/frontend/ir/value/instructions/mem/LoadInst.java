package frontend.ir.value.instructions.mem;

import frontend.ir.type.ArrayType;
import frontend.ir.type.PointerType;
import frontend.ir.value.Value;
import frontend.ir.value.instructions.Instruction;
import frontend.ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class LoadInst extends Instruction
{

    public LoadInst(Value pointer)
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

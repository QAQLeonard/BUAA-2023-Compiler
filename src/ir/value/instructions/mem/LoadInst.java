package ir.value.instructions.mem;

import ir.type.ArrayType;
import ir.type.PointerType;
import ir.value.BasicBlock;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

public class LoadInst extends Instruction
{

    public LoadInst(BasicBlock basicBlock, Value pointer)
    {
        super(((PointerType) pointer.getType()).getTargetType(), Operator.Load, basicBlock);
        this.setName("%" + REG_NUMBER++);
        if (getType() instanceof ArrayType)
        {
            setType(new PointerType(((ArrayType) getType()).getElementType()));
        }
        this.addOperand(pointer);
        this.addInstToBlock(basicBlock);
    }

    public Value getPointer()
    {
        return getOperands().get(0);
    }

    @Override
    public String toString()
    {
        return getName() + " = load " + getType() + ", " + getPointer().getType() + " " + getPointer().getName();
    }
}

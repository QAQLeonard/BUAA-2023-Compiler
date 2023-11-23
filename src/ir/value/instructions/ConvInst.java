package ir.value.instructions;

import ir.type.IntegerType;
import ir.type.PointerType;
import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.Value;

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
    public String toString()
    {
        if (op == Operator.Zext)
        {
            return getName() + " = zext i1 " + getOperands().get(0).getName() + " to i32";
        }
        else if (op == Operator.Bitcast)
        {
            return getName() + " = bitcast " + getOperands().get(0).getType() + getOperands().get(0).getName() + " to i32*";
        }
        else
        {
            return null;
        }
    }
}

package ir.value.instructions.mem;

import ir.value.BasicBlock;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

public class StoreInst extends Instruction
{
    public StoreInst(BasicBlock basicBlock, Value pointer, Value value)
    {
        super(value.getType(), Operator.Store, basicBlock);
        this.addOperand(value);
        this.addOperand(pointer);
    }

    @Override
    public String toString()
    {
        return "store " + getOperands().get(0).getType() + " " + getOperands().get(0).getName() + ", " + getOperands().get(1).getType() + " " + getOperands().get(1).getName();
    }
}

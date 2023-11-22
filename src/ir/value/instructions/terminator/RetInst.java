package ir.value.instructions.terminator;

import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

public class RetInst extends Instruction
{
    public RetInst(BasicBlock basicBlock, Value ret)
    {
        super(ret != null ? ret.getType() : VoidType.voidType, Operator.Ret, basicBlock);
        if (ret != null)
        {
            this.addOperand(ret);
        }
    }


    @Override
    public String toString()
    {
        if (getOperands().size() == 1)
        {
            return "ret " + getOperands().get(0).getType() + " " + getOperands().get(0).getName();
        }
        else
        {
            return "ret void";
        }
    }

}


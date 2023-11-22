package ir.value.instructions.terminator;

import ir.type.IntegerType;
import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.BuildFactory;
import ir.value.ConstInt;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

public class BrInst extends Instruction
{
    public BrInst(BasicBlock basicBlock, BasicBlock trueBlock)
    {
        super(VoidType.voidType, Operator.Br, basicBlock);
        this.addOperand(trueBlock);
        // 添加前驱后继
        if (basicBlock != null)
        {
            if (basicBlock.getInstructionList().getTail() == null || (!(basicBlock.getInstructionList().getTail().getValue() instanceof BrInst) && !(basicBlock.getInstructionList().getTail().getValue() instanceof RetInst)))
            {
                basicBlock.addSuccessor(trueBlock);
                trueBlock.addSuccessor(basicBlock);
            }
        }
    }

    public BrInst(BasicBlock basicBlock, Value cond, BasicBlock trueBlock, BasicBlock falseBlock)
    {
        super(VoidType.voidType, Operator.Br, basicBlock);
        // conversion handler
        Value condTmp = cond;
        if (!(cond.getType() instanceof IntegerType && ((IntegerType) cond.getType()).isI1()))
        {
            condTmp = BuildFactory.buildBinary(basicBlock, Operator.Ne, cond, new ConstInt(0));
        }
        this.addOperand(condTmp);
        this.addOperand(trueBlock);
        this.addOperand(falseBlock);
        // 添加前驱后继
        if (basicBlock.getInstructionList().getTail() == null || (!(basicBlock.getInstructionList().getTail().getValue() instanceof BrInst) && !(basicBlock.getInstructionList().getTail().getValue() instanceof RetInst)))
        {
            basicBlock.addSuccessor(trueBlock);
            basicBlock.addSuccessor(falseBlock);
            trueBlock.addSuccessor(basicBlock);
            falseBlock.addSuccessor(basicBlock);
        }
    }

    @Override
    public String toString()
    {
        if (this.getOperands().size() == 1)
        {
            return "br label %" + this.getOperands().get(0).getName();
        }
        else
        {
            return "br " + this.getOperands().get(0).getType() + " " + this.getOperands().get(0).getName() + ", label %" + this.getOperands().get(1).getName() + ", label %" + this.getOperands().get(2).getName();
        }
    }
}


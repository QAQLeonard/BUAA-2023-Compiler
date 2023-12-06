package ir.value.instructions.terminator;

import ir.type.IntegerType;
import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.BuildFactory;
import ir.value.ConstInt;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class BrInst extends Instruction
{
    public BrInst(BasicBlock trueBlock)
    {
        super(VoidType.voidType, Operator.Br);
        this.addOperand(trueBlock);
    }

    public BrInst(BasicBlock basicBlock, Value cond, BasicBlock trueBlock, BasicBlock falseBlock)
    {
        super(VoidType.voidType, Operator.Br);
        // conversion handler
        Value condTmp = cond;
        if (!(cond.getType() instanceof IntegerType && ((IntegerType) cond.getType()).isI1()))
        {
            condTmp = BuildFactory.getBinaryInst(basicBlock, Operator.Ne, cond, new ConstInt(0));
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

    public BasicBlock getTrueLabel()
    {
        if (this.getOperands().size() == 3)  // is CondBr
        {
            return (BasicBlock) this.getOperands().get(1);
        }
        else
        {
            return (BasicBlock) this.getOperands().get(0);
        }
    }

    public BasicBlock getFalseLabel()
    {
        if (this.getOperands().size() == 3)
        {
            return (BasicBlock) this.getOperands().get(2);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void output(File destFile) throws IOException
    {
        if (this.getOperands().size() == 1)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "br label %" + this.getOperands().get(0).getName() + "\n", true);
        }
        else
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "br " + this.getOperands().get(0).getType() + " " + this.getOperands().get(0).getName() + ", label %" + this.getOperands().get(1).getName() + ", label %" + this.getOperands().get(2).getName() + "\n", true);
        }
    }
}


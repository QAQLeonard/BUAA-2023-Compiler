package ir.value;

import ir.type.LabelType;
import ir.value.instructions.Instruction;
import ir.value.instructions.terminator.BrInst;
import ir.value.instructions.terminator.RetInst;
import ir.utils.IRLinkedList;
import ir.utils.IRListNode;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasicBlock extends Value
{
    IRLinkedList<Instruction, BasicBlock> instructionList;
    IRListNode<BasicBlock, Function> node;
    List<BasicBlock> predecessors;
    List<BasicBlock> successors;

    public BasicBlock(Function function)
    {
        super(String.valueOf(REG_NUMBER++), new LabelType());
        this.instructionList = new IRLinkedList<>(this);
        this.node = new IRListNode<>(this);
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
        function.getList().insertAtTail(this.node);
    }

    public IRLinkedList<Instruction, BasicBlock> getInstructionList()
    {
        return instructionList;
    }

    public IRListNode<BasicBlock, Function> getNode()
    {
        return node;
    }

    public List<BasicBlock> getPredecessors()
    {
        return predecessors;
    }

    public void addSuccessor(BasicBlock successor)
    {
        this.successors.add(successor);
    }

    public void addInst(Instruction instruction)
    {
        if (instructionList.getTail() == null || (!(instructionList.getTail().getValue() instanceof BrInst) && !(instructionList.getTail().getValue() instanceof RetInst)))
        {
            instructionList.insertAtTail(instruction.getNode());
        }
        else
        {
            instruction.removeUseFromOperands();
        }
    }

    public void refreshReg()
    {
        for (IRListNode<Instruction, BasicBlock> irNode : this.instructionList)
        {
            Instruction inst = irNode.getValue();
            if (inst.requiresRegisterRenaming())
            {
                inst.setName("%" + REG_NUMBER++);
            }
        }
    }

    @Override
    public void output(File destFile) throws IOException
    {
        for (IRListNode<Instruction, BasicBlock> instruction : this.instructionList)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "    ", true);
            instruction.getValue().output(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
        }
    }
}

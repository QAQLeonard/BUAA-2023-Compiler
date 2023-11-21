package ir.value.instructions;

import ir.IRModule;
import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.User;
import ir.value.instructions.terminator.BrInst;
import ir.value.instructions.terminator.RetInst;
import ir.utils.IRListNode;

public abstract class Instruction extends User
{
    Operator op;
    IRListNode<Instruction, BasicBlock> node;
    int handler;
    static int HANDLER = 0;

    public Instruction(Type type, Operator op, BasicBlock basicBlock)
    {
        super("", type);
        this.op = op;
        this.node = new IRListNode<>(this);
        this.handler = HANDLER++;
        IRModule.getInstance().addInstruction(handler, this);
    }

    public Operator getOperator()
    {
        return op;
    }

    public void setOperator(Operator op)
    {
        this.op = op;
    }

    public IRListNode<Instruction, BasicBlock> getNode()
    {
        return node;
    }

    public void setNode(IRListNode<Instruction, BasicBlock> node)
    {
        this.node = node;
    }

    public int getHandler()
    {
        return handler;
    }

    public void setHandler(int handler)
    {
        this.handler = handler;
    }

    public static int getHANDLER()
    {
        return HANDLER;
    }

    public static void setHANDLER(int HANDLER)
    {
        Instruction.HANDLER = HANDLER;
    }

    public BasicBlock getParent()
    {
        return this.node.getParentList().getContainer();
    }

    public void addInstToBlock(BasicBlock basicBlock)
    {
        if (basicBlock.getInstructions().getTail() == null || (!(basicBlock.getInstructions().getTail().getValue() instanceof BrInst) && !(basicBlock.getInstructions().getTail().getValue() instanceof RetInst)))
        {
            basicBlock.getInstructions().insertAtTail(this.node);
        }
        else
        {
            this.removeUseFromOperands();
        }
    }

    public void addInstToBlockBegin(BasicBlock basicBlock)
    {
        basicBlock.getInstructions().insertAtHead(this.node);
    }
}

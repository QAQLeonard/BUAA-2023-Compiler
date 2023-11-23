package ir.value.instructions;

import ir.IRModule;
import ir.type.FunctionType;
import ir.type.Type;
import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.User;
import ir.value.instructions.mem.StoreInst;
import ir.value.instructions.terminator.BrInst;
import ir.value.instructions.terminator.CallInst;
import ir.value.instructions.terminator.RetInst;
import ir.utils.IRListNode;

public abstract class Instruction extends User
{
    Operator op;
    IRListNode<Instruction, BasicBlock> node;
    int handler;
    static int HANDLER = 0;

    public Instruction(Type type, Operator op)
    {
        super("", type);
        this.op = op;
        this.node = new IRListNode<>(this);
        this.handler = HANDLER++;
        IRModule.getInstance().addInstruction(handler, this);
    }

    public boolean requiresRegisterRenaming()
    {
        if (this instanceof StoreInst || this instanceof BrInst || this instanceof RetInst)
        {
            return false;
        }
        if (this instanceof CallInst)
        {
            FunctionType functionType = (FunctionType) this.getOperands().get(0).getType();
            return !(functionType.getReturnType() instanceof VoidType);
        }
        return true;
    }

    public IRListNode<Instruction, BasicBlock> getNode()
    {
        return node;
    }

}

package frontend.ir.value.instructions;

import frontend.ir.IRModule;
import frontend.ir.type.FunctionType;
import frontend.ir.type.Type;
import frontend.ir.type.VoidType;
import frontend.ir.value.BasicBlock;
import frontend.ir.value.User;
import frontend.ir.value.instructions.mem.StoreInst;
import frontend.ir.value.instructions.terminator.BrInst;
import frontend.ir.value.instructions.terminator.CallInst;
import frontend.ir.value.instructions.terminator.RetInst;
import frontend.ir.utils.IRListNode;

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

    public Operator getOperator()
    {
        return op;
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

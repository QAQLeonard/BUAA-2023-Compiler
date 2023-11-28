package ir.value;

import ir.IRModule;
import ir.type.FunctionType;
import ir.type.Type;
import ir.utils.IRLinkedList;
import ir.utils.IRListNode;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Function extends Value
{
    final IRLinkedList<BasicBlock, Function> list;
    final IRListNode<Function, IRModule> node;//自身对应的node
    final List<Argument> arguments;
    final List<Function> predecessors;
    final List<Function> successors;
    final boolean isLibraryFunction;

    public Function(String name, Type type, boolean isLibraryFunction)
    {
        super(name, type);
        REG_NUMBER = 0;
        this.list = new IRLinkedList<>(this);
        this.node = new IRListNode<>(this);
        this.arguments = new ArrayList<>();
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
        this.isLibraryFunction = isLibraryFunction;
        for (Type t : ((FunctionType) type).getParametersType())
        {
            arguments.add(new Argument(t, ((FunctionType) type).getParametersType().indexOf(t), isLibraryFunction));
        }
        IRModule.getInstance().getFunctions().insertAtTail(this.node);
    }

    public IRLinkedList<BasicBlock, Function> getList()
    {
        return list;
    }

    public IRListNode<Function, IRModule> getNode()
    {
        return node;
    }

    public List<Value> getArguments()
    {
        return new ArrayList<>(arguments);
    }

    public void addPredecessor(Function predecessor)
    {
        this.predecessors.add(predecessor);
    }

    public void addSuccessor(Function successor)
    {
        this.successors.add(successor);
    }

    public boolean isLibraryFunction()
    {
        return isLibraryFunction;
    }

    public void refreshArgReg()
    {
        for (Argument arg : arguments)
        {
            arg.setName("%" + REG_NUMBER++);
        }
    }

    @Override
    public void outputIR(File destFile) throws IOException
    {
        StringBuilder s = new StringBuilder();
        s.append(((FunctionType) this.getType()).getReturnType()).append(" @").append(this.getName()).append("(");
        for (int i = 0; i < arguments.size(); i++)
        {
            s.append(arguments.get(i).getType());
            if (i != arguments.size() - 1)
            {
                s.append(", ");
            }
        }
        s.append(")");
        FileOperate.outputFileUsingUsingBuffer(destFile, s.toString(), true);
    }
}

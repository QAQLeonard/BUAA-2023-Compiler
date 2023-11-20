package ir.value;

import ir.IRModule;
import ir.type.FunctionType;
import ir.type.Type;
import ir.utils.IRLinkedList;
import ir.utils.IRListNode;

import java.util.ArrayList;
import java.util.List;

public class Function extends Value {
    private final IRLinkedList<BasicBlock, Function> list;
    private final IRListNode<Function, IRModule> node;
    private final List<Argument> arguments;
    private final List<Function> predecessors;
    private final List<Function> successors;
    private final boolean isLibraryFunction;

    public Function(String name, Type type, boolean isLibraryFunction) {
        super(name, type);
        REG_NUMBER = 0;
        this.list = new IRLinkedList<>(this);
        this.node = new IRListNode<>(this);
        this.arguments = new ArrayList<>();
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
        this.isLibraryFunction = isLibraryFunction;
        for (Type t : ((FunctionType) type).getParametersType()) {
            arguments.add(new Argument(t, ((FunctionType) type).getParametersType().indexOf(t), isLibraryFunction));
        }
        this.node.insertAtTail(IRModule.getInstance().getFunctions());
    }

    public IRLinkedList<BasicBlock, Function> getList() {
        return list;
    }

    public IRListNode<Function, IRModule> getNode() {
        return node;
    }

    public List<Value> getArguments() {
        return new ArrayList<>(arguments);
    }

    public List<Function> getPredecessors() {
        return predecessors;
    }

    public void addPredecessor(Function predecessor) {
        this.predecessors.add(predecessor);
    }

    public List<Function> getSuccessors() {
        return successors;
    }

    public void addSuccessor(Function successor) {
        this.successors.add(successor);
    }

    public boolean isLibraryFunction() {
        return isLibraryFunction;
    }

    public void refreshArgReg() {
        for (Argument arg : arguments) {
            arg.setName("%" + REG_NUMBER++);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(((FunctionType) this.getType()).getReturnType()).append(" @").append(this.getName()).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            s.append(arguments.get(i).getType());
            // s.append(" ").append(arguments.get(i).getName());
            if (i != arguments.size() - 1) {
                s.append(", ");
            }
        }
        s.append(")");
        return s.toString();
    }
}

package ir;

import ir.value.*;
import ir.value.instructions.Instruction;
import ir.utils.IRLinkedList;
import ir.utils.IRListNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IRModule
{
    private static final IRModule module = new IRModule();
    private List<GlobalVar> globalVars;
    private IRLinkedList<Function, IRModule> functions;
    private HashMap<Integer, Instruction> instructions;

    private IRModule()
    {
        this.globalVars = new ArrayList<>();
        this.functions = new IRLinkedList<>(this);
        this.instructions = new HashMap<>();
    }

    public static IRModule getInstance()
    {
        return module;
    }

    public void addInstruction(int handle, Instruction instruction)
    {
        this.instructions.put(handle, instruction);
    }

    public IRLinkedList<Function, IRModule> getFunctions()
    {
        return this.functions;
    }

    public void addGlobalVar(GlobalVar globalVariable)
    {
        this.globalVars.add(globalVariable);
    }

    public void refreshRegNumber()
    {
        for (IRListNode<Function, IRModule> function : functions)
        {
            Value.REG_NUMBER = 0;
            function.getValue().refreshArgReg();
            if (!function.getValue().isLibraryFunction())
            {
                for (IRListNode<BasicBlock, Function> basicBlock : function.getValue().getList())
                {
                    if (basicBlock.getValue().getInstructions().isEmpty())
                    {
                        BuildFactory.getInstance().checkBlockEnd(basicBlock.getValue());
                    }
                    basicBlock.getValue().setName(String.valueOf(Value.REG_NUMBER++));
                    basicBlock.getValue().refreshReg();
                }
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (GlobalVar globalVar : globalVars)
        {
            s.append(globalVar.toString()).append("\n");
        }
        if (!globalVars.isEmpty())
        {
            s.append("\n");
        }
        refreshRegNumber();
        for (IRListNode<Function, IRModule> function : functions)
        {
            if (function.getValue().isLibraryFunction())
            {
                s.append("declare ").append(function.getValue().toString()).append("\n");
            }
            else
            {
                s.append("\ndefine dso_local ").append(function.getValue().toString()).append("{\n");
                for (IRListNode<BasicBlock, Function> basicBlock : function.getValue().getList())
                {
                    if (basicBlock != function.getValue().getList().getHead())
                    {
                        s.append("\n");
                    }
                    s.append(";<label>:").append(basicBlock.getValue().getName()).append(":\n").append(basicBlock.getValue().toString());
                }
                s.append("}\n");
            }
        }
        return s.toString();
    }
}

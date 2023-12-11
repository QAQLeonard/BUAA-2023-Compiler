package ir;

import backend.MipsGenerator;
import ir.value.*;
import ir.value.instructions.ConstArray;
import ir.value.instructions.Instruction;
import ir.utils.IRLinkedList;
import ir.utils.IRListNode;
import ir.value.instructions.mem.AllocaInst;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static backend.MipsGenerator.*;
import static ir.utils.LLVMUtils.checkBlockEnd;

public class IRModule
{
    private static final IRModule instance = new IRModule();
    List<GlobalVar> globalVars;
    IRLinkedList<Function, IRModule> functions;
    HashMap<Integer, Instruction> instructions;

    private IRModule()
    {
        this.globalVars = new ArrayList<>();
        this.functions = new IRLinkedList<>(this);
        this.instructions = new HashMap<>();
    }

    public static IRModule getInstance()
    {
        return instance;
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
                    if (basicBlock.getValue().getInstructionList().isEmpty())
                    {
                        checkBlockEnd(basicBlock.getValue());
                    }
                    basicBlock.getValue().setName(String.valueOf(Value.REG_NUMBER++));
                    basicBlock.getValue().refreshReg();
                }
            }
        }
    }

    public void outputLLVM(File destFile) throws IOException
    {
        StringBuilder s = new StringBuilder();
        for (GlobalVar globalVar : globalVars)
        {
            globalVar.output(destFile);
        }
        if (!globalVars.isEmpty())
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
        }
        refreshRegNumber();
        for (IRListNode<Function, IRModule> function : functions)
        {
            if (function.getValue().isLibraryFunction())
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, "declare ", true);
                function.getValue().output(destFile);
                FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
            }
            else
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, "\ndefine dso_local ", true);
                function.getValue().output(destFile);
                FileOperate.outputFileUsingUsingBuffer(destFile, "{\n", true);
                for (IRListNode<BasicBlock, Function> basicBlock : function.getValue().getList())
                {
                    if (basicBlock != function.getValue().getList().getHead())
                        FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
                    FileOperate.outputFileUsingUsingBuffer(destFile, basicBlock.getValue().getName() + ":\n", true);
                    basicBlock.getValue().output(destFile);
                }
                FileOperate.outputFileUsingUsingBuffer(destFile, "}\n", true);
            }
        }
    }

    public void outputMips(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, ".data", false);
        for (GlobalVar globalVar : globalVars)
        {
            if (globalVar.getValue() instanceof ConstString constString)
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, "\n# ", true);
                globalVar.output(destFile);
                FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
                FileOperate.outputFileUsingUsingBuffer(destFile, globalVar.getIdentifier() + ": .asciiz " + constString.getName() + "\n", true);
            }
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, "\n.text\n", true);
        for (GlobalVar globalVar : globalVars)
        {
            if (!(globalVar.getValue() instanceof ConstString))
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, "\n# ", true);
                globalVar.output(destFile);
                FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
            }
            if (globalVar.getValue() instanceof ConstInt constInt)
            {
                MipsGenerator.getGp(globalVar.getIdentifier());
                load("$t2", String.valueOf(constInt.getValue()));
                MipsGenerator.store("$t2", globalVar.getIdentifier());
            }
            else if (globalVar.getValue() instanceof ConstArray constArray)
            {
                MipsGenerator.getGpArray(globalVar.getIdentifier(), 4 * constArray.capacity);
                if (constArray.init || !constArray.allZero())
                {
                    ArrayList<Value> values = constArray.get1DArray();
                    for (int i = 0; i < values.size(); i++)
                    {
                        Value value = values.get(i);
                        if (value instanceof ConstInt)
                        {
                            ConstInt constInt = (ConstInt) value;
                            FileOperate.outputFileUsingUsingBuffer(destFile, "\n# ", true);
                            load("$t2", String.valueOf(constInt.getValue()));
                            load("$t0", globalVar.getIdentifier());
                            FileOperate.outputFileUsingUsingBuffer(destFile, "addu $t0, $t0, " + (4 * i) + "\n", true);
                            store("$t2", "$t0", 0);
                        }
                    }
                }
            }
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, """
                
                jal main
                li $v0, 10
                syscall
                
                """, true);

        for (IRListNode<Function, IRModule> funcEntry : functions)
        {
            Function function = funcEntry.getValue();
            if (function.isLibraryFunction())
            {
                continue;
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, "\n" + function.getName() + ":\n", true);
            rec = function.getArguments().size();
            for (int i = 0; rec > 0; i++)
            {
                rec--;
                load("$t0", "$sp", 4 * rec);
                getSp(function.getArguments().get(i).getIdentifier());
                store("$t0", function.getArguments().get(i).getIdentifier());
            }
            rec = 0;
            for (IRListNode<BasicBlock, Function> blockEntry : function.getList())
            {
                BasicBlock basicBlock = blockEntry.getValue();
                FileOperate.outputFileUsingUsingBuffer(destFile, "\n" + "label_"+basicBlock.getId() + ":\n", true);
                for (IRListNode<Instruction, BasicBlock> instEntry : basicBlock.getInstructionList())
                {
                    Instruction inst = instEntry.getValue();
                    FileOperate.outputFileUsingUsingBuffer(destFile, "\n# ", true);
                    inst.output(destFile);
                    FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
                    if (!(inst instanceof AllocaInst))
                    {
                        getSp(inst.getIdentifier());
                    }
                    translate(inst);
                }
            }
        }
    }
}


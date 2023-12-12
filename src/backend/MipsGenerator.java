package backend;

import backend.utils.Pair;
import frontend.ir.IRModule;
import frontend.ir.type.*;
import frontend.ir.value.*;
import frontend.ir.value.instructions.*;
import frontend.ir.value.instructions.mem.AllocaInst;
import frontend.ir.value.instructions.mem.GEPInst;
import frontend.ir.value.instructions.mem.LoadInst;
import frontend.ir.value.instructions.mem.StoreInst;
import frontend.ir.value.instructions.terminator.BrInst;
import frontend.ir.value.instructions.terminator.CallInst;
import frontend.ir.value.instructions.terminator.RetInst;
import utils.FileOperate;


import java.io.File;
import java.io.IOException;
import java.util.*;

import static utils.FileOperate.CreateFileUsingJava7Files;

public class MipsGenerator
{
    public static Map<String, Pair<String, Integer>> mem = new HashMap<>();
    public static int gpOff = 0, spOff = 0, rec = 0;

    public static File destFile;

    public static void outputMips() throws IOException
    {
        destFile = new File("mips.txt");
        CreateFileUsingJava7Files(destFile);
        IRModule.getInstance().outputMips(destFile);
    }

    public static void getGp(String name)
    {
        if (mem.containsKey(name))
        {
            return;
        }
        mem.put(name, new Pair<>("$gp", gpOff));
        gpOff += 4;
    }

    public static void getGpArray(String name, int offset)
    {
        if (mem.containsKey(name))
        {
            return;
        }
        getGp(name);
        FileOperate.outputFileUsingUsingBuffer(destFile, "addu $t0, $gp, " + gpOff + "\n", true);
        store("$t0", name);
        gpOff += offset;
    }

    public static void getSp(String name)
    {
        if (mem.containsKey(name))
        {
            return;
        }
        spOff -= 4;
        mem.put(name, new Pair<>("$sp", spOff));
    }

    public static void getSpArray(String name, int offset)
    {
        if (mem.containsKey(name))
        {
            return;
        }
        getSp(name);
        spOff -= offset;
        FileOperate.outputFileUsingUsingBuffer(destFile, "addu $t0, $sp, " + spOff + "\n", true);
        store("$t0", name);
    }

    public static void translate(Instruction ir)
    {
        if (ir instanceof BinaryInst) parseBinary((BinaryInst) ir);
        else if (ir instanceof CallInst) parseCall((CallInst) ir);
        else if (ir instanceof RetInst) parseRet((RetInst) ir);
        else if (ir instanceof AllocaInst) parseAlloca((AllocaInst) ir);
        else if (ir instanceof LoadInst) parseLoad((LoadInst) ir);
        else if (ir instanceof StoreInst) parseStore((StoreInst) ir);
        else if (ir instanceof GEPInst) parseGEP((GEPInst) ir);
        else if (ir instanceof BrInst) parseBr((BrInst) ir);
        else if (ir instanceof ConvInst) parseConv((ConvInst) ir);
    }

    public static void parseBinary(BinaryInst b)
    {
        // 特殊处理 Not 操作符
        if (b.getOperator() == Operator.Not)
        {
            load("$t0", b.getOperands().get(0).getIdentifier());
            FileOperate.outputFileUsingUsingBuffer(destFile, "not $t1, $t0\n", true);
            store("$t1", b.getIdentifier());
            return;
        }

        // 使用 HashMap 初始化操作符映射
        Map<Operator, String> operatorMap = new HashMap<>();
        operatorMap.put(Operator.Add, "addu");
        operatorMap.put(Operator.Sub, "subu");
        operatorMap.put(Operator.Mul, "mul");
        operatorMap.put(Operator.Div, "div");
        operatorMap.put(Operator.Mod, "rem");
        operatorMap.put(Operator.Shl, "sll");
        operatorMap.put(Operator.Shr, "srl");
        operatorMap.put(Operator.And, "and");
        operatorMap.put(Operator.Or, "or");
        operatorMap.put(Operator.Lt, "slt");
        operatorMap.put(Operator.Le, "sle");
        operatorMap.put(Operator.Gt, "sgt");
        operatorMap.put(Operator.Ge, "sge");
        operatorMap.put(Operator.Eq, "seq");
        operatorMap.put(Operator.Ne, "sne");

        // 根据操作符获取对应的 MIPS 指令
        String mipsInstruction = operatorMap.get(b.getOperator());
        if (mipsInstruction != null)
        {
            calc(b, mipsInstruction);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported binary operator: " + b.getOperator());
        }
    }


    public static void calc(BinaryInst b, String op)
    {
        load("$t0", b.getOperands().get(0).getIdentifier());
        load("$t1", b.getOperands().get(1).getIdentifier());
        FileOperate.outputFileUsingUsingBuffer(destFile, op + " $t2, $t0, $t1\n", true);
        store("$t2", b.getIdentifier());
    }

    public static void parseCall(CallInst callInst)
    {
        Function function = callInst.getCalledFunction();
        if (function.isLibraryFunction())
        {
            if (Objects.equals(function.getName(), "getint"))
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, """
                        li $v0, 5
                        syscall
                        """, true);
                store("$v0", callInst.getIdentifier());
            }
            else if (Objects.equals(function.getName(), "putint"))
            {
                load("$a0", callInst.getOperands().get(1).getIdentifier());
                FileOperate.outputFileUsingUsingBuffer(destFile, """
                        li $v0, 1
                        syscall
                        """, true);
            }
            else if (Objects.equals(function.getName(), "putch"))
            {
                load("$a0", callInst.getOperands().get(1).getIdentifier());
                FileOperate.outputFileUsingUsingBuffer(destFile, """
                        li $v0, 11
                        syscall
                        """, true);
            }
            else if (Objects.equals(function.getName(), "putstr"))
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, """
                        li $v0, 4
                        syscall
                        """, true);
            }
        }
        else
        {
            store("$ra", "$sp", spOff - 4);
            rec = 1;
            int argSize = callInst.getCalledFunction().getArguments().size();
            for (int i = 1; i <= argSize; i++)
            {
                rec++;
                load("$t0", callInst.getOperands().get(i).getIdentifier());
                store("$t0", "$sp", spOff - rec * 4);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, "addu $sp, $sp, " + (spOff - rec * 4) + "\n" + "jal " + function.getName() + "\n" + "addu $sp, $sp, " + (-spOff + rec * 4) + "\n", true);
            load("$ra", "$sp", spOff - 4);
            if (!(((FunctionType) function.getType()).getReturnType() instanceof VoidType))
            {
                store("$v0", callInst.getIdentifier());
            }
        }
    }

    public static void parseRet(RetInst ret)
    {
        if (!ret.getOperands().isEmpty())
        {
            load("$v0", ret.getOperands().get(0).getIdentifier());
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, "addu $sp, $sp, " + spOff + "\n", true);
    }

    public static void parseAlloca(AllocaInst allocaInst)
    {
        if (allocaInst.getAllocaType() instanceof PointerType)
        {
            PointerType pointerType = (PointerType) allocaInst.getAllocaType();
            if (pointerType.getTargetType() instanceof IntegerType)
            {
                getSp(allocaInst.getIdentifier());
            }
            else if (pointerType.getTargetType() instanceof ArrayType)
            {
                ArrayType arrayType = (ArrayType) pointerType.getTargetType();
                getSpArray(allocaInst.getIdentifier(), 4 * arrayType.getCapacity());
            }
        }
        else if (allocaInst.getAllocaType() instanceof IntegerType)
        {
            getSp(allocaInst.getIdentifier());
        }
        else if (allocaInst.getAllocaType() instanceof ArrayType)
        {
            ArrayType arrayType = (ArrayType) allocaInst.getAllocaType();
            getSpArray(allocaInst.getIdentifier(), 4 * arrayType.getCapacity());
        }
    }

    public static void parseLoad(LoadInst loadInst)
    {
        if (loadInst.getOperands().get(0) instanceof GEPInst)
        {
            load("$t0", loadInst.getOperands().get(0).getIdentifier());
            load("$t1", "$t0", 0);
            store("$t1", loadInst.getIdentifier());
        }
        else
        {
            load("$t0", loadInst.getOperands().get(0).getIdentifier());
            store("$t0", loadInst.getIdentifier());
        }
    }

    public static void parseStore(StoreInst storeInst)
    {
        if (storeInst.getOperands().get(1) instanceof GEPInst)
        {
            load("$t0", storeInst.getOperands().get(0).getIdentifier());
            load("$t1", storeInst.getOperands().get(1).getIdentifier());
            store("$t0", "$t1", 0);
        }
        else
        {
            load("$t0", storeInst.getOperands().get(0).getIdentifier());
            store("$t0", storeInst.getOperands().get(1).getIdentifier());
        }
    }

    public static void parseGEP(GEPInst gepInst)
    {
        PointerType pt = (PointerType) gepInst.getPointer().getType();
        if (pt.isString())
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "la $t0, " + gepInst.getPointer().getIdentifier() + "\n", true);
            return;
        }
        int offsetNum;
        List<Integer> dims;
        if (pt.getTargetType() instanceof ArrayType)
        {
            offsetNum = gepInst.getOperands().size() - 1;
            dims = ((ArrayType) pt.getTargetType()).getDimensions();
        }
        else
        {
            offsetNum = 1;
            dims = new ArrayList<>();
        }
        load("$t2", gepInst.getPointer().getIdentifier()); // arr
        store("$t2", gepInst.getIdentifier());
        int lastOff = 0;
        for (int i = 1; i <= offsetNum; i++)
        {
            int base = 4;
            if (pt.getTargetType() instanceof ArrayType)
            {
                for (int j = i - 1; j < dims.size(); j++)
                {
                    base *= dims.get(j);
                }
            }
            if (gepInst.getOperands().get(i) instanceof ConstInt)
            {
                int dimOff = Integer.parseInt(gepInst.getOperands().get(i).getName()) * base;
                lastOff += dimOff;
                if (i == offsetNum)
                {
                    if (lastOff == 0)
                    {
                        store("$t2", gepInst.getIdentifier());
                    }
                    else
                    {
                        FileOperate.outputFileUsingUsingBuffer(destFile, "addu $t2, $t2, " + lastOff + "\n", true);
                        // IOUtils.mips("addu $t2, $t2, " + lastOff + "\n");
                        store("$t2", gepInst.getIdentifier());
                    }
                }
            }
            else
            {
                if (lastOff != 0)
                {
                    FileOperate.outputFileUsingUsingBuffer(destFile, "addu $t2, $t2, " + lastOff + "\n", true);
                    // IOUtils.mips("addu $t2, $t2, " + lastOff + "\n");
                }
                load("$t1", gepInst.getOperands().get(i).getIdentifier()); // offset
                FileOperate.outputFileUsingUsingBuffer(destFile, "mul $t1, $t1, " + base + "\n" + "addu $t2, $t2, $t1\n", true);
                store("$t2", gepInst.getIdentifier());
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, "\n", true);
        }
    }

    public static void parseBr(BrInst brInst)
    {
        if (brInst.getOperands().size() == 3) // is CondBr
        {
            load("$t0", brInst.getOperands().get(0).getIdentifier());
            FileOperate.outputFileUsingUsingBuffer(destFile, "beqz $t0, " + "label_" + brInst.getFalseLabel().getId() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, "j " + "label_" + brInst.getTrueLabel().getId() + "\n", true);
        }
        else
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "j " + "label_" + brInst.getTrueLabel().getId() + "\n", true);
        }
    }

    public static void parseConv(ConvInst convInst)
    {
        if (convInst.getOperator() == Operator.Zext)
        {
            load("$t0", convInst.getOperands().get(0).getIdentifier());
            FileOperate.outputFileUsingUsingBuffer(destFile, """
                    sll $t0, $t0, 16
                    srl $t0, $t0, 16
                    """, true);
            store("$t0", convInst.getIdentifier());
        }
        else if (convInst.getOperator() == Operator.Bitcast)
        {
            load("$t0", convInst.getOperands().get(0).getIdentifier());
            store("$t0", convInst.getIdentifier());
        }
    }

    public static void load(String reg, String name)
    {
        if (isNumber(name))
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "li " + reg + ", " + name + "\n", true);
        }
        else
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, "lw " + reg + ", " + mem.get(name).getSecond() + "(" + mem.get(name).getFirst() + ")\n", true);
        }
    }

    public static void load(String reg, String name, int offset)
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, "lw " + reg + ", " + offset + "(" + name + ")\n", true);
    }

    public static void store(String reg, String name)
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, "sw " + reg + ", " + mem.get(name).getSecond() + "(" + mem.get(name).getFirst() + ")\n", true);
    }

    public static void store(String reg, String name, int offset)
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, "sw " + reg + ", " + offset + "(" + name + ")\n", true);
    }

    public static boolean isNumber(String str)
    {
        return str.matches("-?[0-9]+");
    }

}
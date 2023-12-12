package frontend.ir;

import frontend.ir.value.BuildFactory;
import frontend.ir.type.*;
import frontend.ir.value.*;
import frontend.ir.value.instructions.Operator;
import frontend.parser.node.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static utils.FileOperate.CreateFileUsingJava7Files;

public class LLVMGenerator
{
    public static BasicBlock curTrueBlock = null;
    public static BasicBlock curFalseBlock = null;
    public static BasicBlock continueBlock = null;
    public static BasicBlock curForFinalBlock = null;

    public static Stack<Function> functionStack = new Stack<>();
    public static Stack<BasicBlock> blockStack = new Stack<>();

    public static Integer saveVal = null;
    public static Operator saveOp = null;
    public static int tmpIndex = 0;
    public static Operator tmpOp = null;
    public static Type tmpType = null;
    public static Value tmpValue = null;
    public static List<Value> tmpList = null;
    public static List<Type> tmpTypeList = null;
    public static List<Value> funcArgsList = null;

    public static boolean isGlobal = true;
    public static boolean isConst = false;
    public static boolean isArray = false;
    public static boolean isRegister = false;

    public static Value curArray = null;
    public static String tmpName = null;
    public static int tmpDepth = 0;
    public static int tmpOffset = 0;
    public static List<Integer> tmpDims = null;
    public static int id_cnt = 0;

    private static List<Map<String, Value>> symbolTable = new ArrayList<>();

    public static Map<String, Value> getCurSymbolTable()
    {
        return symbolTable.get(symbolTable.size() - 1);
    }

    public static void addSymbol(String name, Value value)
    {
        getCurSymbolTable().put(name, value);
    }

    public void addGlobalSymbol(String name, Value value)
    {
        symbolTable.get(0).put(name, value);
    }

    public static Value getValue(String name)
    {
        for (int i = symbolTable.size() - 1; i >= 0; i--)
        {
            if (symbolTable.get(i).containsKey(name))
            {
                return symbolTable.get(i).get(name);
            }
        }
        return null;
    }

    private static List<Map<String, Integer>> constTable = new ArrayList<>();

    public static void addConst(String name, Integer value)
    {
        constTable.get(constTable.size() - 1).put(name, value);
    }

    public static Integer getConst(String name)
    {
        for (int i = constTable.size() - 1; i >= 0; i--)
        {
            if (constTable.get(i).containsKey(name))
            {
                return constTable.get(i).get(name);
            }
        }
        return 0;
    }

    private List<String> stringList = new ArrayList<>();

    private int getStringIndex(String str)
    {
        for (int i = 0; i < stringList.size(); i++)
        {
            if (stringList.get(i).equals(str))
            {
                return i;
            }
        }
        stringList.add(str);
        Type type = BuildFactory.getArrayType(IntegerType.i8, str.length() + 1);
        Value value = BuildFactory.getGlobalVar(getStringName(str), type, true, BuildFactory.getConstString(str));
        addGlobalSymbol(getStringName(str), value);
        return stringList.size() - 1;
    }

    private String getStringName(int index)
    {
        return "_str_" + index;
    }

    private String getStringName(String str)
    {
        return getStringName(getStringIndex(str));
    }

    public static void addSymbolAndConstTable()
    {
        symbolTable.add(new HashMap<>());
        constTable.add(new HashMap<>());
    }

    public static void removeSymbolAndConstTable()
    {
        symbolTable.remove(symbolTable.size() - 1);
        constTable.remove(constTable.size() - 1);
    }


    public static void run()
    {
        addSymbolAndConstTable();
        addSymbol("getint", BuildFactory.getLibraryFunction("getint", IntegerType.i32, new ArrayList<>()));
        addSymbol("putint", BuildFactory.getLibraryFunction("putint", VoidType.voidType, new ArrayList<>(Collections.singleton(IntegerType.i32))));
        addSymbol("putch", BuildFactory.getLibraryFunction("putch", VoidType.voidType, new ArrayList<>(Collections.singleton(IntegerType.i32))));
        addSymbol("putstr", BuildFactory.getLibraryFunction("putstr", VoidType.voidType, new ArrayList<>(Collections.singleton(new PointerType(IntegerType.i8)))));
        CompUnitNode.getInstance().parseIR();
    }

    public static void outputLLVM() throws IOException
    {
        File destFile = new File("llvm_ir.ll");
        CreateFileUsingJava7Files(destFile);
        IRModule.getInstance().outputLLVM(destFile);
    }





}
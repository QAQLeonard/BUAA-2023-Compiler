package ir.value;

import ir.type.ArrayType;
import ir.type.FunctionType;
import ir.type.Type;
import ir.value.instructions.BinaryInst;
import ir.value.instructions.ConvInst;
import ir.value.instructions.Operator;
import ir.value.instructions.mem.*;
import ir.value.instructions.terminator.BrInst;
import ir.value.instructions.terminator.CallInst;
import ir.value.instructions.terminator.RetInst;

import java.util.List;

public class BuildFactory
{

    public BuildFactory()
    {
    }


    /**
     * Functions
     **/
    public static Function getFunction(String name, Type retType, List<Type> parametersTypes)
    {
        return new Function(name, new FunctionType(retType, parametersTypes), false);
    }

    public static Function getLibraryFunction(String name, Type retType, List<Type> parametersTypes)
    {
        return new Function(name, new FunctionType(retType, parametersTypes), true);
    }


    public static List<Value> getFunctionArguments(Function function)
    {
        return function.getArguments();
    }

    /**
     * BasicBlock
     */
    public static BasicBlock buildBasicBlock(Function function)
    {
        return new BasicBlock(function);
    }



    /**
     * BinaryInst
     **/
    public static BinaryInst getBinaryInst(BasicBlock basicBlock, Operator op, Value left, Value right)
    {
        BinaryInst tmp = new BinaryInst(basicBlock, op, left, right);
        if (op == Operator.And || op == Operator.Or)
        {
            tmp = getBinaryInst(basicBlock, Operator.Ne, tmp, ConstInt.ZERO);
        }
        basicBlock.addInst(tmp);
        return tmp;
    }

    public static BinaryInst buildNot(BasicBlock basicBlock, Value value)
    {
        return getBinaryInst(basicBlock, Operator.Eq, value, ConstInt.ZERO);
    }

    /**
     * Var
     */
    public static GlobalVar getGlobalVar(String name, Type type, boolean isConst, Value value)
    {
        return new GlobalVar(name, type, isConst, value);
    }

    public static AllocaInst buildVar(BasicBlock basicBlock, Value value, boolean isConst, Type allocaType)
    {
        AllocaInst tmp = new AllocaInst(isConst, allocaType);
        basicBlock.addInst(tmp);
        if (value != null)
        {
            getStoreInst(basicBlock, tmp, value);
        }
        return tmp;
    }

    public static ConstInt getConstInt(int value)
    {
        return new ConstInt(value);
    }

    public static ConstString getConstString(String value)
    {
        return new ConstString(value);
    }

    /**
     * Array
     */
    public static GlobalVar getGlobalArray(String name, Type type, boolean isConst)
    {
        Value tmp = new ConstArray(type, ((ArrayType) type).getElementType(), ((ArrayType) type).getCapacity());
        return new GlobalVar(name, type, isConst, tmp);
    }

    public static AllocaInst buildArray(BasicBlock basicBlock, boolean isConst, Type arrayType)
    {
        AllocaInst tmp = new AllocaInst(isConst, arrayType);
        basicBlock.addInst(tmp);
        return tmp;
    }

    public static void buildInitArray(Value array, int index, Value value)
    {
        ((ConstArray) ((GlobalVar) array).getValue()).storeValue(index, value);
    }

    public static ArrayType getArrayType(Type elementType, int length)
    {
        return new ArrayType(elementType, length);
    }

    /**
     * ConvInst
     */
    public static Value buildZext(Value value, BasicBlock basicBlock)
    {
        if (value instanceof ConstInt)
        {
            return new ConstInt(((ConstInt) value).getValue());
        }
        ConvInst tmp = new ConvInst(basicBlock, Operator.Zext, value);
        basicBlock.addInst(tmp);
        return tmp;
    }

    public static BinaryInst buildConvToI1(Value val, BasicBlock basicBlock)
    {
        BinaryInst tmp = new BinaryInst(basicBlock, Operator.Ne, val, getConstInt(0));
        basicBlock.addInst(tmp);
        return tmp;
    }

    /**
     * MemInst
     */
    public static LoadInst getLoadInst(BasicBlock basicBlock, Value pointer)
    {
        LoadInst tmp = new LoadInst(pointer);
        basicBlock.addInst(tmp);
        return tmp;
    }

    public static StoreInst getStoreInst(BasicBlock basicBlock, Value ptr, Value value)
    {
        StoreInst tmp = new StoreInst(ptr, value);
        basicBlock.addInst(tmp);
        return tmp;
    }

    public static GEPInst getGEPInst(BasicBlock basicBlock, Value pointer, List<Value> indices)
    {
        GEPInst tmp = new GEPInst(pointer, indices);
        basicBlock.addInst(tmp);
        return tmp;
    }

    public static GEPInst getGEPInst(BasicBlock basicBlock, Value pointer, int offset)
    {
        GEPInst tmp = new GEPInst(pointer, offset);
        basicBlock.addInst(tmp);
        return tmp;
    }

    /**
     * TerminatorInst
     */
    public static void buildBrInst(BasicBlock basicBlock, BasicBlock trueBlock)
    {
        BrInst tmp = new BrInst(trueBlock);
        if (basicBlock != null)
        {
            if (basicBlock.getInstructionList().getTail() == null || (!(basicBlock.getInstructionList().getTail().getValue() instanceof BrInst) && !(basicBlock.getInstructionList().getTail().getValue() instanceof RetInst)))
            {
                basicBlock.addSuccessor(trueBlock);
                trueBlock.addSuccessor(basicBlock);
            }
            basicBlock.addInst(tmp);
        }
    }

    public static void buildBrInst(BasicBlock basicBlock, Value cond, BasicBlock trueBlock, BasicBlock falseBlock)
    {
        BrInst tmp = new BrInst(basicBlock, cond, trueBlock, falseBlock);
        basicBlock.addInst(tmp);
    }

    public static CallInst getCallInst(BasicBlock basicBlock, Function function, List<Value> args)
    {
        CallInst tmp = new CallInst(basicBlock, function, args);
        basicBlock.addInst(tmp);
        return tmp;
    }


    public static void buildRetInst(BasicBlock basicBlock, Value ret)
    {
        RetInst tmp = new RetInst(ret);
        basicBlock.addInst(tmp);
    }

}

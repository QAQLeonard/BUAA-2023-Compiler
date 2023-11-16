package ir.value;

import ir.type.ArrayType;
import ir.type.FunctionType;
import ir.type.IntegerType;
import ir.type.Type;
import ir.value.instructions.BinaryInst;
import ir.value.instructions.ConstArray;
import ir.value.instructions.ConvInst;
import ir.value.instructions.Operator;
import ir.value.instructions.mem.*;
import ir.value.instructions.terminator.BrInst;
import ir.value.instructions.terminator.CallInst;
import ir.value.instructions.terminator.RetInst;

import java.util.List;

public class BuildFactory {

    private static final BuildFactory buildFactory = new BuildFactory();

    public BuildFactory() {
    }

    public static BuildFactory getInstance() {
        return buildFactory;
    }

    /**
     * Functions
     **/
    public static Function getFunction(String name, Type retType, List<Type> parametersTypes) {
        return new Function(name, new FunctionType(retType, parametersTypes), false);
    }

    public static Function buildLibraryFunction(String name, Type retType, List<Type> parametersTypes) {
        return new Function(name, new FunctionType(retType, parametersTypes), true);
    }


    public static List<Value> getFunctionArguments(Function function) {
        return function.getArguments();
    }

    /**
     * BasicBlock
     */
    public static BasicBlock buildBasicBlock(Function function) {
        return new BasicBlock(function);
    }

    public static void checkBlockEnd(BasicBlock basicBlock) {
        Type retType = ((FunctionType) basicBlock.getNode().getParent().getValue().getType()).getReturnType();
        if (!basicBlock.getInstructions().isEmpty()) {
            Value lastInst = basicBlock.getInstructions().getEnd().getValue();
            if (lastInst instanceof RetInst || lastInst instanceof BrInst) {
                return;
            }
        }
        if (retType instanceof IntegerType) {
            buildRet(basicBlock, ConstInt.ZERO);
        } else {
            buildRet(basicBlock, null);
        }
    }

    /**
     * BinaryInst
     **/
    public static BinaryInst buildBinary(BasicBlock basicBlock, Operator op, Value left, Value right) {
        BinaryInst tmp = new BinaryInst(basicBlock, op, left, right);
        if (op == Operator.And || op == Operator.Or) {
            tmp = buildBinary(basicBlock, Operator.Ne, tmp, ConstInt.ZERO);
        }
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static BinaryInst buildNot(BasicBlock basicBlock, Value value) {
        return buildBinary(basicBlock, Operator.Eq, value, ConstInt.ZERO);
    }

    /**
     * Var
     */
    public static GlobalVar buildGlobalVar(String name, Type type, boolean isConst, Value value) {
        return new GlobalVar(name, type, isConst, value);
    }

    public static AllocaInst buildVar(BasicBlock basicBlock, Value value, boolean isConst, Type allocaType) {
        AllocaInst tmp = new AllocaInst(basicBlock, isConst, allocaType);
        tmp.addInstToBlock(basicBlock);
        if (value != null) {
            buildStore(basicBlock, tmp, value);
        }
        return tmp;
    }

    public static ConstInt getConstInt(int value) {
        return new ConstInt(value);
    }

    public static ConstString getConstString(String value) {
        return new ConstString(value);
    }

    /**
     * Array
     */
    public static GlobalVar buildGlobalArray(String name, Type type, boolean isConst) {
        Value tmp = new ConstArray(type, ((ArrayType) type).getElementType(), ((ArrayType) type).getCapacity());
        return new GlobalVar(name, type, isConst, tmp);
    }

    public static AllocaInst buildArray(BasicBlock basicBlock, boolean isConst, Type arrayType) {
        AllocaInst tmp = new AllocaInst(basicBlock, isConst, arrayType);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static void buildInitArray(Value array, int index, Value value) {
        ((ConstArray) ((GlobalVar) array).getValue()).storeValue(index, value);
    }

    public static ArrayType getArrayType(Type elementType, int length) {
        return new ArrayType(elementType, length);
    }

    /**
     * ConvInst
     */
    public Value buildZext(Value value, BasicBlock basicBlock) {
        if (value instanceof ConstInt) {
            return new ConstInt(((ConstInt) value).getValue());
        }
        ConvInst tmp = new ConvInst(basicBlock, Operator.Zext, value);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public ConvInst buildBitcast(Value value, BasicBlock basicBlock) {
        ConvInst tmp = new ConvInst(basicBlock, Operator.Bitcast, value);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public BinaryInst buildConvToI1(Value val, BasicBlock basicBlock) {
        BinaryInst tmp = new BinaryInst(basicBlock, Operator.Ne, val, getConstInt(0));
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    /**
     * MemInst
     */
    public static LoadInst buildLoad(BasicBlock basicBlock, Value pointer) {
        LoadInst tmp = new LoadInst(basicBlock, pointer);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static StoreInst buildStore(BasicBlock basicBlock, Value ptr, Value value) {
        StoreInst tmp = new StoreInst(basicBlock, ptr, value);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static GEPInst buildGEP(BasicBlock basicBlock, Value pointer, List<Value> indices) {
        GEPInst tmp = new GEPInst(basicBlock, pointer, indices);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static GEPInst buildGEP(BasicBlock basicBlock, Value pointer, int offset) {
        GEPInst tmp = new GEPInst(basicBlock, pointer, offset);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public PhiInst buildPhi(BasicBlock basicBlock, Type type, List<Value> in) {
        PhiInst tmp = new PhiInst(basicBlock, type, in);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    /**
     * TerminatorInst
     */
    public static BrInst buildBr(BasicBlock basicBlock, BasicBlock trueBlock) {
        BrInst tmp = new BrInst(basicBlock, trueBlock);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static BrInst buildBr(BasicBlock basicBlock, Value cond, BasicBlock trueBlock, BasicBlock falseBlock) {
        BrInst tmp = new BrInst(basicBlock, cond, trueBlock, falseBlock);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }

    public static CallInst buildCall(BasicBlock basicBlock, Function function, List<Value> args) {
        CallInst tmp = new CallInst(basicBlock, function, args);
        tmp.addInstToBlock(basicBlock);
        return tmp;
    }


    public static RetInst buildRet(BasicBlock basicBlock, Value ret) {
        RetInst tmp = new RetInst(basicBlock, ret);
        tmp.addInstToBlock(basicBlock);

        return tmp;
    }

}

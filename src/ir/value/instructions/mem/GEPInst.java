package ir.value.instructions.mem;

import ir.type.ArrayType;
import ir.type.IntegerType;
import ir.type.PointerType;
import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.GlobalVar;
import ir.value.Value;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

import java.util.List;

public class GEPInst extends Instruction
{
    Type elementType;
    Value target;

    public GEPInst(BasicBlock basicBlock, Value pointer, List<Value> indices)
    {
        super(new PointerType(getElementType(pointer, indices)), Operator.GEP, basicBlock);
        this.setName("%" + REG_NUMBER++);
        if (pointer instanceof GEPInst)
        {
            target = ((GEPInst) pointer).target;
        }
        else if (pointer instanceof AllocaInst || pointer instanceof GlobalVar)
        {
            target = pointer;
        }
        this.addOperand(pointer);
        for (Value value : indices)
        {
            this.addOperand(value);
        }
        this.elementType = getElementType(pointer, indices);
        this.addInstToBlock(basicBlock);
    }

    public GEPInst(BasicBlock basicBlock, Value pointer, int offset)
    {
        this(basicBlock, pointer, ((ArrayType) ((PointerType) pointer.getType()).getTargetType()).offset2Index(offset));
    }

    public Value getPointer()
    {
        return getOperands().get(0);
    }

    private static Type getElementType(Value pointer, List<Value> indices)
    {
        Type type = pointer.getType();
        for (Value ignored : indices)
        {
            if (type instanceof ArrayType)
            {
                type = ((ArrayType) type).getElementType();
            }
            else if (type instanceof PointerType)
            {
                type = ((PointerType) type).getTargetType();
            }
            else
            {
                break;
            }
        }
        return type;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(getName()).append(" = getelementptr ");
        // 如果是字符串，需要加 inbounds
        if (getPointer().getType() instanceof PointerType && ((PointerType) getPointer().getType()).getTargetType() instanceof ArrayType && ((ArrayType) ((PointerType) getPointer().getType()).getTargetType()).getElementType() instanceof IntegerType && ((IntegerType) ((ArrayType) ((PointerType) getPointer().getType()).getTargetType()).getElementType()).isI8())
        {
            s.append("inbounds ");
        }
        s.append(((PointerType) getPointer().getType()).getTargetType()).append(", ");
        for (int i = 0; i < getOperands().size(); i++)
        {
            if (i == 0)
            {
                s.append(getPointer().getType()).append(" ").append(getPointer().getName());
            }
            else
            {
                s.append(", ").append(getOperands().get(i).getType()).append(" ").append(getOperands().get(i).getName());
            }
        }
        return s.toString();
    }
}

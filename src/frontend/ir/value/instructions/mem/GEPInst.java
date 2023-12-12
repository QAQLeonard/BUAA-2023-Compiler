package frontend.ir.value.instructions.mem;

import frontend.ir.type.ArrayType;
import frontend.ir.type.IntegerType;
import frontend.ir.type.PointerType;
import frontend.ir.type.Type;
import frontend.ir.value.GlobalVar;
import frontend.ir.value.Value;
import frontend.ir.value.instructions.Instruction;
import frontend.ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GEPInst extends Instruction
{
    Type elementType;
    Value target;

    public GEPInst(Value pointer, List<Value> indices)
    {
        super(new PointerType(getElementType(pointer, indices)), Operator.GEP);
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
    }

    public GEPInst(Value pointer, int offset)
    {
        this(pointer, ((ArrayType) ((PointerType) pointer.getType()).getTargetType()).offset2Index(offset));
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
    public void output(File destFile) throws IOException
    {
        StringBuilder s = new StringBuilder();
        s.append(getName()).append(" = getelementptr ");

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
        FileOperate.outputFileUsingUsingBuffer(destFile, s.toString(), true);
    }
}

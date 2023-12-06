package ir.type;

import ir.value.ConstInt;
import ir.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ArrayType implements Type
{
    final Type elementType;
    final int length;

    public ArrayType(Type elementType, int length)
    {
        this.elementType = elementType;
        this.length = length;
    }

    public Type getElementType()
    {
        return elementType;
    }

    public int getLength()
    {
        return length;
    }

    public List<Integer> getDimensions()
    {
        List<Integer> dimensions = new ArrayList<>();
        for (Type type = this; type instanceof ArrayType; type = ((ArrayType) type).getElementType())
        {
            dimensions.add(((ArrayType) type).getLength());
        }
        return dimensions;
    }

    public int getCapacity()
    {
        int capacity = 1;
        for (int dimension : getDimensions())
        {
            capacity *= dimension;
        }
        return capacity;
    }

    public List<Value> offset2Index(int offset)
    {
        List<Value> index = new ArrayList<>();
        Type type = this;
        while (type instanceof ArrayType)
        {
            index.add(new ConstInt(offset / ((ArrayType) type).getCapacity()));
            offset %= ((ArrayType) type).getCapacity();
            type = ((ArrayType) type).getElementType();
        }
        index.add(new ConstInt(offset));
        return index;
    }

    public int index2Offset(List<Integer> index)
    {
        int offset = 0, i = 0;
        Type type = this;
        offset += index.get(i++) * ((ArrayType) type).getCapacity();
        while (type instanceof ArrayType)
        {
            type = ((ArrayType) type).getElementType();
            if (type instanceof ArrayType)
            {
                offset += index.get(i++) * ((ArrayType) type).getCapacity();
            }
            else
            {
                offset += index.get(i++);
            }
        }
        return offset;
    }

    @Override
    public String toString()
    {
        return "[" + length + " x " + elementType.toString() + "]";
    }

    public boolean isString()
    {
        return elementType instanceof IntegerType && ((IntegerType) elementType).isI8();
    }
}

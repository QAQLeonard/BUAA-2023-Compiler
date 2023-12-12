package ir.value;

import ir.type.ArrayType;
import ir.type.Type;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConstArray extends Const
{
    Type elementType;
    List<Value> array;
    public int capacity;
    public boolean init = false;

    public ConstArray(Type type, Type elementType, int capacity)
    {
        super("", type);
        this.elementType = elementType;
        this.array = new ArrayList<>();
        this.capacity = capacity;
        for (int i = 0; i < ((ArrayType) type).getLength(); i++)
        {
            if (elementType instanceof ArrayType)
                array.add(new ConstArray(elementType, ((ArrayType) elementType).getElementType(), ((ArrayType) elementType).getCapacity()));
            else array.add(ConstInt.ZERO);
        }
    }

    public ArrayList<Value> get1DArray()
    {
        ArrayList<Value> result = new ArrayList<>();
        for (Value value : array)
        {
            if (value instanceof ConstArray)
            {
                result.addAll(((ConstArray) value).get1DArray());
            }
            else
            {
                result.add(value);
            }
        }
        return result;
    }

    public void storeValue(int offset, Value value)
    {
        // recursion
        if (elementType instanceof ArrayType)
        {
            ((ConstArray) (array.get(offset / ((ArrayType) elementType).getCapacity()))).storeValue(offset % ((ArrayType) elementType).getCapacity(), value);
        }
        else
        {
            array.set(offset, value);
        }
    }

    public boolean allZero()
    {
        for (Value value : array)
        {
            if (value instanceof ConstInt)
            {
                if (((ConstInt) value).getValue() != 0)
                {
                    return false;
                }
            }
            else
            {
                if (!((ConstArray) value).allZero())
                {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void output(File destFile) throws IOException
    {
        if (allZero())
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.getType().toString() + " " + "zeroinitializer", true);
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getType().toString()).append(" ").append("[");
            for (int i = 0; i < array.size(); i++)
            {
                if (i != 0)
                {
                    sb.append(", ");
                }
                sb.append(array.get(i).toString());
            }
            sb.append("]");
            FileOperate.outputFileUsingUsingBuffer(destFile, sb.toString(), true);
        }
    }
}

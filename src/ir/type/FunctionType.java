package ir.type;

import java.util.ArrayList;
import java.util.List;

public class FunctionType implements Type
{
    List<Type> parametersType;
    Type returnType;

    public FunctionType(Type returnType, List<Type> parametersType)
    {
        this.returnType = returnType;
        this.parametersType = parametersType;
        processArrayType();
    }

    public List<Type> getParametersType()
    {
        return parametersType;
    }


    public Type getReturnType()
    {
        return returnType;
    }


    private void processArrayType()
    {
        List<Integer> target = new ArrayList<>();
        for (Type type : parametersType)
        {
            if (type instanceof ArrayType)
            {
                if (((ArrayType) type).getLength() == -1)
                {
                    target.add(parametersType.indexOf(type));
                }
            }
        }
        for (int index : target)
        {
            parametersType.set(index, new PointerType(((ArrayType) parametersType.get(index)).getElementType()));
        }
    }
}

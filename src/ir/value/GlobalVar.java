package ir.value;

import ir.IRModule;
import ir.type.PointerType;
import ir.type.Type;

public class GlobalVar extends User
{

    boolean isConst;
    Value value;

    public GlobalVar(String name, Type type, boolean isConst, Value value)
    {
        // GlobalVar store as a PointerType in llvm-ir
        super("@" + name, new PointerType(type));
        this.isConst = isConst;
        this.value = value;
        IRModule.getInstance().addGlobalVar(this);
    }

    public boolean isConst()
    {
        return isConst;
    }

    public Value getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" = ");
        if (isConst)
        {
            sb.append("constant ");
        }
        else
        {
            sb.append("global ");
        }
        if (value != null)
        {
            sb.append(value);
        }
        return sb.toString();
    }
}

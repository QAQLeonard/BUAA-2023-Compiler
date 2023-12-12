package frontend.ir.value;

import frontend.ir.IRModule;
import frontend.ir.type.PointerType;
import frontend.ir.type.Type;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class GlobalVar extends User
{

    boolean isConst;
    Value value;

    public GlobalVar(String name, Type type, boolean isConst, Value value)
    {
        // GlobalVar store as a PointerType in llvm-frontend.ir
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
    public void output(File destFile) throws IOException
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
        FileOperate.outputFileUsingUsingBuffer(destFile, sb.toString()+"\n", true);
    }
}

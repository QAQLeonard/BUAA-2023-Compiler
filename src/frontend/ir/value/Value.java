package frontend.ir.value;

import frontend.ir.LLVMGenerator;
import frontend.ir.type.Type;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Value
{
    String name;
    Type type;
    String id;
    ArrayList<Use> useList; // def-use
    public static int REG_NUMBER = 0;

    public Value(String name, Type type)
    {
        this.name = name;
        this.type = type;
        this.useList = new ArrayList<>();
        id = "identifier" + (++LLVMGenerator.id_cnt);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    /**
     * Remove all use used by the user
     * @param user
     */
    public void removeUseByUser(User user)
    {
        useList.removeIf(use -> use.getUser() == user);
    }

    @Override
    public String toString()
    {
        return type.toString() + " " + name;
    }

    public void output(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, type.toString() + " " + name, true);
    }

    public String getIdentifier()
    {
        if (this instanceof ConstInt) return name;
        if (name.startsWith("@")) return name.replaceAll("@", "");
        return name + "_" + id;
    }
}

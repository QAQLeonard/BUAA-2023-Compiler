package ir.value;

import ir.IRModule;
import ir.type.Type;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Value
{
    String name;
    Type type;
    ArrayList<Use> useList; // def-use
    public static int REG_NUMBER = 0;

    public Value(String name, Type type)
    {
        this.name = name;
        this.type = type;
        this.useList = new ArrayList<>();
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

    public void outputIR(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, type.toString() + " " + name, true);
    }
}

package ir.value;

import ir.type.Type;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User extends Value
{
    List<Value> operands;

    public User(String name, Type type)
    {
        super(name, type);
        this.operands = new ArrayList<>();
    }

    public List<Value> getOperands()
    {
        return operands;
    }

    public void addOperand(Value operand)
    {
        this.operands.add(operand);
        if (operand != null)
        {
            operand.useList.add(new Use(operand, this));
        }
    }

    public void removeUseFromOperands()
    {
        if (operands == null)
        {
            return;
        }
        for (Value operand : operands)
        {
            if (operand != null)
            {
                operand.removeUseByUser(this);
            }
        }
    }

    public void outputIR(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, type.toString() + " " + name, true);
    }

}

package ir.value;

import ir.type.Type;

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
            operand.useList.add(new Use(operand, this, operands.size() - 1));
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

}

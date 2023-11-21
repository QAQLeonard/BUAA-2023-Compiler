package ir.value;

/**
 * user,value对应起来
 */
public class Use
{
    private Value user;
    private Value value;
    private int posOfOperand; // 在 OperandList 中的位置

    public Use(Value user, Value value, int posOfOperand)
    {
        this.user = user;
        this.value = value;
        this.posOfOperand = posOfOperand;
    }

    public Value getUser()
    {
        return user;
    }


    public Value getValue()
    {
        return value;
    }

    public void setValue(Value value)
    {
        this.value = value;
    }

}

package ir.value;

/**
 * user,value对应起来
 */
public class Use
{
    Value user;
    Value value;

    public Use(Value user, Value value)
    {
        this.user = user;
        this.value = value;
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

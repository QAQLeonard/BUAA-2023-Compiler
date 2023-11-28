package ir.value;

import ir.type.Type;

import java.io.File;
import java.io.IOException;

public abstract class Const extends Value
{
    public Const(String name, Type type)
    {
        super(name, type);
    }

}

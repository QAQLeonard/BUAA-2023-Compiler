package ir.value;

import ir.type.IntegerType;
import ir.type.PointerType;

public class ConstString extends Const
{
    String value;
    int length;

    public ConstString(String value)
    {
        // String store as a PointerType in llvm-ir
        super("\"" + value.replace("\n", "\\n") + "\"", new PointerType(IntegerType.i8));
        this.length = value.length() + 1;
        this.value = value.replace("\n", "\\0a") + "\\00";
    }

    @Override
    public String toString()
    {
        return "[" + length + " x " + ((PointerType) getType()).getTargetType() + "] c\"" + value + "\"";
    }
}

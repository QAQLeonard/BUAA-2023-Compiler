package ir.value;

import ir.type.IntegerType;
import ir.type.PointerType;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

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
    public void outputIR(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, "[" + length + " x " + ((PointerType) getType()).getTargetType() + "] c\"" + value + "\"", true);
    }
}

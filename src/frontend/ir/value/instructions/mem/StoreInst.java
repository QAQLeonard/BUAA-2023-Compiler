package frontend.ir.value.instructions.mem;

import frontend.ir.value.Value;
import frontend.ir.value.instructions.Instruction;
import frontend.ir.value.instructions.Operator;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

public class StoreInst extends Instruction
{
    public StoreInst(Value pointer, Value value)
    {
        super(value.getType(), Operator.Store);
        this.addOperand(value);
        this.addOperand(pointer);
    }

    @Override
    public void output(File destFile) throws IOException
    {
        StringBuilder s = new StringBuilder();
        s.append("store ").append(getOperands().get(0).getType()).append(" ").append(getOperands().get(0).getName()).append(", ").append(getOperands().get(1).getType()).append(" ").append(getOperands().get(1).getName());
        FileOperate.outputFileUsingUsingBuffer(destFile, s.toString(), true);
    }
}

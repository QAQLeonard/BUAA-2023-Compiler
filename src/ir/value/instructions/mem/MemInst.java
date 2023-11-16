package ir.value.instructions.mem;

import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

public abstract class MemInst extends Instruction {
    public MemInst(Type type, Operator op, BasicBlock basicBlock) {
        super(type, op, basicBlock);
    }
}

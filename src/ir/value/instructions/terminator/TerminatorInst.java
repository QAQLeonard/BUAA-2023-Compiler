package ir.value.instructions.terminator;

import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.instructions.Instruction;
import ir.value.instructions.Operator;

public abstract class TerminatorInst extends Instruction {
    public TerminatorInst(Type type, Operator op, BasicBlock basicBlock) {
        super(type, op, basicBlock);
    }
}

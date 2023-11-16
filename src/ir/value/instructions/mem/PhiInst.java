package ir.value.instructions.mem;

import ir.type.Type;
import ir.value.BasicBlock;
import ir.value.Value;
import ir.value.instructions.Operator;

import java.util.List;

public class PhiInst extends MemInst {
    public PhiInst(BasicBlock basicBlock, Type type, List<Value> values) {
        super(type, Operator.Phi, basicBlock);
        for (Value value : values) {
            this.addOperand(value);
        }
        this.setName("%" + REG_NUMBER++);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(getName()).append(" = phi ").append(getType()).append(" ");
        for (int i = 0; i < getOperands().size(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            s.append("[ ").append(getOperands().get(i).getName()).append(", %").append(getNode().getParent().getValue().getPredecessors().get(i).getName()).append(" ]");
        }
        return s.toString();
    }
}

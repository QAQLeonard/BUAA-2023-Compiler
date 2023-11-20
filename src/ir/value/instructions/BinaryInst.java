package ir.value.instructions;

import ir.type.IntegerType;
import ir.type.VoidType;
import ir.value.BasicBlock;
import ir.value.BuildFactory;
import ir.value.Value;

import java.util.EnumMap;
import java.util.Map;

public class BinaryInst extends Instruction {

    public BinaryInst(BasicBlock basicBlock, Operator op, Value left, Value right) {
        super(VoidType.voidType, op, basicBlock);
        boolean isLeftI1 = left.getType() instanceof IntegerType && ((IntegerType) left.getType()).isI1();
        boolean isRightI1 = right.getType() instanceof IntegerType && ((IntegerType) right.getType()).isI1();
        boolean isLeftI32 = left.getType() instanceof IntegerType && ((IntegerType) left.getType()).isI32();
        boolean isRightI32 = right.getType() instanceof IntegerType && ((IntegerType) right.getType()).isI32();
        if (isLeftI1 && isRightI32) {
            this.addOperand(BuildFactory.buildZext(left, basicBlock));
            this.addOperand(right);
        } else if (isLeftI32 && isRightI1) {
            this.addOperand(left);
            this.addOperand(BuildFactory.buildZext(right, basicBlock));
        } else {
            this.addOperand(left);
            this.addOperand(right);
        }
        this.setType(this.getOperands().get(0).getType());
        if (isCond()) {
            this.setType(IntegerType.i1);
        }
        this.setName("%" + REG_NUMBER++);
    }


    public boolean isCond() {
        // return this.isLt() || this.isLe() || this.isGe() || this.isGt() || this.isEq() || this.isNe();
        Operator temp =  this.getOperator();
        return temp == Operator.Lt || temp == Operator.Le || temp == Operator.Ge || temp == Operator.Gt || temp == Operator.Eq || temp == Operator.Ne;
    }

    private static final Map<Operator, String> OPERATOR_IR_MAP = new EnumMap<>(Operator.class);

    static {
        OPERATOR_IR_MAP.put(Operator.Add, "add i32 ");
        OPERATOR_IR_MAP.put(Operator.Sub, "sub i32 ");
        OPERATOR_IR_MAP.put(Operator.Mul, "mul i32 ");
        OPERATOR_IR_MAP.put(Operator.Div, "sdiv i32 ");
        OPERATOR_IR_MAP.put(Operator.Mod, "srem i32 ");
        OPERATOR_IR_MAP.put(Operator.Shl, "shl i32 ");
        OPERATOR_IR_MAP.put(Operator.Shr, "ashr i32 ");
        OPERATOR_IR_MAP.put(Operator.And, "and ");
        OPERATOR_IR_MAP.put(Operator.Or, "or ");
        // 逻辑比较操作符使用 icmp 指令
        OPERATOR_IR_MAP.put(Operator.Lt, "icmp slt ");
        OPERATOR_IR_MAP.put(Operator.Le, "icmp sle ");
        OPERATOR_IR_MAP.put(Operator.Ge, "icmp sge ");
        OPERATOR_IR_MAP.put(Operator.Gt, "icmp sgt ");
        OPERATOR_IR_MAP.put(Operator.Eq, "icmp eq ");
        OPERATOR_IR_MAP.put(Operator.Ne, "icmp ne ");
    }


    @Override
    public String toString() {
        String operatorIR = OPERATOR_IR_MAP.getOrDefault(this.getOperator(), "");
        String typeStr = this.getOperands().get(0).getType().toString();

        // 对于 And 和 Or 操作，类型是操作数的类型
        if (this.getOperator() == Operator.And || this.getOperator() == Operator.Or) {
            operatorIR += typeStr + " ";
        }

        // 逻辑比较操作也需要操作数的类型
        if (this.isCond()) {
            operatorIR += typeStr + " ";
        }

        return getName() + " = " + operatorIR + this.getOperands().get(0).getName() + ", " + this.getOperands().get(1).getName();
    }
}

package ir.value.instructions;

import ir.type.ArrayType;
import ir.type.Type;
import ir.value.Const;
import ir.value.ConstInt;
import ir.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ConstArray extends Const {
    // 高维数组应该往下递归
    private Type elementType;
    private List<Value> array;
    private int capacity;

    public ConstArray(Type type, Type elementType, int capacity) {
        super("", type);
        this.elementType = elementType;
        this.array = new ArrayList<>();
        this.capacity = capacity;
        if (elementType instanceof ArrayType) {
            for (int i = 0; i < ((ArrayType) type).getLength(); i++) {
                array.add(new ConstArray(elementType, ((ArrayType) elementType).getElementType(), ((ArrayType) elementType).getCapacity()));
            }
        } else {
            for (int i = 0; i < ((ArrayType) type).getLength(); i++) {
                array.add(ConstInt.ZERO);
            }
        }
    }

    public Type getElementType() {
        return elementType;
    }

    public void setElementType(Type elementType) {
        this.elementType = elementType;
    }

    public List<Value> getArray() {
        return array;
    }

    public void setArray(List<Value> array) {
        this.array = array;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Value> get1DArray() {
        List<Value> result = new ArrayList<>();
        for (Value value : array) {
            if (value instanceof ConstArray) {
                result.addAll(((ConstArray) value).get1DArray());
            } else {
                result.add(value);
            }
        }
        return result;
    }

    public void storeValue(int offset, Value value) {
        // recursion
        if (elementType instanceof ArrayType) {
            ((ConstArray) (array.get(offset / ((ArrayType) elementType).getCapacity()))).storeValue(offset % ((ArrayType) elementType).getCapacity(), value);
        } else {
            array.set(offset, value);
        }
    }

    private boolean allZero() {
        for (Value value : array) {
            if (value instanceof ConstInt) {
                if (((ConstInt) value).getValue() != 0) {
                    return false;
                }
            } else {
                if (!((ConstArray) value).allZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (allZero()) {
            return this.getType().toString() + " " + "zeroinitializer";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getType().toString()).append(" ").append("[");
            for (int i = 0; i < array.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(array.get(i).toString());
            }
            sb.append("]");
            return sb.toString();
        }
    }
}

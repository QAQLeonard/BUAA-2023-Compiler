package ir.value;

import ir.type.Type;

public class Argument extends Value {
    private int index;

    public Argument(Type type, int index, boolean isLibraryFunction) {
        super(isLibraryFunction ? "" : "%" + REG_NUMBER++, type);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return this.getType().toString() + " " + this.getName();
    }

}

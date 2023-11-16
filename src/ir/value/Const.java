package ir.value;

import ir.type.Type;

public abstract class Const extends Value {
    public Const(String name, Type type) {
        super(name, type);
    }
}

package org.avmframework.variable;

public class IntegerVariable extends AtomicVariable {

    public IntegerVariable(int value) {
        super(value);
    }

    public IntegerVariable(int value, int min, int max) {
        super(value, min, max);
    }

    public IntegerVariable(int value, int min, int max, int step, int accelerationBase) {
        super(value, min, max, step, accelerationBase);
    }
}

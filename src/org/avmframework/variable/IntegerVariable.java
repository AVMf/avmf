package org.avmframework.variable;

public class IntegerVariable extends AtomicVariable {

    public IntegerVariable(int initialValue) {
        super(initialValue);
    }

    public IntegerVariable(int initialValue, int min, int max) {
        super(initialValue, min, max);
    }

    public IntegerVariable(int initialValue, int min, int max, int step, int accelerationBase) {
        super(initialValue, min, max, step, accelerationBase);
    }

    @Override
    public IntegerVariable deepCopy() {
        IntegerVariable copy = new IntegerVariable(initialValue, min, max, stepSize, accelerationFactor);
        copy.value = value;
        return copy;
    }
}

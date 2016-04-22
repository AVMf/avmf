package org.avmframework.variable;

public class IntegerVariable extends AtomicVariable {

    public IntegerVariable(int initialValue) {
        super(initialValue);
    }

    public IntegerVariable(int initialValue, int min, int max) {
        super(initialValue, min, max);
    }

    public IntegerVariable(int initialValue, int min, int max, int accelerationFactor) {
        super(initialValue, min, max, accelerationFactor);
    }

    public double getValueAsInt() {
        return value;
    }

    @Override
    public IntegerVariable deepCopy() {
        IntegerVariable copy = new IntegerVariable(initialValue, min, max, accelerationFactor);
        copy.value = value;
        return copy;
    }

    @Override
    public String toString() {
        return "" + getValueAsInt();
    }

}

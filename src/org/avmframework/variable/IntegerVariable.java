package org.avmframework.variable;

public class IntegerVariable extends AtomicVariable {

    protected IntegerVariable() {
    }

    public IntegerVariable(int initialValue) {
        super(initialValue);
    }

    public IntegerVariable(int initialValue, int min, int max) {
        super(initialValue, min, max);
    }

    public IntegerVariable(int initialValue, int min, int max, int accelerationBase) {
        super(initialValue, min, max, accelerationBase);
    }

    public double getValueAsInt() {
        return value;
    }

    @Override
    public IntegerVariable deepCopy() {
        IntegerVariable copy = new IntegerVariable();
        copy.initialValue = initialValue;
        copy.min = min;
        copy.max = max;
        copy.accelerationFactor = accelerationFactor;
        copy.value = value;
        return copy;
    }

    @Override
    public String toString() {
        return "" + getValueAsInt();
    }

}

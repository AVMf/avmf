package org.avmframework.variable;

public class DoubleVariable extends AtomicVariable {

    public DoubleVariable() {
        super(new AtomicVariableSpecification());
    }

    public DoubleVariable(int min, int max) {
        super(new AtomicVariableSpecification(min, max));
    }

    public DoubleVariable(int min, int max, int precision) {
        super(new AtomicVariableSpecification(min, max, precision));
    }

    public DoubleVariable(int min, int max, int precision, int step, int accelerationBase) {
        super(new AtomicVariableSpecification(min, max, precision, step, accelerationBase));
    }

    public double getDoubleValue() {
        return value / Math.pow(10, varSpec.getPrecision());
    }
}

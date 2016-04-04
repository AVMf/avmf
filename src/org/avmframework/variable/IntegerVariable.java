package org.avmframework.variable;

public class IntegerVariable extends AtomicVariable {

    public IntegerVariable() {
        super(new AtomicVariableSpecification());
    }

    public IntegerVariable(int min, int max) {
        super(new AtomicVariableSpecification(min, max));
    }

    public IntegerVariable(int min, int max, int step, int accelerationBase) {
        super(new AtomicVariableSpecification(min, max, step, accelerationBase));
    }

    public int getIntValue() {
        return value;
    }
}

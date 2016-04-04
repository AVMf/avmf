package org.avmframework.variable;

public abstract class AtomicVariable extends Variable {

    protected AtomicVariableSpecification varSpec;
    protected int value;

    protected AtomicVariable(AtomicVariableSpecification varSpec) {
        this.varSpec = varSpec;
    }

    public AtomicVariableSpecification getVariableSpecification() {
        return varSpec;
    }

}

package uk.ac.shef.avmframework.variable;

public abstract class Variable {

    protected VariableSpecification variableSpecification;

    public Variable() {
        variableSpecification = new VariableSpecification();
    }

    public Variable(int min, int max) {
        variableSpecification = new VariableSpecification(min, max);
    }

    protected Variable(VariableSpecification variableSpecification) {
        this.variableSpecification = new VariableSpecification();
    }

    public abstract VariableSpecification getVariableSpecification();
}

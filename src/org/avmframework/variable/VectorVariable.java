package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public class VectorVariable extends Variable {

    public static final int INITIAL_LENGTH_DEFAULT = 0;
    public static final int MIN_LENGTH_DEFAULT = 0;
    public static final int MAX_LENGTH_DEFAULT = 20;

    protected int initialLength = INITIAL_LENGTH_DEFAULT;
    protected int minLength = MIN_LENGTH_DEFAULT;
    protected int maxLength = MAX_LENGTH_DEFAULT;

    protected VectorVariable() {
    }

    public VectorVariable(int initialLength) {
        this.initialLength = initialLength;
    }

    public VectorVariable(int initialLength, int minLength, int maxLength) {
        this.initialLength = initialLength;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void setValueToInitial() {

    }

    @Override
    public void setValueToRandom(RandomGenerator randomGenerator) {

    }

    @Override
    public VectorVariable deepCopy() {
        return null;
    }

    @Override
    public <T extends Throwable> void accept(VariableTypeVisitor<T> vtv) throws T {
        vtv.visit(this);
    }
}

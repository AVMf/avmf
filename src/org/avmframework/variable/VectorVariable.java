package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public class VectorVariable extends Variable {

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

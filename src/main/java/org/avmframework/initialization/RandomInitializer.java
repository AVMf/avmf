package org.avmframework.initialization;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.Vector;
import org.avmframework.variable.Variable;

public class RandomInitializer extends Initializer {

    protected RandomGenerator rg;

    public RandomInitializer(RandomGenerator rg) {
        this.rg = rg;
    }

    @Override
    public void initialize(Vector vec) {
        for (Variable var : vec.getVariables()) {
            var.setValueToRandom(rg);
        }
    }
}

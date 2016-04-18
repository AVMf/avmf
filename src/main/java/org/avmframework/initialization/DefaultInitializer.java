package org.avmframework.initialization;

import org.avmframework.Vector;
import org.avmframework.variable.Variable;

public class DefaultInitializer extends Initializer {

    @Override
    public void initialize(Vector vec) {
        for (Variable var : vec.getVariables()) {
            var.setValueToInitial();
        }
    }
}

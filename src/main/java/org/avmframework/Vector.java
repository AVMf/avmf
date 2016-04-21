package org.avmframework;

import org.avmframework.variable.Variable;
import org.avmframework.variable.VectorVariable;

public class Vector extends VectorVariable {

    public void addVariable(Variable variable) {
        variables.add(variable);
        size ++;
    }

    @Override
    public Vector deepCopy() {
        return (Vector) doDeepCopy(new Vector());
    }
}

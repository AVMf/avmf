package org.avmframework;

import org.avmframework.variable.Variable;
import org.avmframework.variable.VectorVariable;

import java.util.ArrayList;
import java.util.List;

public class Vector extends VectorVariable {

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public List<Variable> getVariables() {
        return new ArrayList<>(variables);
    }

    @Override
    public Vector deepCopy() {
        return (Vector) doDeepCopy(new Vector());
    }
}

package org.avmframework;

import org.avmframework.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class Vector {

    protected List<Variable> variables = new ArrayList<>();

    public Vector() {
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public Variable getVariable(int index) {
        return variables.get(index);
    }

    public int size() {
        return variables.size();
    }
}

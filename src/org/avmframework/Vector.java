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

    public List<Variable> getVariables() {
        return new ArrayList<>(variables);
    }

    public int size() {
        return variables.size();
    }

    public Vector deepCopy() {
        Vector copy = new Vector();
        for (Variable var : variables) {
            copy.addVariable(var.deepCopy());
        }
        return copy;
    }
}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        return variables != null ? variables.equals(vector.variables) : vector.variables == null;
    }

    @Override
    public int hashCode() {
        return variables != null ? variables.hashCode() : 0;
    }

    @Override
    public String toString() {
        boolean first = true;
        String out = "[";
        for (Variable var : variables) {
            if (first) {
                first = false;
            } else {
                out += ", ";
            }
            out += var;
        }
        out += "]";
        return out;
    }
}

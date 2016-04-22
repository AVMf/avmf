package org.avmframework;

import org.avmframework.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVector {

    protected List<Variable> variables = new ArrayList<>();

    public Variable getVariable(int index) {
        return variables.get(index);
    }

    public int size() {
        return variables.size();
    }

    protected void deepCopyVariables(AbstractVector copy) {
        copy.variables.clear();
        for (Variable var : variables) {
            copy.variables.add(var.deepCopy());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractVector)) return false;

        AbstractVector that = (AbstractVector) o;

        return variables != null ? variables.equals(that.variables) : that.variables == null;
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

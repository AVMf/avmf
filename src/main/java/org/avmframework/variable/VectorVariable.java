package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class VectorVariable extends Variable {

    protected List<Variable> variables = new ArrayList<>();
    protected int size = 0;

    public Variable getVariable(int index) {
        return variables.get(index);
    }

    public List<Variable> getVariables() {
        return new ArrayList<>(variables);
    }

    public int size() {
        return size;
    }

    public void increaseSize() {
        size ++;
        if (size > variables.size()) {
            size = variables.size();
        }
    }

    public void decreaseSize() {
        size --;
        if (size < 0) {
            size = 0;
        }
    }

    @Override
    public void setValueToInitial() {
        for (Variable var : variables) {
            var.setValueToInitial();
        }
    }

    @Override
    public void setValueToRandom(RandomGenerator randomGenerator) {
        for (Variable var : variables) {
            var.setValueToRandom(randomGenerator);
        }
    }

    @Override
    public VectorVariable deepCopy() {
        return doDeepCopy(new VectorVariable());
    }

    protected VectorVariable doDeepCopy(VectorVariable copy) {
        for (Variable var : variables) {
            copy.variables.add(var.deepCopy());
        }
        copy.size = size;
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VectorVariable)) return false;

        VectorVariable that = (VectorVariable) o;

        if (size != that.size) return false;
        return variables != null ? variables.equals(that.variables) : that.variables == null;
    }

    @Override
    public int hashCode() {
        int result = variables != null ? variables.hashCode() : 0;
        result = 31 * result + size;
        return result;
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

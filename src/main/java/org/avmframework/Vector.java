package org.avmframework;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class Vector extends AbstractVector {

  public void addVariable(Variable variable) {
    variables.add(variable);
  }

  public List<Variable> getVariables() {
    return new ArrayList<>(variables);
  }

  public void setVariablesToInitial() {
    for (Variable var : variables) {
      var.setValueToInitial();
    }
  }

  public void setVariablesToRandom(RandomGenerator rg) {
    for (Variable var : variables) {
      var.setValueToRandom(rg);
    }
  }

  public Vector deepCopy() {
    Vector copy = new Vector();
    deepCopyVariables(copy);
    return copy;
  }
}

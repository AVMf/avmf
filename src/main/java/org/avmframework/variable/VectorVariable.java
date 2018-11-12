package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AbstractVector;

public abstract class VectorVariable extends AbstractVector implements Variable {

  protected int size = 0;

  public int size() {
    return size;
  }

  public void increaseSize() {
    size++;
    if (size > variables.size()) {
      size = variables.size();
    }
  }

  public void decreaseSize() {
    size--;
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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof VectorVariable)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }

    VectorVariable that = (VectorVariable) obj;

    return size == that.size;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + size;
    return result;
  }
}

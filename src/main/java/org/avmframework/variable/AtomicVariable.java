package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public abstract class AtomicVariable implements Variable {

  protected int min;
  protected int max;

  protected int initialValue;
  protected int value;

  public AtomicVariable(int initialValue, int min, int max) {
    this.initialValue = initialValue;
    this.min = min;
    this.max = max;
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  @Override
  public void setValueToInitial() {
    setValue(initialValue);
  }

  @Override
  public void setValueToRandom(RandomGenerator randomGenerator) {
    int range = max - min + 1;
    int randomValue = randomGenerator.nextInt(range);
    setValue(min + randomValue);
  }

  public void setValue(int value) {
    if (value > max) {
      value = max;
    }

    if (value < min) {
      value = min;
    }

    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    AtomicVariable that = (AtomicVariable) obj;

    if (min != that.min) {
      return false;
    }
    if (max != that.max) {
      return false;
    }
    if (initialValue != that.initialValue) {
      return false;
    }
    return value == that.value;
  }

  @Override
  public int hashCode() {
    int result = min;
    result = 31 * result + max;
    result = 31 * result + initialValue;
    result = 31 * result + value;
    return result;
  }
}

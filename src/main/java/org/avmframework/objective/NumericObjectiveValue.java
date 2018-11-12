package org.avmframework.objective;

public class NumericObjectiveValue extends ObjectiveValue<NumericObjectiveValue> {

  protected double value;
  protected boolean higherIsBetter;

  protected boolean haveOptimum = false;
  protected double optimum;

  public NumericObjectiveValue(double value, boolean higherIsBetter) {
    this.value = value;
    this.higherIsBetter = higherIsBetter;
  }

  public NumericObjectiveValue(double value, boolean higherIsBetter, double optimum) {
    this(value, higherIsBetter);
    this.haveOptimum = true;
    this.optimum = optimum;
  }

  public double getValue() {
    return value;
  }

  @Override
  public boolean isOptimal() {
    if (!haveOptimum) {
      return false;
    }

    if (higherIsBetter) {
      return value >= optimum;
    } else {
      return value <= optimum;
    }
  }

  @Override
  public int compareTo(NumericObjectiveValue other) {
    if (value == other.value) {
      return 0;
    } else if (value < other.value) {
      return higherIsBetter ? -1 : 1;
    } else {
      return higherIsBetter ? 1 : -1;
    }
  }

  @Override
  public String toString() {
    return "" + getValue();
  }

  public static NumericObjectiveValue higherIsBetterObjectiveValue(double value) {
    return new NumericObjectiveValue(value, true);
  }

  public static NumericObjectiveValue higherIsBetterObjectiveValue(double value, double optimum) {
    return new NumericObjectiveValue(value, true, optimum);
  }

  public static NumericObjectiveValue lowerIsBetterObjectiveValue(double value) {
    return new NumericObjectiveValue(value, false);
  }

  public static NumericObjectiveValue lowerIsBetterObjectiveValue(double value, double optimum) {
    return new NumericObjectiveValue(value, false, optimum);
  }
}

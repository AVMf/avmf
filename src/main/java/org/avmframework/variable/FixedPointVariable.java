package org.avmframework.variable;

public class FixedPointVariable extends AtomicVariable {

  protected int precision;

  public FixedPointVariable(double initialValue, int precision, double min, double max) {
    super(
        doubleToInt(initialValue, precision),
        doubleToInt(min, precision),
        doubleToInt(max, precision));
    this.precision = precision;
    if (min > max) {
      throw new MinGreaterThanMaxException(min, max);
    }
    setValueToInitial();
  }

  public double asDouble() {
    return intToDouble(value, precision);
  }

  @Override
  public FixedPointVariable deepCopy() {
    FixedPointVariable copy = new FixedPointVariable(initialValue, precision, min, max);
    copy.value = value;
    return copy;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FixedPointVariable)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }

    FixedPointVariable that = (FixedPointVariable) obj;

    return precision == that.precision;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + precision;
    return result;
  }

  @Override
  public String toString() {
    return String.format("%." + precision + "f", asDouble());
  }

  private static int doubleToInt(double value, int precision) {
    return (int) Math.round(value * Math.pow(10, precision));
  }

  private static double intToDouble(int value, int precision) {
    return ((double) value) * Math.pow(10, -precision);
  }
}

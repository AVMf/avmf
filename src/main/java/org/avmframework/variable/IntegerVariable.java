package org.avmframework.variable;

public class IntegerVariable extends AtomicVariable {

  public IntegerVariable(int initialValue, int min, int max) {
    super(initialValue, min, max);
    if (min > max) {
      throw new MinGreaterThanMaxException(min, max);
    }
    setValueToInitial();
  }

  public int asInt() {
    return value;
  }

  @Override
  public IntegerVariable deepCopy() {
    IntegerVariable copy = new IntegerVariable(initialValue, min, max);
    copy.value = value;
    return copy;
  }

  @Override
  public String toString() {
    return "" + asInt();
  }
}

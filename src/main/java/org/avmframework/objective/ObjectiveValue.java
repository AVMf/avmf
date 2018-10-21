package org.avmframework.objective;

public abstract class ObjectiveValue<T extends ObjectiveValue> implements Comparable<T> {

  public abstract boolean isOptimal();

  public boolean betterThan(T other) {
    return compareTo(other) > 0;
  }

  public boolean sameAs(T other) {
    return compareTo(other) == 0;
  }

  public boolean worseThan(T other) {
    return compareTo(other) < 0;
  }
}

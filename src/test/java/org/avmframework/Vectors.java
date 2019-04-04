package org.avmframework;

import org.avmframework.variable.IntegerVariable;

public class Vectors {

  public static Vector emptyVector() {
    return new Vector();
  }

  public static Vector singleIntegerVector() {
    return integerVector(1);
  }

  public static Vector integerVector(int length) {
    return integerVector(length, 0);
  }

  public static Vector integerVector(int length, int initialValue) {
    Vector vector = new Vector();
    for (int i = 0; i < length; i++) {
      vector.addVariable(new IntegerVariable(initialValue, -100, 100));
    }
    return vector;
  }
}

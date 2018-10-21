package org.avmframework.objective;

public class DistanceFunctions {

  public static double K = 1.0;

  public static double equals(boolean a, boolean b) {
    if (a == b) return 0;
    return K;
  }

  public static double equals(double a, double b) {
    return Math.abs(a - b) + K;
  }

  public static double notEquals(boolean a, boolean b) {
    if (a != b) return 0;
    return K;
  }

  public static double notEquals(double a, double b) {
    if (a == b) return K;
    return 0;
  }

  public static double greaterThan(double a, double b) {
    if (a > b) return 0;
    return (b - a) + K;
  }

  public static double greaterThanOrEquals(double a, double b) {
    if (a >= b) return 0;
    return (b - a) + K;
  }

  public static double lessThan(double a, double b) {
    if (a < b) return 0;
    return (a - b) + K;
  }

  public static double lessThanOrEquals(double a, double b) {
    if (a <= b) return 0;
    return (a - b) + K;
  }
}

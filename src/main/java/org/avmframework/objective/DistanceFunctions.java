package org.avmframework.objective;

public class DistanceFunctions {

  public static double K = 1.0;

  public static double equals(boolean bool1, boolean bool2) {
    if (bool1 == bool2) {
      return 0;
    }
    return K;
  }

  public static double equals(double num1, double num2) {
    return Math.abs(num1 - num2) + K;
  }

  public static double notEquals(boolean bool1, boolean bool2) {
    if (bool1 != bool2) {
      return 0;
    }
    return K;
  }

  public static double notEquals(double num1, double num2) {
    if (num1 == num2) {
      return K;
    }
    return 0;
  }

  public static double greaterThan(double num1, double num2) {
    if (num1 > num2) {
      return 0;
    }
    return (num2 - num1) + K;
  }

  public static double greaterThanOrEquals(double num1, double num2) {
    if (num1 >= num2) {
      return 0;
    }
    return (num2 - num1) + K;
  }

  public static double lessThan(double num1, double num2) {
    if (num1 < num2) {
      return 0;
    }
    return (num1 - num2) + K;
  }

  public static double lessThanOrEquals(double num1, double num2) {
    if (num1 <= num2) {
      return 0;
    }
    return (num1 - num2) + K;
  }
}

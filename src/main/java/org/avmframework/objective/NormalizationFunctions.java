package org.avmframework.objective;

public class NormalizationFunctions {

  static double ALPHA = 1.001;
  static double BETA = 1.0;

  public static double omega0(double x) {
    return 1 - Math.pow(ALPHA, -x);
  }

  public static double omega1(double x) {
    return x / (x + BETA);
  }
}

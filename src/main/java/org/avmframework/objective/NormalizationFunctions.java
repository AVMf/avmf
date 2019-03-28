package org.avmframework.objective;

public class NormalizationFunctions {

  static double ALPHA = 1.001;
  static double BETA = 1.0;

  public static double omega0(double num) {
    return 1 - Math.pow(ALPHA, -num);
  }

  public static double omega1(double num) {
    return num / (num + BETA);
  }
}

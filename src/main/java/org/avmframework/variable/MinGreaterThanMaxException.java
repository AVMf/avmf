package org.avmframework.variable;

public class MinGreaterThanMaxException extends RuntimeException {

  public MinGreaterThanMaxException(char min, char max) {
    this("" + min, "" + max);
  }

  public MinGreaterThanMaxException(double min, double max) {
    this("" + min, "" + max);
  }

  public MinGreaterThanMaxException(int min, int max) {
    this("" + min, "" + max);
  }

  public MinGreaterThanMaxException(String min, String max) {
    super("Min value (" + min + ") is greater than max value (" + max + ")");
  }
}

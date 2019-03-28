package org.avmframework.examples.inputdatageneration.triangle;

public class Triangle {

  public enum TriangleType {
    NOT_A_TRIANGLE,
    SCALENE,
    EQUILATERAL,
    ISOSCELES;
  }

  public static TriangleType classify(int num1, int num2, int num3) {
    TriangleType type;

    if (num1 > num2) {
      int temp = num1;
      num1 = num2;
      num2 = temp;
    }
    if (num1 > num3) {
      int temp = num1;
      num1 = num3;
      num3 = temp;
    }
    if (num2 > num3) {
      int temp = num2;
      num2 = num3;
      num3 = temp;
    }
    if (num1 + num2 <= num3) {
      type = TriangleType.NOT_A_TRIANGLE;
    } else {
      type = TriangleType.SCALENE;
      if (num1 == num2) {
        if (num2 == num3) {
          type = TriangleType.EQUILATERAL;
        }
      } else {
        if (num1 == num2) {
          type = TriangleType.ISOSCELES;
        } else if (num2 == num3) {
          type = TriangleType.ISOSCELES;
        }
      }
    }
    return type;
  }
}

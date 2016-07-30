package org.avmframework.examples.inputdatageneration.triangle;

public class Triangle {

    public enum TriangleType {
        NOT_A_TRIANGLE, SCALENE, EQUILATERAL, ISOSCELES;
    }

    public static TriangleType classify(int a, int b, int c) {
        TriangleType type;

        if (a > b) {
            int t = a;
            a = b;
            b = t;
        }
        if (a > c) {
            int t = a;
            a = c;
            c = t;
        }
        if (b > c) {
            int t = b;
            b = c;
            c = t;
        }
        if (a + b <= c) {
            type = TriangleType.NOT_A_TRIANGLE;
        } else {
            type = TriangleType.SCALENE;
            if (a == b) {
                if (b == c) {
                    type = TriangleType.EQUILATERAL;
                }
            } else {
                if (a == b) {
                    type = TriangleType.ISOSCELES;
                } else if (b == c) {
                    type = TriangleType.ISOSCELES;
                }
            }
        }
        return type;
    }
}


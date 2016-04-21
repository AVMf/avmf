package org.avmframework;

import org.avmframework.variable.IntegerVariable;

public class Vectors {

    public static Vector emptyVector() {
        return new Vector();
    }

    public static Vector singleIntegerVector() {
        Vector vector = new Vector();
        vector.addVariable(new IntegerVariable(0, -100, 100));
        return vector;
    }

}

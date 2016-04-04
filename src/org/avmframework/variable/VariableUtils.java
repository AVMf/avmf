package org.avmframework.variable;

import org.avmframework.Vector;

public class VariableUtils {

    public static int intValue(Vector vector, int index) {
        return ((IntegerVariable) vector.getVariable(index)).getIntValue();
    }

    public static double doubleValue(Vector vector, int index) {
        return ((DoubleVariable) vector.getVariable(index)).getDoubleValue();
    }

}

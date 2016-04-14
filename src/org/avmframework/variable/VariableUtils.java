package org.avmframework.variable;

import org.avmframework.Vector;

public class VariableUtils {

    public static int intValue(Vector vector, int index) {
        return ((IntegerVariable) vector.getVariable(index)).getValue();
    }

    public static int intValue(Variable var) {
        return ((IntegerVariable) var).getValue();
    }

    public static String intVectorAsString(Vector vector) {
        boolean first = true;
        String out = "[";
        for (Variable var : vector.getVariables()) {
            if (first) {
                first = false;
            } else {
                out += ", ";
            }
            out += intValue(var);
        }
        out += "]";
        return out;
    }

    /*
    public static double doubleValue(Vector vector, int index) {
        return ((DoubleVariable) vector.getVariable(index)).getDoubleValue();
    }
    */
}

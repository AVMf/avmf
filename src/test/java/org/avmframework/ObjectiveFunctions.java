package org.avmframework;

import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;

public class ObjectiveFunctions {

    public static ObjectiveFunction flatObjectiveFunction() {
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                return NumericObjectiveValue.HigherIsBetterObjectiveValue(1.0);
            }
        };
        return objFun;
    }
}

package org.avmframework;

import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;

public class ObjectiveFunctions {

    public static ObjectiveFunction flat() {
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                return NumericObjectiveValue.HigherIsBetterObjectiveValue(1.0);
            }
        };
        return objFun;
    }

    public static ObjectiveFunction initialImprovement(final int improvementFor) {
        ObjectiveFunction objFun = new ObjectiveFunction() {
            int value = 0;

            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                if (value < improvementFor) {
                    value ++;
                }
                return NumericObjectiveValue.HigherIsBetterObjectiveValue(value);
            }
        };
        return objFun;
    }
}

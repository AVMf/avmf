package org.avmframework;

import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.IntegerVariable;
import org.avmframework.variable.Variable;

public class ObjectiveFunctions {

  public static ObjectiveFunction flat() {
    ObjectiveFunction objFun =
        new ObjectiveFunction() {
          @Override
          protected ObjectiveValue computeObjectiveValue(Vector vector) {
            return NumericObjectiveValue.higherIsBetterObjectiveValue(1.0);
          }
        };
    return objFun;
  }

  public static ObjectiveFunction allZeros() {
    return new ObjectiveFunction() {
      @Override
      protected ObjectiveValue computeObjectiveValue(Vector vector) {
        int distance = 0;
        for (Variable var : vector.getVariables()) {
          distance += Math.abs(((IntegerVariable) var).getValue());
        }
        return NumericObjectiveValue.lowerIsBetterObjectiveValue(distance, 0);
      }
    };
  }
}

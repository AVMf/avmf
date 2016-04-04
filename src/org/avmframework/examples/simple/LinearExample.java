package org.avmframework.examples.simple;

import org.avmframework.AVM;
import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.IntegerVariable;
import org.avmframework.variable.VariableUtils;

public class LinearExample implements ObjectiveFunction {

    @Override
    public ObjectiveValue compute(Vector vector) {
        int value = VariableUtils.intValue(vector, 0);
        int distance = Math.abs(value - 100);
        return NumericObjectiveValue.LowerIsBetterObjectiveValue(distance, 0);
    }

    public static void main(String[] args) {
        Vector vector = new Vector();
        vector.addVariable(new IntegerVariable(-100000, 10000));

        AVM avm;
    }
}

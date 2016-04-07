package org.avmframework.examples.simple;

import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.localsearch.IteratedPatternSearch;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.IntegerVariable;
import org.avmframework.variable.VariableUtils;

public class LinearExample  {

    public static void main(String[] args) {
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                int value = VariableUtils.intValue(vector, 0);
                int distance = Math.abs(value - 100);
                return NumericObjectiveValue.LowerIsBetterObjectiveValue(distance, 0);
            }
        };

        LocalSearch ips = new IteratedPatternSearch();
        TerminationPolicy tp = new TerminationPolicy(true, 1000);

        Vector vector = new Vector();
        vector.addVariable(new IntegerVariable(0, -1000, 1000));

        AVM avm = new AVM(ips, tp);
        Monitor monitor = avm.search(vector, objFun);

        System.out.println("Number of objective function evaluations: " + monitor.getNumEvaluations());
    }
}

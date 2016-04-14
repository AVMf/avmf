package org.avmframework.examples.quadratic;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.IteratedPatternSearch;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.IntegerVariable;
import org.avmframework.variable.VariableUtils;

public class QuadraticExample  {

    public static void main(String[] args) {
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                int x = VariableUtils.intValue(vector, 0);
                int y = (x * x)- x - 30;
                int distance = Math.abs(y);
                return NumericObjectiveValue.LowerIsBetterObjectiveValue(distance, 0);
            }
        };

        LocalSearch ips = new IteratedPatternSearch();
        TerminationPolicy tp = new TerminationPolicy(true, 1000);

        Vector vector = new Vector();
        vector.addVariable(new IntegerVariable(0, -1000, 1000));

        RandomGenerator rg = new MersenneTwister();
        RandomInitializer ri = new RandomInitializer(rg);

        AVM avm = new AVM(ips, tp, ri);
        Monitor monitor = avm.search(vector, objFun);
        Vector bestVector = monitor.getBestVector();
        int bestSolution = VariableUtils.intValue(vector, 0);
        int numEvaluations = monitor.getNumEvaluations();

        System.out.println("Best solution: " + bestSolution);
        System.out.println("Number of objective function evaluations: " + numEvaluations);
    }
}

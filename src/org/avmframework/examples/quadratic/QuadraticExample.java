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

        // define the objective function
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                int x = VariableUtils.intValue(vector, 0);
                int y = (x * x)- x - 30;
                int distance = Math.abs(y);
                return NumericObjectiveValue.LowerIsBetterObjectiveValue(distance, 0);
            }
        };

        // set up the vector to be optimized
        Vector vector = new Vector();
        vector.addVariable(new IntegerVariable(0, -1000, 1000));

        // set up the local search to be used
        LocalSearch ips = new IteratedPatternSearch();
        TerminationPolicy tp = new TerminationPolicy(true, 1000);

        // set up the random generator
        RandomGenerator rg = new MersenneTwister();

        // set up the initializer
        RandomInitializer ri = new RandomInitializer(rg);

        // set up the AVM
        AVM avm = new AVM(ips, tp, ri);

        // perform the search
        Monitor monitor = avm.search(vector, objFun);

        // output the results
        Vector bestVector = monitor.getBestVector();
        int bestSolution = VariableUtils.intValue(bestVector, 0);
        int numEvaluations = monitor.getNumEvaluations();

        System.out.println("Best solution: " + bestSolution);
        System.out.println("Number of objective function evaluations: " + numEvaluations);
    }
}

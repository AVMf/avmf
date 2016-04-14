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
import org.avmframework.variable.FloatingPointVariable;

public class QuadraticExample  {

    public static final int A = 4, B = 10, C = 6;

    public static void main(String[] args) {

        // define the objective function
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                double x = ((FloatingPointVariable) vector.getVariable(0)).getValueAsDouble();
                double y = (A * x * x) + (B * x) + C;
                double distance = Math.abs(y);
                return NumericObjectiveValue.LowerIsBetterObjectiveValue(distance, 0);
            }
        };

        // set up the vector to be optimized
        Vector vector = new Vector();
        vector.addVariable(new FloatingPointVariable(0.0, 1, -100.0, 100.0));

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
        System.out.println("Best solution: " + monitor.getBestVector().getVariable(0));
        System.out.println("Best objective value: " + monitor.getBestObjVal());
        System.out.println("Number of objective function evaluations: " + monitor.getNumEvaluations());
    }
}

package org.avmframework.examples.quadratic;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.FloatingPointVariable;

public class QuadraticExample  {

    static final int A = 4, B = 10, C = 6;

    static final int PRECISION = 1;
    static final double INITIAL_VALUE = 0.0, MIN = -100000.0, MAX = 100000.0;

    static final int MAX_EVALUATIONS = 1000;

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
        vector.addVariable(new FloatingPointVariable(INITIAL_VALUE, PRECISION, MIN, MAX));

        // set up the local search ("IteratedPatternSearch" / "GeometricSearch" / "LatticeSearch")
        String localSearchName = "IteratedPatternSearch";
        if (args.length > 0) {
            localSearchName = args[0];
        }
        LocalSearch localSearch = LocalSearch.instantiate(localSearchName);

        // set up the termination policy
        TerminationPolicy terminationPolicy = TerminationPolicy.maxEvaluations(MAX_EVALUATIONS);

        // set up random initialization of vectors
        RandomGenerator randomGenerator = new MersenneTwister();
        Initializer initializer = new RandomInitializer(randomGenerator);

        // set up the AVM
        AVM avm = new AVM(localSearch, terminationPolicy, initializer);

        // perform the search
        Monitor monitor = avm.search(vector, objFun);

        // output the results
        System.out.println("Best solution: " + monitor.getBestVector().getVariable(0));
        System.out.println("Best objective value: " + monitor.getBestObjVal());
        System.out.println(
                "Number of objective function evaluations: " + monitor.getNumEvaluations() +
                        " (unique: " + monitor.getNumUniqueEvaluations() + ")"
        );
        System.out.println("Running time: " + monitor.getRunningTime() + "ms");
    }
}

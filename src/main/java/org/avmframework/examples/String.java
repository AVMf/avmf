package org.avmframework.examples;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.StringVariable;

import static org.avmframework.variable.StringVariable.createPrintableASCIICharacterVariable;

public class String {

    static final java.lang.String TARGET_STRING = "Alternating Variable Method";
    static final int MAX_EVALUATIONS = 100000;

    public static void main(java.lang.String[] args) {

        // define the objective function
        ObjectiveFunction objFun = new ObjectiveFunction() {
            @Override
            protected ObjectiveValue computeObjectiveValue(Vector vector) {
                java.lang.String string = ((StringVariable) vector.getVariable(0)).asString();
                double distance = stringEqualsDistance(string, TARGET_STRING);
                return NumericObjectiveValue.LowerIsBetterObjectiveValue(distance, 0);
            }

            protected double stringEqualsDistance(java.lang.String str, java.lang.String target) {
                double distance = 0;
                for (int i=0; i < Math.min(str.length(), target.length()); i++ ) {
                    distance += charEqualsDistance(str.charAt(i), target.charAt(i));
                }
                int diffPenalty = Math.abs(str.length() - target.length());
                return distance + diffPenalty;
            }

            protected double charEqualsDistance(char chr, char target) {
                return 1.0 - 1.0 / (1.0 + Math.abs(chr - target));
            }
        };

        // set up the string variable to be optimized
        StringVariable strVar = createPrintableASCIICharacterVariable("Nothing related to the target string", 50, ' ');

        // set up the vector to be optimized
        Vector vector = new Vector();
        vector.addVariable(strVar);

        // set up the local search
        // Geometric or lattice search can be used if "GeometricSearch" or "LatticeSearch" are provided as a parameter
        java.lang.String localSearchName = "IteratedPatternSearch";
        if (args.length > 0 && (args[0].equals("GeometricSearch") || args[0].equals("LatticeSearch"))) {
            localSearchName = args[0];
        }
        LocalSearch localSearch = LocalSearch.instantiate(localSearchName);

        // set up the termination policy
        TerminationPolicy terminationPolicy = TerminationPolicy.createMaxEvaluationsTerminationPolicy(MAX_EVALUATIONS);

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

package org.avmframework.examples;

import static org.avmframework.variable.StringVariable.createPrintableAsciiCharacterVariable;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AlternatingVariableMethod;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.util.ArgsParser;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.StringVariable;

public class StringOptimization {

  // HOW TO RUN:
  //
  // java class org.avmframework.examples.StringOptimization [search]
  //   where:
  //     - [search] is an optional parameter denoting which search to use
  //       (e.g., "IteratedPatternSearch", "GeometricSearch" or "LatticeSearch")

  // CHANGE THE FOLLOWING CONSTANTS TO EXPLORE THEIR EFFECT ON THE SEARCH:

  // - string to be searched for:
  static final String TARGET_STRING = "Alternating Variable Method";

  // - search constants
  static final String SEARCH_NAME = "IteratedPatternSearch"; // can also be set at the command line
  static final int MAX_EVALUATIONS = 1000;

  public static void main(String[] args) {

    // define the objective function
    ObjectiveFunction objFun =
        new ObjectiveFunction() {
          @Override
          protected ObjectiveValue computeObjectiveValue(Vector vector) {
            String string = ((StringVariable) vector.getVariable(0)).asString();
            double distance = stringEqualsDistance(string, TARGET_STRING);
            return NumericObjectiveValue.lowerIsBetterObjectiveValue(distance, 0);
          }

          protected double stringEqualsDistance(String str, String target) {
            double distance = 0;
            for (int i = 0; i < Math.min(str.length(), target.length()); i++) {
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
    StringVariable strVar =
        createPrintableAsciiCharacterVariable("Nothing related to the target string", 50, ' ');

    // set up the vector to be optimized
    Vector vector = new Vector();
    vector.addVariable(strVar);

    // instantiate local search from command line, using default (set by the constant SEARCH_NAME)
    // if none supplied
    LocalSearch localSearch =
        new ArgsParser(StringOptimization.class, args).parseSearchParam(SEARCH_NAME);

    // set up the termination policy
    TerminationPolicy terminationPolicy =
        TerminationPolicy.createMaxEvaluationsTerminationPolicy(MAX_EVALUATIONS);

    // set up random initialization of vectors
    RandomGenerator randomGenerator = new MersenneTwister();
    Initializer initializer = new RandomInitializer(randomGenerator);

    // set up the AlternatingVariableMethod
    AlternatingVariableMethod avm = new AlternatingVariableMethod(
        localSearch, terminationPolicy, initializer);

    // perform the search
    Monitor monitor = avm.search(vector, objFun);

    // output the results
    System.out.println("Best solution: " + monitor.getBestVector().getVariable(0));
    System.out.println("Best objective value: " + monitor.getBestObjVal());
    System.out.println(
        "Number of objective function evaluations: "
            + monitor.getNumEvaluations()
            + " (unique: "
            + monitor.getNumUniqueEvaluations()
            + ")");
    System.out.println("Running time: " + monitor.getRunningTime() + "ms");
  }
}

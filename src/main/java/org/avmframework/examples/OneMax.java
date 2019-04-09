package org.avmframework.examples;

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
import org.avmframework.variable.IntegerVariable;
import org.avmframework.variable.Variable;

public class OneMax {

  // HOW TO RUN:
  //
  // java class org.avmframework.examples.OneMax [search]
  //   where:
  //     - [search] is an optional parameter denoting which search to use
  //       (e.g., "IteratedPatternSearch", "GeometricSearch" or "LatticeSearch")

  // CHANGE THE FOLLOWING CONSTANTS TO EXPLORE THEIR EFFECT ON THE SEARCH:

  // - vector constants
  static final int NUM_VARS = 10;
  static final int INIT = 0;
  static final int MIN = -100000;
  static final int MAX = 100000;

  // - search constants
  static final String SEARCH_NAME = "IteratedPatternSearch"; // can also be set at the command line
  static final int MAX_EVALUATIONS = 1000;

  public static void main(String[] args) {

    // define the objective function
    ObjectiveFunction objFun =
        new ObjectiveFunction() {
          //@Override
          //protected ObjectiveValue computeObjectiveValue(Vector vector) {
          //  int distance = 0;
          //  int product = 0;
          //  int sum = 0;
          //  for (Variable var : vector.getVariables()) {
          //    sum += Math.abs(((IntegerVariable) var).getValue());
          //    product *= Math.abs(((IntegerVariable) var).getValue());
          //  }
            // product = product - 1;
          //return NumericObjectiveValue.lowerIsBetterObjectiveValue(distance, 0);

          @Override
          protected ObjectiveValue computeObjectiveValue(Vector vector) {
            int distance = 0;
            int product = 0;
            int sum = 0;
            for (Variable var : vector.getVariables()) {
              distance += Math.abs(((IntegerVariable) var).getValue() - 1);
            }
          return NumericObjectiveValue.lowerIsBetterObjectiveValue(distance, 0);

          }
        };

    // set up the vector to be optimized
    Vector vector = new Vector();
    for (int i = 0; i < NUM_VARS; i++) {
      vector.addVariable(new IntegerVariable(INIT, MIN, MAX));
    }

    // instantiate local search from command line, using default (set by the constant SEARCH_NAME)
    // if none supplied
    LocalSearch localSearch = new ArgsParser(OneMax.class, args).parseSearchParam(SEARCH_NAME);

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
    System.out.println("Best solution: " + monitor.getBestVector());
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

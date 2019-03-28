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
import org.avmframework.variable.FixedPointVariable;

public class Quadratic {

  // HOW TO RUN:
  //
  // java class org.avmframework.examples.Quadratic [search]
  //   where:
  //     - [search] is an optional parameter denoting which search to use
  //       (e.g., "IteratedPatternSearch", "GeometricSearch" or "LatticeSearch")

  // CHANGE THE FOLLOWING CONSTANTS TO EXPLORE THEIR EFFECT ON THE SEARCH:

  // - problem constants
  static final int A = 4;
  static final int B = 10;
  static final int C = 6;

  // - vector constants
  static final int PRECISION = 1;
  static final double INITIAL_VALUE = 0.0;
  static final double MIN = -100000.0;
  static final double MAX = 100000.0;

  // - search constants
  static final String SEARCH_NAME = "IteratedPatternSearch"; // can also be set at the command line
  static final int MAX_EVALUATIONS = 1000;

  public static void main(String[] args) {

    // define the objective function
    ObjectiveFunction objFun =
        new ObjectiveFunction() {
          @Override
          protected ObjectiveValue computeObjectiveValue(Vector vector) {
            double num1 = ((FixedPointVariable) vector.getVariable(0)).asDouble();
            double num2 = (A * num1 * num1) + (B * num1) + C;
            double distance = Math.abs(num2);
            return NumericObjectiveValue.lowerIsBetterObjectiveValue(distance, 0);
          }
        };

    // set up the vector to be optimized
    Vector vector = new Vector();
    vector.addVariable(new FixedPointVariable(INITIAL_VALUE, PRECISION, MIN, MAX));

    // instantiate local search from command line, using default (set by the constant SEARCH_NAME)
    // if none supplied
    LocalSearch localSearch = new ArgsParser(Quadratic.class, args).parseSearchParam(SEARCH_NAME);

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

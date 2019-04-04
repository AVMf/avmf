package org.avmframework.examples;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AlternatingVariableMethod;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.testoptimization.PrioritizationObject;
import org.avmframework.examples.testoptimization.TestCase;
import org.avmframework.examples.testoptimization.TestSuiteCoverage;
import org.avmframework.examples.util.ArgsParser;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* This example shows prioritization problem termed as "test case prioritization".
 * It involves three objectives: maximize coverage, maximize fault detection,
 * and minimize execution time.
 * The weights for the objectives and their definition are provided in the class
 * "PrioritizationObjectiveFunction".
 * More objectives can be added as described in the paper below:
 * https://link.springer.com/chapter/10.1007/978-3-319-47443-4_11
 */

public class TestCasePrioritization {

  // HOW TO RUN:
  //
  // java class org.avmframework.examples.AllZeros [search]
  //   where:
  //     - [search] is an optional parameter denoting which search to use
  //       (e.g., "IteratedPatternSearch", "GeometricSearch" or "LatticeSearch")

  // CHANGE THE FOLLOWING CONSTANTS TO EXPLORE THEIR EFFECT ON THE SEARCH:

  // - search constants
  static final String SEARCH_NAME = "IteratedPatternSearch"; // can also be set at the command line
  static final int MAX_EVALUATIONS = 200000;

  public static void main(String[] args) {
    List<TestCase> originalTestSuite = new ArrayList<TestCase>();
    TestSuiteCoverage transitionStateCoverage = new TestSuiteCoverage();
    List<TestCase> prioritizedTestSuite = new ArrayList<TestCase>();

    // sample file that contains the test suite information consisting of 150 test cases
    // each test case consists of key attributes such as id, execution time, apis covered
    String file = "src/main/java/org/avmframework/examples/testoptimization/originalTestSuite.txt";
    originalTestSuite = readTestSuite(file, originalTestSuite, transitionStateCoverage);

    // instantiate the test object using command line parameters
    PrioritizationObject testObject = new PrioritizationObject();

    // instantiate local search from command line, using default (set by the constant SEARCH_NAME)
    // if none supplied
    LocalSearch localSearch =
        new ArgsParser(TestCasePrioritization.class, args).parseSearchParam(SEARCH_NAME);

    // set up the objective function
    ObjectiveFunction objFun = testObject.getObjectiveFunction(
        originalTestSuite, transitionStateCoverage);

    // set up the vector
    Vector vector = testObject.setUpVector(originalTestSuite.size());

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

    prioritizedTestSuite = orderTestCases(monitor, originalTestSuite);
    // this shows how the test cases in the test suite need to be ordered for maximum effectiveness
    // with minimum cost
    System.out.println(
        "The prioritized test cases from higher to lower important test cases "
        + "are represented by ID as follows: ");
    System.out.print("\n");
    for (int i = 0; i < prioritizedTestSuite.size(); i++) {
      System.out.print(prioritizedTestSuite.get(i).getId());
      if ((i + 1) < prioritizedTestSuite.size()) {
        System.out.print(", ");
      }
    }
    System.out.print("\n");
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

  // read the file, populate the test cases and add it to an array
  private static List<TestCase> readTestSuite(
      String file, List<TestCase> testSuite, TestSuiteCoverage transitionStateCoverage) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(file));
      String str;
      int counter = 0;
      while ((str = in.readLine()) != null) {
        if (counter == 0) { // the first row contains the column names
          counter++;
          continue;
        } else {
          String[] details = str.split("\t");

          TestCase testCase = new TestCase();
          testCase.setId(details[0]);
          // set up coverage separated by comma
          String[] apiCovered = details[1].split(",");
          List<String> apiList = new ArrayList<String>();
          for (int i = 0; i < apiCovered.length; i++) {
            apiList.add(apiCovered[i].trim());
            transitionStateCoverage.coverage.add(apiCovered[i].trim());
          }
          testCase.setApiCovered(apiList);
          testCase.setTime(Integer.parseInt(details[2]));
          double faultDetection = Double.parseDouble(details[3]) / Double.parseDouble(details[4]);
          testCase.setNumFailedExecution(Integer.parseInt(details[3]));
          testCase.setNumTotalExecution(Integer.parseInt(details[4]));
          testCase.setFaultDetection(faultDetection);

          testSuite.add(testCase);

          transitionStateCoverage.executionTime += testCase.getTime();
          transitionStateCoverage.faultDetection += testCase.getFaultDetection();
        }
      }
      in.close();
    } catch (IOException exception) {
      // nothing to catch
    }
    return testSuite;
  }

  // final solution to be returned
  protected static List<TestCase> orderTestCases(
      Monitor monitor, List<TestCase> originalTestSuite) {
    List<TestCase> orderedTestSuite = new ArrayList<TestCase>();

    Map<Double, TestCase> numVariablesHash = new HashMap<Double, TestCase>();
    List<Double> numVariables = new ArrayList<Double>();

    for (int i = 0; i < monitor.getBestVector().size(); i++) {
      double num = Double.parseDouble(monitor.getBestVector().getVariable(i).toString());
      while (numVariables.contains(num)) {
        num += 0.09; // to make unique variable values
      }
      numVariables.add(num);
      numVariablesHash.put(num, originalTestSuite.get(i));
    }
    // prioritize test case in decreasing order
    Collections.sort(numVariables, Collections.reverseOrder());

    for (int j = 0; j < numVariables.size(); j++) {
      TestCase tempCase = numVariablesHash.get(numVariables.get(j));
      orderedTestSuite.add(tempCase);
    }
    return orderedTestSuite;
  }
}

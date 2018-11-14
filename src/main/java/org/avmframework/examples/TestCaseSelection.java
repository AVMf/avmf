package org.avmframework.examples;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AlternatingVariableMethod;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.testoptimization.SelectionObject;
import org.avmframework.examples.testoptimization.TestCase;
import org.avmframework.examples.testoptimization.TestSuiteCoverage;
import org.avmframework.examples.util.ArgsParser;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* This example shows selection problem termed as "test case selection".
 * It involves two objectives: maximize coverage and  fault detection.
 * The execution time of the selected test case must be less than or equal to
 * the defined time budget.
 * The weights for the objectives and their definition are provided in the class
 * "SelectionObjectiveFunction".
 * More objectives can be added as described in the paper below:
 * http://dl.acm.org/citation.cfm?id=2908850
 */
public class TestCaseSelection {

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
    List<TestCase> selectedTestSuite = new ArrayList<TestCase>();

    // sample file that contains the test suite information consisting of 150 test cases
    // each test case consists of key attributes such as id, execution time, apis covered
    String file = "src/main/java/org/avmframework/examples/testoptimization/originalTestSuite.txt";
    originalTestSuite = readTestSuite(file, originalTestSuite, transitionStateCoverage);

    // instantiate the test object using command line parameters
    SelectionObject testObject = new SelectionObject();

    // instantiate local search from command line, using default (set by the constant SEARCH_NAME)
    // if none supplied
    LocalSearch localSearch =
        new ArgsParser(TestCaseSelection.class, args).parseSearchParam(SEARCH_NAME);

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

    selectedTestSuite = selectTestCases(monitor, originalTestSuite);
    System.out.println(
        "We are interested to get the selected test cases within time budget, and they"
            + " are represented by ID as follows");

    for (int i = 0; i < selectedTestSuite.size(); i++) {
      System.out.print(selectedTestSuite.get(i).getId());
      if ((i + 1) < selectedTestSuite.size()) {
        System.out.print(", ");
      }
    }
    System.out.print("\n");

    // output the results
    System.out.println("Best solutions vector values: " + monitor.getBestVector());
    System.out.println("Best objective value: " + monitor.getBestObjVal());
    System.out.println(
        "Number of objective function evaluations: "
            + monitor.getNumEvaluations()
            + " (unique: "
            + monitor.getNumUniqueEvaluations()
            + ")");
    System.out.println("Running time: " + monitor.getRunningTime() + "ms");
  }

  // read file and populate the test cases
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
            // calculate the overall coverage of the test suite
            transitionStateCoverage.coverage.add(apiCovered[i].trim());
          }
          testCase.setApiCovered(apiList);
          testCase.setTime(Integer.parseInt(details[2]));
          double faultDetection = Double.parseDouble(details[3]) / Double.parseDouble(details[4]);
          testCase.setNumFailedExecution(Integer.parseInt(details[3]));
          testCase.setNumTotalExecution(Integer.parseInt(details[4]));
          testCase.setFaultDetection(faultDetection);

          testSuite.add(testCase);

          // calculate the overall coverage of the test suite
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
  protected static List<TestCase> selectTestCases(
      Monitor monitor, List<TestCase> originalTestSuite) {
    List<TestCase> selectedTestSuite = new ArrayList<TestCase>();

    int count = 0;
    for (Variable variable : monitor.getBestVector().getVariables()) {
      int num = Integer.parseInt(variable.toString());
      if (num > 0) {
        selectedTestSuite.add(originalTestSuite.get(count));
      }
      count++;
    }
    return selectedTestSuite;
  }
}

package org.avmframework.examples.testoptimization;

import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* This example shows selection problem termed as "test case selection".
 * It involves two objectives: maximize coverage (objCoverage) and
 * fault detection (objFaultDetectionCoverage).
 * The execution time of the selected test case must be less than or equal to
 * the defined time budget.
 * More objectives can be as provided as described in the paper below:
 * http://dl.acm.org/citation.cfm?id=2908850
 */

/* Description the three objectives are provided below:
 * objCoverage measures the coverage of unique APIs for each test case
 * objFaultDetectionCoverage measures the faults found by each test case within
 * a specified time period (e.g., one week in the past)
 * We aim to maximize objCoverage and objFaultDetectionCoverage
 */

public class SelectionObjectiveFunction extends ObjectiveFunction {
  private List<TestCase> originalTestSuite = new ArrayList<TestCase>();
  private TestSuiteCoverage transitionStateCoverage = new TestSuiteCoverage();

  // assign weights for the two objectives
  // we assume that each objective has equal weight in this context
  // the weights can be modified if some objective is more important than the others
  final double weightCoverage = 0.5;
  final double weightFaultDetection = 0.5;

  // 40% time of test suite is taken as time budget
  // If you want to have very low time budget, you can modify the values of the vector to be real
  // numbers
  // between for example 0 and 1 and modify function "selectTestCases" to select all the test cases
  // with values >0.5
  final double timeBudget = 40;

  public SelectionObjectiveFunction(
      List<TestCase> testSuite, TestSuiteCoverage transitionStateCoverage) {
    this.originalTestSuite = testSuite;
    this.transitionStateCoverage = transitionStateCoverage;
  }

  // in selection the order does not matter, so we include  the coverage and execution time of all
  // the selected test cases
  public ObjectiveValue computeObjectiveValue(Vector vector) {
    List<TestCase> selectedTestSuite = new ArrayList<TestCase>();
    selectedTestSuite = selectTestCases(vector);

    double executionTime = 0;
    double fdc = 0;
    Set<String> coveredCoverageSet = new HashSet<String>();
    for (int i = 0; i < selectedTestSuite.size(); i++) {
      TestCase tempCase = selectedTestSuite.get(i);
      coveredCoverageSet.addAll(tempCase.getApiCovered());
      executionTime += tempCase.getTime();
      fdc += tempCase.getFaultDetection();
    }
    double fitness;
    // convert time budget from percentage to real number
    if (executionTime <= (transitionStateCoverage.getExecutionTime() * timeBudget / 100)) {
      double objCoverage;
      double objFaultDetectionCoverage;
      objCoverage = coveredCoverageSet.size() / transitionStateCoverage.getCoverage().size();
      objFaultDetectionCoverage = fdc / transitionStateCoverage.getFaultDetection();
      // subtract 1 for minimization
      fitness =
          (1 - objCoverage) * this.weightCoverage
          + (1 - objFaultDetectionCoverage) * this.weightFaultDetection;
    } else {
      fitness = 1; // this solution is bad since it exceeds the alloted time budget
    }

    return NumericObjectiveValue.lowerIsBetterObjectiveValue(fitness, 0);
  }

  // select the test cases and return them
  protected List<TestCase> selectTestCases(Vector vector) {
    List<TestCase> selectedTestSuite = new ArrayList<TestCase>();

    int count = 0;
    for (Variable variable : vector.getVariables()) {
      int num = Integer.parseInt(variable.toString());
      if (num > 0) {
        selectedTestSuite.add(this.originalTestSuite.get(count));
      }
      count++;
    }

    return selectedTestSuite;
  }
}

package org.avmframework.examples.testoptimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;

/* This example shows prioritization problem termed as "test case prioritization"
 * It involves three objectives: maximize coverage (objCoverage),
 * maximize fault detection (objFDC) and minimize execution time (objTime)
 * More objectives can be as provided as described in the paper below:
 * https://link.springer.com/chapter/10.1007/978-3-319-47443-4_11
 */

/* Description the three objectives are provided below:
 * objCoverage measures the coverage of unique APIs for the prioritized test cases
 * objFDC measures the faults found by the prioritized test cases within a specified time period (e.g., one week in the past)
 * objTime measures the execution time
 * We aim to maximize objCoverage and objFDC, and minimize objTime
 */

public class PrioritizationObjectiveFunction extends ObjectiveFunction {
  private List<TestCase> originalTestSuite = new ArrayList<TestCase>();
  private TestSuiteCoverage tsCoverage = new TestSuiteCoverage();

  // assign weights for the three objectives
  // we assume that each objective has equal weight in this context
  // the weights can be modified if some objective is more important than the others
  final double WEIGHT_COVERAGE = 0.333, WEIGHT_FAULT_DETECTION = 0.333, WEIGHT_TIME = 0.333;

  public PrioritizationObjectiveFunction(List<TestCase> testSuite, TestSuiteCoverage tsCoverage) {
    this.originalTestSuite = testSuite;
    this.tsCoverage = tsCoverage;
  }

  // The employed prioritization strategy is referred to as STIPI, which is search-based test case
  // prioritization based on incremental unique coverage and position impact.
  // Details in: https://link.springer.com/chapter/10.1007/978-3-319-47443-4_11
  public ObjectiveValue computeObjectiveValue(Vector vector) {
    List<TestCase> orderedTestSuite = new ArrayList<TestCase>();
    // order the test cases based on their values
    orderedTestSuite = orderTestCases(vector);

    double coverageCal = 0, fdcCal = 0, timeCalc = 0;
    Set<String> coveredCoverageSet = new HashSet<String>(); // set is created for unique coverage
    for (int i = 0; i < orderedTestSuite.size(); i++) {
      TestCase testCase = orderedTestSuite.get(i);

      // initially the first test case has no deductions since it has the first position, i.e.,
      // the first test case is scheduled to execute earlier
      if (i == 0) {
        coverageCal = testCase.getApiCovered().size();
        fdcCal = testCase.getFaultDetection();
        timeCalc = testCase.getTime();

        coveredCoverageSet.addAll(testCase.getApiCovered());
      } else { // each test case appearing later are penalized with respect to the test case
               // appearing earlier
        int beforeCoverage = coveredCoverageSet.size();
        coveredCoverageSet.addAll(testCase.getApiCovered());
        // to get the number of unique apis covered by this test case
        int afterCoverage = coveredCoverageSet.size() - beforeCoverage;

        coverageCal +=
            ((double) (afterCoverage))
                * ((double) (orderedTestSuite.size() - i) / orderedTestSuite.size());
        fdcCal +=
            ((double) (testCase.getFaultDetection()))
                * ((double) (orderedTestSuite.size() - i) / orderedTestSuite.size());
        timeCalc +=
            ((double) (testCase.getTime()))
                * ((double) (orderedTestSuite.size() - i) / orderedTestSuite.size());
      }
    }
    double objCoverage, objFDC, objTime;
    double fitness;

    // divide by maximum of all the test cases in the test suite
    objCoverage = coverageCal / tsCoverage.getCoverage().size();
    objFDC = fdcCal / tsCoverage.getFaultDetection();
    objTime = timeCalc / tsCoverage.getExecutionTime();

    // subtract 1 for minimization, 1 is not subtracted for objTime because minimum time is better
    fitness =
        (1 - objCoverage) * this.WEIGHT_COVERAGE
            + (1 - objFDC) * this.WEIGHT_FAULT_DETECTION
            + objTime * this.WEIGHT_TIME;

    return NumericObjectiveValue.LowerIsBetterObjectiveValue(fitness, 0);
  }

  // prioritize test cases based on the values of the vector
  protected List<TestCase> orderTestCases(Vector vector) {
    List<TestCase> orderedTestSuite = new ArrayList<TestCase>();

    Map<Double, TestCase> numVariablesHash = new HashMap<Double, TestCase>();
    List<Double> numVariables = new ArrayList<Double>();

    for (int i = 0; i < vector.size(); i++) {
      double num = Double.parseDouble(vector.getVariable(i).toString());

      while (numVariables.contains(num)) {
        num += 0.09; // to make the values representing the test cases unique
      }
      numVariables.add(num);
      numVariablesHash.put(num, this.originalTestSuite.get(i));
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

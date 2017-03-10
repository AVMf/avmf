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
import org.avmframework.variable.Variable;


/* This example shows prioritization problem termed as "test case prioritization"
 * It involves three objectives: maximize coverage, maximize fault detection and minimize execution time
 * More objectives can be as provided as described in the paper below:
 * https://link.springer.com/chapter/10.1007/978-3-319-47443-4_11
 */


public class PrioritizationObjectiveFunction extends ObjectiveFunction {
	private List<TestCase> originalTestSuite = new ArrayList<TestCase>();
	private TestSuiteCoverage tsCoverage = new TestSuiteCoverage();
	
	// assign weights for the three objectives
	final double WEIGHT_COVERAGE = 0.333, WEIGHT_FAULT_DETECION = 0.333, WEIGHT_TIME = 0.333; 
	
    public PrioritizationObjectiveFunction(List<TestCase> testSuite, TestSuiteCoverage tsCoverage) {
        this.originalTestSuite = testSuite;
        this.tsCoverage = tsCoverage;
    }

	public ObjectiveValue computeObjectiveValue(Vector vector) {
		List<TestCase> orderedTestSuite = new ArrayList<TestCase>();
		orderedTestSuite = orderTestCases(vector);

		double coverageCal = 0, fdcCal = 0, timeCalc = 0;
		Set<String> coveredCoverageSet = new HashSet<String>();
		for (int i = 0; i < orderedTestSuite.size(); i++) {
			TestCase testCase = orderedTestSuite.get(i);
			// initially the first test case has no deductions
			if (i == 0) {
				coverageCal = testCase.getApiCovered().size();
				fdcCal = testCase.getFaultDetection();
				timeCalc = testCase.getTime();

				coveredCoverageSet.addAll(testCase.getApiCovered());
			} else {
				int beforeCoverage = coveredCoverageSet.size();
				coveredCoverageSet.addAll(testCase.getApiCovered());
				int afterCoverage = coveredCoverageSet.size() - beforeCoverage;

				coverageCal += ((double) (afterCoverage))
						* ((double) (orderedTestSuite.size() - i) / orderedTestSuite
								.size());
				fdcCal += ((double) (testCase.getFaultDetection()))
						* ((double) (orderedTestSuite.size() - i) / orderedTestSuite
								.size());
				timeCalc += ((double) (testCase.getTime()))
						* ((double) (orderedTestSuite.size() - i) / orderedTestSuite
								.size());
			}
		}
		double objCoverage, objFDC, objTime;
		double fitness;

		objCoverage = coverageCal / tsCoverage.getCoverage().size();
		objFDC = fdcCal / tsCoverage.getFaultDetection();
		objTime = timeCalc / tsCoverage.getExecutionTime();

		// subtract 1 for minimization, 1 is not subracted for objTime cause
		// minium time is better
		fitness = (1 - objCoverage) * this.WEIGHT_COVERAGE + (1 - objFDC)
				* this.WEIGHT_FAULT_DETECION + objTime * this.WEIGHT_TIME;

		return NumericObjectiveValue.LowerIsBetterObjectiveValue(fitness, 0);
    }

    protected List<TestCase> orderTestCases(Vector vector){
		List<TestCase> orderedTestSuite = new ArrayList<TestCase>();

		Map<Double, TestCase> numVariablesHash = new HashMap<Double, TestCase>();
		List<Double> numVariables = new ArrayList<Double>();

		for (int i = 0; i < vector.size(); i++) {
			double num = Double.parseDouble(vector.getVariable(i).toString());

			while (numVariables.contains(num)) {
				num += 0.09;		// to make the test cases unique
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

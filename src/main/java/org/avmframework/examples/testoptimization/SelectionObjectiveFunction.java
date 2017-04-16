package org.avmframework.examples.testoptimization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.Variable;

/* This example shows selection problem termed as "test case selection".
 * It involves two objectives: maximize coverage and  fault detection.
 * The execution time of the selected test case must be less than or equal to the defined time budget.
 * More objectives can be as provided as described in the paper below:
 * http://dl.acm.org/citation.cfm?id=2908850
 */

public class SelectionObjectiveFunction extends ObjectiveFunction {
	private List<TestCase> originalTestSuite = new ArrayList<TestCase>();
	private TestSuiteCoverage tsCoverage = new TestSuiteCoverage();
	
	// assign weights for the two objectives
	final double WEIGHT_COVERAGE = 0.5, WEIGHT_FAULT_DETECION = 0.5; 
	final double TIME_BUDGET = 40;	// 40% time of test suite as time budget
	
    public SelectionObjectiveFunction(List<TestCase> testSuite, TestSuiteCoverage tsCoverage) {
        this.originalTestSuite = testSuite;
        this.tsCoverage = tsCoverage;
    }
    
    public ObjectiveValue computeObjectiveValue(Vector vector) {
    	List<TestCase> selectedTestSuite = new ArrayList<TestCase>();
    	selectedTestSuite = selectTestCases(vector);
    	
    	double executionTime = 0, fdc = 0;
    	Set<String> coveredCoverageSet = new HashSet<String>();
    	for (int i=0; i<selectedTestSuite.size();i++){
    		TestCase tempCase = selectedTestSuite.get(i);
    		coveredCoverageSet.addAll(tempCase.getApiCovered());
    		executionTime += tempCase.getTime(); 	
    		fdc += tempCase.getFaultDetection();
    	}
    	double fitness;
    	if (executionTime<= (tsCoverage.getExecutionTime()*TIME_BUDGET/100)){
    		double objCoverage, objFDC;
    		objCoverage = coveredCoverageSet.size()/tsCoverage.getCoverage().size();
    		objFDC = fdc/tsCoverage.getFaultDetection();
    		// subtract 1 for minimization
    		fitness = (1-objCoverage)*this.WEIGHT_COVERAGE + (1-objFDC)*this.WEIGHT_FAULT_DETECION;
    	}else
    		fitness = 1;	// this solution is bad since it exceeds time budget
    		
    	 return NumericObjectiveValue.LowerIsBetterObjectiveValue(fitness, 0);
    }

    protected List<TestCase> selectTestCases(Vector vector){
		List<TestCase> selectedTestSuite = new ArrayList<TestCase>();
		
		int i=0;
		for (Variable variable:vector.getVariables()){
			int num = Integer.parseInt(variable.toString());
			if (num>0)
				selectedTestSuite.add(this.originalTestSuite.get(i));
			i++;
		}		
		return selectedTestSuite;
	}
}

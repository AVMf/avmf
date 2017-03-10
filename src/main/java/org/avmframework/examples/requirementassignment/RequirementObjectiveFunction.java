package org.avmframework.examples.requirementassignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.Variable;


/* This example shows requirement assignment optimization problem.
 * The requirements need to be optimally assigned to the stakeholders
 * It involves three objectives: maximize the number of requirements  assigned to the stakeholders,
 * maximize familarity between each requirement and stakeholder, and 
 * minimize the workload between different stakeholders
 * Other objectives can be assigned as described in the paper
 * https://link.springer.com/article/10.1007/s10664-015-9418-0
 */


public class RequirementObjectiveFunction extends ObjectiveFunction {
	private List<Requirement> reqList = new ArrayList<Requirement>();
	private RequirementOverview reqOverview = new RequirementOverview();
	
	// assign weights for the three objectives
	final double WEIGHT_ASSIGNED_DIS= 0.333, WEIGHT_FAM_AVG = 0.333, WEIGHT_WL_DIFF = 0.333; 

	 final int NUM_STAKEHOLDER = 10;		// number of stakeholder to distribute requirements
	 final int MIN_FM=1, MAX_FM= 10;		// familarity
	 
	 
    public RequirementObjectiveFunction(List<Requirement> reqList, RequirementOverview reqOverview) {
        this.reqList = reqList;
        this.reqOverview = reqOverview;
    }

	public ObjectiveValue computeObjectiveValue(Vector vector) {
		double AssignedDis = 0.0;
		double FamAvg = 0.0;
		double OWLDiffAvg = 0.0;

		int assignedNumber = 0;
		int fmst[] = new int[NUM_STAKEHOLDER];
		double wl[] = new double[NUM_STAKEHOLDER];
		int number[] = new int[NUM_STAKEHOLDER];

		for (int i = 0; i < NUM_STAKEHOLDER; i++) {
			fmst[i] = 0;
			wl[i] = 0;
			number[i] = 0;
		}

    	for (int i=0; i<vector.getVariables().size();i++){
    		int value = Integer.parseInt(vector.getVariable(i).toString());
	    	if (value>= 0){
	    		assignedNumber++;
	    		fmst[value]++;
				Requirement req = this.reqList.get(i);
				wl[value]+=(1.0*(req.getComplexity()-reqOverview.getMinComp())/(reqOverview.getMaxComp()-
						reqOverview.getMinComp())+1.0*(req.getDependency()-reqOverview.getMinDep())/(reqOverview.getMaxDep()-
								reqOverview.getMinDep())+1.0*(req.getImportance()-reqOverview.getMinImp())/(reqOverview.getMaxImp()-
										reqOverview.getMinImp()))/3.0;
		    	number[value]++;
	    	}
    	}
		AssignedDis = (double)assignedNumber / reqList.size();

		double totalFm = 0.0;
		for (int i = 0; i < fmst.length; i++)
			totalFm += (fmst[i] - MIN_FM) / (MAX_FM - MIN_FM);
		FamAvg = totalFm / assignedNumber;

		for (int i = 0; i < wl.length; i++)
			if (number[i] != 0)
				wl[i] = wl[i]/number[i];

		for (int i = 0; i < NUM_STAKEHOLDER - 1; i++)
			for (int j = i + 1; j < NUM_STAKEHOLDER; j++)
				OWLDiffAvg += Math.abs(wl[i] - wl[j]);
		OWLDiffAvg = OWLDiffAvg / (NUM_STAKEHOLDER * (NUM_STAKEHOLDER - 1));

		double objAssignedDis, objFamAvg, objOWLDiffAvg;
		double fitness;
		
		objAssignedDis = numFormat(AssignedDis);
		objFamAvg = numFormat(FamAvg);
		objOWLDiffAvg = numFormat(OWLDiffAvg);
		
		// fitness need to be minimized
		fitness = (1-objAssignedDis)* this.WEIGHT_ASSIGNED_DIS + (1-objFamAvg)* this.WEIGHT_FAM_AVG
				+ (objOWLDiffAvg)* this.WEIGHT_WL_DIFF;
		return NumericObjectiveValue.LowerIsBetterObjectiveValue(fitness, 0);
	}

	public static  double numFormat (double m){
		DecimalFormat df = new DecimalFormat ("0.0000");
		return Double.parseDouble(df.format(m));
	}
}

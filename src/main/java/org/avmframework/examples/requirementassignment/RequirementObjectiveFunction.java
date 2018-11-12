package org.avmframework.examples.requirementassignment;

import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/* This example shows requirement assignment optimization problem.
 * The requirements need to be optimally assigned to the stakeholders.
 * It involves three objectives: maximize the number of requirements  assigned
 * to the stakeholders (ASSIGN), maximize familarity between each requirement
 * and stakeholder (FAM), and minimize the workload between different
 * stakeholders (OWL)
 * Other objectives can be assigned as described in the paper
 * https://link.springer.com/article/10.1007/s10664-015-9418-0
 */

/* Description the three objectives are provided below:
 * ASSIGN calculates a value that tells how far we are from assigning all the
 * requirements to the stakeholders.
 * FAM is about computing the overall familiarity of stakeholders to assigned
 * requirements
 * OWL computes the overall difference of the workload between each pair of
 * stakeholders
 * We aim to maximize ASSIGN and FAM, and minimize OWL.
 * Detailed description and formulae can be checked from
 * https://link.springer.com/article/10.1007/s10664-015-9418-0
 */

public class RequirementObjectiveFunction extends ObjectiveFunction {
  private List<Requirement> reqList = new ArrayList<Requirement>();
  private RequirementOverview reqOverview = new RequirementOverview();

  // assign weights for the three objectives
  // we assume that each objective has equal weight in this context
  // the weights can be modified if some objective is more important than the others
  final double WEIGHTASSIGNEDDIS = 0.333;
  final double WEIGHTFAMAVG = 0.333;
  final double WEIGHTWLDIFF = 0.333;

  final int NUMBEROFSTAKEHOLDERS = 10; // number of stakeholders to distribute requirements
  final int MINFAMILIARITY = 1; // familiarity of the requirements (minimum)
  final int MAXFAMILIARITY = 10; // familiarity of the requirements (maximum)

  public RequirementObjectiveFunction(List<Requirement> reqList, RequirementOverview reqOverview) {
    this.reqList = reqList;
    this.reqOverview = reqOverview;
  }

  public ObjectiveValue computeObjectiveValue(Vector vector) {
    double assignedDis = 0.0;
    double owlDiffAvg = 0.0;

    int assignedNumber = 0;
    int[] fmst = new int[NUMBEROFSTAKEHOLDERS];
    double[] wl = new double[NUMBEROFSTAKEHOLDERS];
    int[] number = new int[NUMBEROFSTAKEHOLDERS];

    // initial setup
    for (int i = 0; i < NUMBEROFSTAKEHOLDERS; i++) {
      fmst[i] = 0;
      wl[i] = 0;
      number[i] = 0;
    }

    for (int i = 0; i < vector.getVariables().size(); i++) {
      int value = Integer.parseInt(vector.getVariable(i).toString());
      // assign the requirement if the value is 0 or higher
      // -1 means the requirement is not assigned to any stakeholder
      if (value >= 0) {
        assignedNumber++;
        fmst[value]++;
        Requirement req = this.reqList.get(i);
        wl[value] +=
            (1.0
                        * (req.getComplexity() - reqOverview.getMinComp())
                        / (reqOverview.getMaxComp() - reqOverview.getMinComp())
                    + 1.0
                        * (req.getDependency() - reqOverview.getMinDep())
                        / (reqOverview.getMaxDep() - reqOverview.getMinDep())
                    + 1.0
                        * (req.getImportance() - reqOverview.getMinImp())
                        / (reqOverview.getMaxImp() - reqOverview.getMinImp()))
                / 3.0;
        number[value]++;
      }
    }
    assignedDis = (double) assignedNumber / reqList.size();

    double famAvg = 0.0;
    double totalFm = 0.0;
    for (int i = 0; i < fmst.length; i++) {
      totalFm += (fmst[i] - MINFAMILIARITY) / (MAXFAMILIARITY - MINFAMILIARITY);
    }
    famAvg = totalFm / assignedNumber;

    for (int i = 0; i < wl.length; i++) {
      if (number[i] != 0) {
        wl[i] = wl[i] / number[i];
      }
    }

    for (int i = 0; i < NUMBEROFSTAKEHOLDERS - 1; i++) {
      for (int j = i + 1; j < NUMBEROFSTAKEHOLDERS; j++) {
        owlDiffAvg += Math.abs(wl[i] - wl[j]);
      }
    }
    owlDiffAvg = owlDiffAvg / (NUMBEROFSTAKEHOLDERS * (NUMBEROFSTAKEHOLDERS - 1));

    double objAssignedDis;
    double objFamAvg;
    double objOWLDiffAvg;

    objAssignedDis = numFormat(assignedDis);
    objFamAvg = numFormat(famAvg);
    objOWLDiffAvg = numFormat(owlDiffAvg);

    // fitness need to be minimized so we subtract 1 from objAssignedDis and objFamAvg
    double fitness =
        (1 - objAssignedDis) * this.WEIGHTASSIGNEDDIS
            + (1 - objFamAvg) * this.WEIGHTFAMAVG
            + (objOWLDiffAvg) * this.WEIGHTWLDIFF;
    return NumericObjectiveValue.lowerIsBetterObjectiveValue(fitness, 0);
  }

  public static double numFormat(double num) {
    DecimalFormat df = new DecimalFormat("0.0000");
    return Double.parseDouble(df.format(num));
  }
}

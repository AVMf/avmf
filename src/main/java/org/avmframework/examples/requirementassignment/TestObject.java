package org.avmframework.examples.requirementassignment;

import org.avmframework.Vector;
import org.avmframework.variable.IntegerVariable;

import java.util.List;

public class TestObject {
  static final int INIT = 0; 
  static final int MIN = -1;

  // The value -1 means the requirement is not assigned to any stakeholder
  // set up the vector to be optimized
  public Vector setUpVector(int size, int numStakeholder) {
    Vector vector = new Vector();
    for (int i = 0; i < size; i++) {
      vector.addVariable(new IntegerVariable(INIT, MIN, numStakeholder - 1));
    }
    return vector;
  }

  public RequirementObjectiveFunction getObjectiveFunction(
      List<Requirement> requirementList, RequirementOverview reqOverview) {
    return new RequirementObjectiveFunction(requirementList, reqOverview);
  }
}

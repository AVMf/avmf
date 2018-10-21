package org.avmframework.examples.testoptimization;

import java.util.List;
import org.avmframework.Vector;
import org.avmframework.variable.FixedPointVariable;

public class PrioritizationObject {
  static final int INIT = 0, MIN = 0, MAX = 1;

  // set up the vector to be optimized
  public Vector setUpVector(int size) {
    Vector vector = new Vector();
    for (int i = 0; i < size; i++) {
      vector.addVariable(new FixedPointVariable(INIT, 1, MIN, size)); // precision of 1
    }
    return vector;
  }

  public PrioritizationObjectiveFunction getObjectiveFunction(
      List<TestCase> testSuite, TestSuiteCoverage tsCoverage) {
    return new PrioritizationObjectiveFunction(testSuite, tsCoverage);
  }
}

package org.avmframework.examples.testoptimization;

import org.avmframework.Vector;
import org.avmframework.variable.FixedPointVariable;

import java.util.List;

public class PrioritizationObject {
  static final int INIT = 0;
  static final int MIN = 0;
  static final int MAX = 1;

  // set up the vector to be optimized
  public Vector setUpVector(int size) {
    Vector vector = new Vector();
    for (int i = 0; i < size; i++) {
      vector.addVariable(new FixedPointVariable(INIT, 1, MIN, size)); // precision of 1
    }
    return vector;
  }

  public PrioritizationObjectiveFunction getObjectiveFunction(
      List<TestCase> testSuite, TestSuiteCoverage transitionStateCoverage) {
    return new PrioritizationObjectiveFunction(testSuite, transitionStateCoverage);
  }
}

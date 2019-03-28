package org.avmframework.examples.testoptimization;

import org.avmframework.Vector;
import org.avmframework.variable.IntegerVariable;

import java.util.List;

public class SelectionObject {
  static final int INIT = 0;
  static final int MIN = 0;
  static final int MAX = 1;

  // set up the vector to be optimized
  public Vector setUpVector(int size) {
    Vector vector = new Vector();
    for (int i = 0; i < size; i++) {
      vector.addVariable(new IntegerVariable(INIT, MIN, MAX));
    }
    return vector;
  }

  public SelectionObjectiveFunction getObjectiveFunction(
      List<TestCase> testSuite, TestSuiteCoverage transitionStateCoverage) {
    return new SelectionObjectiveFunction(testSuite, transitionStateCoverage);
  }
}

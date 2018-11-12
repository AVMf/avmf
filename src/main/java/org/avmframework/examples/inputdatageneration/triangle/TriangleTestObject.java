package org.avmframework.examples.inputdatageneration.triangle;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.BranchTargetObjectiveFunction;
import org.avmframework.examples.inputdatageneration.TestObject;
import org.avmframework.variable.IntegerVariable;

public class TriangleTestObject extends TestObject {

  static final int NUM_BRANCHING_NODES = 8;
  static final int INITIAL_VALUE = 0;
  static final int MIN = 0;
  static final int MAX = 1000;

  @Override
  public Vector getVector() {
    Vector vector = new Vector();
    for (int i = 0; i < 3; i++) {
      vector.addVariable(new IntegerVariable(INITIAL_VALUE, MIN, MAX));
    }
    return vector;
  }

  @Override
  public BranchTargetObjectiveFunction getObjectiveFunction(Branch target) {
    return new TriangleBranchTargetObjectiveFunction(target);
  }

  @Override
  public int getNumBranchingNodes() {
    return NUM_BRANCHING_NODES;
  }
}

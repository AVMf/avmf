package org.avmframework.examples.inputdatageneration.line;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.BranchTargetObjectiveFunction;
import org.avmframework.examples.inputdatageneration.TestObject;
import org.avmframework.variable.FixedPointVariable;

public class LineTestObject extends TestObject {

  static final int NUM_BRANCHING_NODES = 7;

  static final int PRECISION = 1;
  static final double INITIAL_VALUE = 0.0;
  static final double MIN = 0.0;
  static final double MAX = 100.0;

  @Override
  public Vector getVector() {
    Vector vector = new Vector();
    for (int i = 0; i < 8; i++) {
      vector.addVariable(new FixedPointVariable(INITIAL_VALUE, PRECISION, MIN, MAX));
    }
    return vector;
  }

  @Override
  public BranchTargetObjectiveFunction getObjectiveFunction(Branch target) {
    return new LineBranchTargetObjectiveFunction(target);
  }

  @Override
  public int getNumBranchingNodes() {
    return NUM_BRANCHING_NODES;
  }
}

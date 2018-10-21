package org.avmframework.examples.inputdatageneration;

public class BranchExection {
  protected Branch branch;
  protected double distanceToAlternative;

  public BranchExection(int id, boolean outcome, double distanceToAlternative) {
    branch = new Branch(id, outcome);
    this.distanceToAlternative = distanceToAlternative;
  }

  public Branch getBranch() {
    return branch;
  }

  public double getDistanceToAlternative() {
    return distanceToAlternative;
  }

  @Override
  public String toString() {
    return branch.toString() + "(" + distanceToAlternative + ")";
  }
}

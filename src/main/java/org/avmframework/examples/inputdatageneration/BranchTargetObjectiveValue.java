package org.avmframework.examples.inputdatageneration;

import org.avmframework.objective.ObjectiveValue;

public class BranchTargetObjectiveValue extends ObjectiveValue {

  protected int approachLevel;
  protected double branchDistance;

  public BranchTargetObjectiveValue(int approachLevel, double branchDistance) {
    this.approachLevel = approachLevel;
    this.branchDistance = branchDistance;
  }

  @Override
  public boolean isOptimal() {
    return approachLevel == 0 && branchDistance == 0;
  }

  @Override
  public int compareTo(Object obj) {
    BranchTargetObjectiveValue other = (BranchTargetObjectiveValue) obj;
    if (approachLevel < other.approachLevel) {
      return 1;
    } else if (approachLevel > other.approachLevel) {
      return -1;
    } else {
      if (branchDistance < other.branchDistance) {
        return 1;
      } else if (branchDistance > other.branchDistance) {
        return -1;
      }
    }
    return 0;
  }

  @Override
  public String toString() {
    return "Approach Level=" + approachLevel + ", Branch Distance=" + branchDistance;
  }
}

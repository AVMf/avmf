package org.avmframework.examples.inputdatageneration;

import org.avmframework.objective.DistanceFunctions;

import java.util.LinkedList;
import java.util.List;

public class ExecutionTrace {

  protected List<BranchExection> branchExecutions = new LinkedList<>();

  public boolean isTrue(int id, boolean bool) {
    return equals(id, bool, true);
  }

  public boolean isFalse(int id, boolean bool) {
    return equals(id, bool, false);
  }

  public boolean equals(int id, boolean bool1, boolean bool2) {
    return equals(id, bool1 ? 1.0 : 0, bool2 ? 1.0 : 0);
  }

  public boolean equals(int id, double num1, double num2) {
    boolean outcome = (num1 == num2);
    double distanceToAlternative;
    if (outcome) {
      distanceToAlternative = DistanceFunctions.notEquals(num1, num2);
    } else {
      distanceToAlternative = DistanceFunctions.equals(num1, num2);
    }
    branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
    return outcome;
  }

  public boolean notEquals(int id, boolean bool1, boolean bool2) {
    return equals(id, bool1 ? 1.0 : 0, bool2 ? 1.0 : 0);
  }

  public boolean notEquals(int id, double num1, double num2) {
    boolean outcome = (num1 != num2);
    double distanceToAlternative;
    if (outcome) {
      distanceToAlternative = DistanceFunctions.equals(num1, num2);
    } else {
      distanceToAlternative = DistanceFunctions.notEquals(num1, num2);
    }
    branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
    return outcome;
  }

  public boolean lessThanOrEquals(int id, double num1, double num2) {
    boolean outcome = (num1 <= num2);
    double distanceToAlternative;
    if (outcome) {
      distanceToAlternative = DistanceFunctions.greaterThan(num1, num2);
    } else {
      distanceToAlternative = DistanceFunctions.lessThanOrEquals(num1, num2);
    }
    branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
    return outcome;
  }

  public boolean lessThan(int id, double num1, double num2) {
    boolean outcome = (num1 < num2);
    double distanceToAlternative;
    if (outcome) {
      distanceToAlternative = DistanceFunctions.greaterThanOrEquals(num1, num2);
    } else {
      distanceToAlternative = DistanceFunctions.lessThan(num1, num2);
    }
    branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
    return outcome;
  }

  public boolean greaterThanOrEquals(int id, double num1, double num2) {
    boolean outcome = (num1 >= num2);
    double distanceToAlternative;
    if (outcome) {
      distanceToAlternative = DistanceFunctions.lessThan(num1, num2);
    } else {
      distanceToAlternative = DistanceFunctions.greaterThanOrEquals(num1, num2);
    }
    branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
    return outcome;
  }

  public boolean greaterThan(int id, double num1, double num2) {
    boolean outcome = (num1 > num2);
    double distanceToAlternative;
    if (outcome) {
      distanceToAlternative = DistanceFunctions.lessThanOrEquals(num1, num2);
    } else {
      distanceToAlternative = DistanceFunctions.greaterThan(num1, num2);
    }
    branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
    return outcome;
  }

  public List<BranchExection> getBranchExecutions() {
    return new LinkedList<>(branchExecutions);
  }

  public BranchExection getBranchExecution(int index) {
    return branchExecutions.get(index);
  }

  @Override
  public String toString() {
    return branchExecutions.toString();
  }
}

package org.avmframework.examples.inputdatageneration;

import org.avmframework.objective.DistanceFunctions;

import java.util.LinkedList;
import java.util.List;

public class ExecutionTrace {

    protected List<BranchExection> branchExecutions = new LinkedList<>();

    public boolean equals(int id, double a, double b) {
        boolean outcome = (a == b);
        double distanceToAlternative;
        if (outcome) {
            distanceToAlternative = DistanceFunctions.notEquals(a, b);
        } else {
            distanceToAlternative = DistanceFunctions.equals(a, b);
        }
        branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
        return outcome;
    }

    public boolean notEquals(int id, double a, double b) {
        boolean outcome = (a != b);
        double distanceToAlternative;
        if (outcome) {
            distanceToAlternative = DistanceFunctions.equals(a, b);
        } else {
            distanceToAlternative = DistanceFunctions.notEquals(a, b);
        }
        branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
        return outcome;
    }

    public boolean lessThanOrEquals(int id, double a, double b) {
        boolean outcome = (a <= b);
        double distanceToAlternative;
        if (outcome) {
            distanceToAlternative = DistanceFunctions.greaterThan(a, b);
        } else {
            distanceToAlternative = DistanceFunctions.lessThanOrEquals(a, b);
        }
        branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
        return outcome;
    }

    public boolean lessThan(int id, double a, double b) {
        boolean outcome = (a < b);
        double distanceToAlternative;
        if (outcome) {
            distanceToAlternative = DistanceFunctions.greaterThanOrEquals(a, b);
        } else {
            distanceToAlternative = DistanceFunctions.lessThan(a, b);
        }
        branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
        return outcome;
    }

    public boolean greaterThanOrEquals(int id, double a, double b) {
        boolean outcome = (a >= b);
        double distanceToAlternative;
        if (outcome) {
            distanceToAlternative = DistanceFunctions.lessThan(a, b);
        } else {
            distanceToAlternative = DistanceFunctions.greaterThanOrEquals(a, b);
        }
        branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
        return outcome;
    }

    public boolean greaterThan(int id, double a, double b) {
        boolean outcome = (a > b);
        double distanceToAlternative;
        if (outcome) {
            distanceToAlternative = DistanceFunctions.lessThanOrEquals(a, b);
        } else {
            distanceToAlternative = DistanceFunctions.greaterThan(a, b);
        }
        branchExecutions.add(new BranchExection(id, outcome, distanceToAlternative));
        return outcome;
    }

    public List<BranchExection> getBranchExecutions() {
        return new LinkedList<BranchExection>(branchExecutions);
    }

    public BranchExection getBranchExecution(int index) {
        return branchExecutions.get(index);
    }

    @Override
    public String toString() {
        return branchExecutions.toString();
    }
}


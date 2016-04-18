package org.avmframework.examples.inputgeneration;

import org.avmframework.objective.ObjectiveValue;

public class InputGenerationObjectiveValue extends ObjectiveValue<InputGenerationObjectiveValue> {

    private int approachLevel;
    private double branchDistance;

    public InputGenerationObjectiveValue(int approachLevel, double branchDistance) {
        this.approachLevel = approachLevel;
        this.branchDistance = branchDistance;
    }

    public int getApproachLevel() {
        return approachLevel;
    }

    public double getBranchDistance() {
        return branchDistance;
    }

    @Override
    public boolean isOptimal() {
        return approachLevel == 0 && branchDistance == 0;
    }

    @Override
    public int compareTo(InputGenerationObjectiveValue other) {
        if (approachLevel < other.approachLevel) {
            return -1;
        } else if (approachLevel > other.approachLevel) {
            return 1;
        } else {
            if (branchDistance < other.branchDistance) {
                return -1;
            } else if (branchDistance > other.branchDistance) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

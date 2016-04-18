package org.avmframework.localsearch.tiebreaking;

import org.avmframework.objective.ObjectiveValue;

public class UseBest extends TiedDirectionPolicy {

    protected TiedDirectionPolicy tdp;

    public UseBest(TiedDirectionPolicy tdp) {
        this.tdp = tdp;
    }

    @Override
    public int resolveDirection(ObjectiveValue left, ObjectiveValue right) {
        if (left.compareTo(right) == 0) {
            return tdp.resolveDirection(left, right);
        }

        return left.betterThan(right) ? -1 : 1;
    }
}

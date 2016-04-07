package org.avmframework.localsearch;

import org.avmframework.objective.ObjectiveValue;

public class UseBest extends TiedDirectionPolicy {

    @Override
    public int resolveDirection(ObjectiveValue left, ObjectiveValue right) {
        return left.betterThan(right) ? -1 : 1;
    }
}

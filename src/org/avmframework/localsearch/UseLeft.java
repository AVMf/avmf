package org.avmframework.localsearch;

import org.avmframework.objective.ObjectiveValue;

public class UseLeft extends TiedDirectionPolicy {

    @Override
    public int resolveDirection(ObjectiveValue left, ObjectiveValue right) {
        return -1;
    }
}

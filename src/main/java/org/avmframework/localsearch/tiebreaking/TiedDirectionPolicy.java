package org.avmframework.localsearch.tiebreaking;

import org.avmframework.objective.ObjectiveValue;

public abstract class TiedDirectionPolicy {

    public abstract int resolveDirection(ObjectiveValue left, ObjectiveValue right);
}

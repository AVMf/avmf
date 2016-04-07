package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.objective.ObjectiveValue;

public class UseRandom extends TiedDirectionPolicy {

    protected RandomGenerator rg;

    public UseRandom(RandomGenerator rg) {
        this.rg = rg;
    }

    @Override
    public int resolveDirection(ObjectiveValue left, ObjectiveValue right) {
        return rg.nextBoolean() ? -1 : 1;
    }
}

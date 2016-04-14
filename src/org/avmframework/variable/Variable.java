package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public abstract class Variable {

    public abstract void setValueToInitial();

    public abstract void setValueToRandom(RandomGenerator randomGenerator);

    public abstract Variable deepCopy();

}

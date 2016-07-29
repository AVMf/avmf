package org.avmframework.examples.inputdatageneration;

import org.avmframework.Vector;

public abstract class TestObject {

    public abstract Vector getVector();

    public abstract BranchTargetObjectiveFunction getObjectiveFunction(Branch target);
}

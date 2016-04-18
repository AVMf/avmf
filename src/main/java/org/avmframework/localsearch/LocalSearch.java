package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.AtomicVariable;

public abstract class LocalSearch {

    public abstract void search(AtomicVariable variable, Vector vector, ObjectiveFunction objFun) throws TerminationException;
}

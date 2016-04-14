package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.VectorVariable;

public abstract class LocalSearch {

    public abstract void search(AtomicVariable variable, Vector vector, ObjectiveFunction objFun) throws TerminationException;

    public abstract void search(VectorVariable variable, Vector vector, ObjectiveFunction objFun) throws TerminationException;
}

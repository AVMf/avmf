package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.AtomicVariable;

public abstract class LocalSearch {

    protected AtomicVariable var;
    protected Vector vector;
    protected ObjectiveFunction objFun;

    public void search(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        this.var = var;
        this.objFun = objFun;
        this.vector = vector;

        performSearch();
    }

    protected abstract void performSearch() throws TerminationException;
}

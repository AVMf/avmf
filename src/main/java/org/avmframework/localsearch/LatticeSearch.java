package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.localsearch.tiebreaking.TiedDirectionPolicy;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.AtomicVariable;

public class LatticeSearch extends PatternSearch {

    public LatticeSearch() {
    }

    public LatticeSearch(TiedDirectionPolicy tdp) {
        super(tdp);
    }

    public void search(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {

    }
}

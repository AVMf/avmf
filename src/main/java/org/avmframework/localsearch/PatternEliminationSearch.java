package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;

public abstract class PatternEliminationSearch extends PatternSearch {

    public PatternEliminationSearch() {
    }

    public PatternEliminationSearch(RandomGenerator rg) {
        super(rg);
    }

    protected void performSearch() throws TerminationException {
        initialize();
        if (establishDirection()) {
            patternSearch();
            eliminationSearch();
        }
    }

    protected void eliminationSearch() throws TerminationException {
        int xPrev = x - k / var.getAccelerationFactor();
        int xNext = x + k;

        int l = Math.min(xPrev, xNext);
        int r = Math.max(xPrev, xNext);

        performEliminationSearch(l, r);
    }

    protected abstract void performEliminationSearch(int l, int r) throws TerminationException;
}

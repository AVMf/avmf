package org.avmframework.localsearch;

import org.avmframework.TerminationException;

public abstract class PatternThenEliminationSearch extends PatternSearch {

    public PatternThenEliminationSearch() {
    }

    public PatternThenEliminationSearch(int accelerationFactor) {
        super(accelerationFactor);
    }

    protected void performSearch() throws TerminationException {
        initialize();
        if (establishDirection()) {
            patternSearch();
            eliminationSearch();
        }
    }

    protected void eliminationSearch() throws TerminationException {
        int xPrev = x - (k * dir / accelerationFactor);
        int xNext = x + (k * dir);

        int l = Math.min(xPrev, xNext);
        int r = Math.max(xPrev, xNext);

        performEliminationSearch(l, r);
    }

    protected abstract void performEliminationSearch(int l, int r) throws TerminationException;
}

package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

public class IteratedPatternSearch extends PatternSearch {

    public IteratedPatternSearch() {
    }

    public IteratedPatternSearch(RandomGenerator rg) {
        super(rg);
    }

    protected void performSearch() throws TerminationException {
        ObjectiveValue next = objFun.evaluate(vector), last;

        do {
            initialize();
            if (establishDirection()) {
                patternSearch();
            }

            last = next;
            next = objFun.evaluate(vector);
        } while (next.betterThan(last));
    }
}

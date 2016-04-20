package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.*;

public class IteratedPatternSearch extends PatternSearch {

    public IteratedPatternSearch() {
    }

    public IteratedPatternSearch(RandomGenerator rg) {
        super(rg);
    }

    public void search(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        ObjectiveValue next = objFun.evaluate(vector), last;

        do {
            super.search(var, vector, objFun);
            last = next;
            next = objFun.evaluate(vector);
        } while (next.betterThan(last));
    }
}

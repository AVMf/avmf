package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class GeometricSearch extends PatternEliminationSearch {

    public GeometricSearch() {
    }

    public GeometricSearch(RandomGenerator rg) {
        super(rg);
    }

    protected void performEliminationSearch(int l, int r) throws TerminationException {
        while (l < r) {
            int m = (int) Math.floor((l + r) / 2.0);
            int n = m + 1;

            var.setValue(m);
            ObjectiveValue mObj = objFun.evaluate(vector);

            var.setValue(n);
            ObjectiveValue nObjVal = objFun.evaluate(vector);

            if (mObj.betterThan(nObjVal)) {
                r = m;
            } else {
                l = n;
            }
        }
    }
}

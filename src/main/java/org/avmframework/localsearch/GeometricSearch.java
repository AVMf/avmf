package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class GeometricSearch extends PatternSearch {

    public GeometricSearch() {
    }

    public GeometricSearch(RandomGenerator rg) {
        super(rg);
    }

    public void search(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        super.search(var, vector, objFun);

        if (dir == 0) {
            return;
        }

        int xPrev = x - k / var.getAccelerationFactor();
        int xNext = x + k;

        int l = Math.min(xPrev, xNext);
        int r = Math.max(xPrev, xNext);

        while (l < r) {
            int mid = (int) Math.floor((l + r) / 2.0);
            int midPlusOne = mid + 1;

            var.setValue(mid);
            ObjectiveValue midObjVal = objFun.evaluate(vector);

            var.setValue(midPlusOne);
            ObjectiveValue midPlusStepObjVal = objFun.evaluate(vector);

            if (midObjVal.betterThan(midPlusStepObjVal)) {
                r = mid;
            } else {
                l = midPlusOne;
            }
        }
    }
}

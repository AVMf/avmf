package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.localsearch.tiebreaking.TiedDirectionPolicy;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class GeometricSearch extends PatternSearch {

    public GeometricSearch() {
    }

    public GeometricSearch(TiedDirectionPolicy tdp) {
        super(tdp);
    }

    public void search(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        super.search(var, vector, objFun);

        if (dir == 0) {
            return;
        }

        int xPrev = x - k / var.getAccelerationFactor();
        int xNext = x + k;

        int min = Math.min(xPrev, xNext);
        int max = Math.max(xPrev, xNext);

        while (min < max) {
            int mid = (int) Math.floor((min + max) / 2.0);
            int midPlusStep = mid + 1;

            var.setValue(mid);
            ObjectiveValue midObjVal = objFun.evaluate(vector);

            var.setValue(midPlusStep);
            ObjectiveValue midPlusStepObjVal = objFun.evaluate(vector);

            if (midObjVal.betterThan(midPlusStepObjVal)) {
                max = mid;
            } else {
                min = midPlusStep;
            }
        }
    }
}

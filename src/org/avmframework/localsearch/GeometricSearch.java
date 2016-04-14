package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.localsearch.tiebreaking.TiedDirectionPolicy;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class GeometricSearch extends PatternSearch {

    public GeometricSearch(Vector vector, ObjectiveFunction objFun, TiedDirectionPolicy tdp) {
        super(vector, objFun, tdp);
    }

    public void search(AtomicVariable var) throws TerminationException {
        super.search(var);

        if (dir == 0) {
            return;
        }

        int xPrev = x - k / var.getAccelerationFactor();
        int xNext = x + k;

        int min = Math.min(xPrev, xNext);
        int max = Math.max(xPrev, xNext);

        while (min < max) {
            int mid = (int) Math.floor((min + max) / 2.0);
            int midPlusStep = mid + var.getStepSize();

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

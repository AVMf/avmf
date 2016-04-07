package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class PatternSearch {

    protected AtomicVariable var;
    protected Vector vector;
    protected ObjectiveFunction objFun;
    protected TiedDirectionPolicy tdp;

    protected ObjectiveValue initial, left, right;
    protected int k, x;

    public PatternSearch(Vector vector, ObjectiveFunction objFun, TiedDirectionPolicy tdp) {
        this.objFun = objFun;
        this.vector = vector;
        this.tdp = tdp;
    }

    public void search(AtomicVariable var) throws TerminationException {
        this.var = var;
        initial = objFun.evaluate(vector);
        k = var.getStepSize();
        x = var.getValue();

        int dir = establishDirection();
        if (dir == 0) {
            return;
        }
        doPatternMoves(dir);
    }

    protected int establishDirection() throws TerminationException {
        left = evaluateValue(x - k);
        right = evaluateValue(x + k);

        boolean leftBetter = left.betterThan(initial);
        boolean rightBetter = right.betterThan(initial);

        if (!leftBetter && !rightBetter) {
            return 0;
        }

        if (leftBetter && rightBetter) {
            return tdp.resolveDirection(left, right);
        }

        return leftBetter ? -1 : 1;
    }

    protected void doPatternMoves(int dir) throws TerminationException {
        ObjectiveValue last = initial;
        ObjectiveValue next = (dir == 1) ? right : left;

        while (next.betterThan(last)) {
            k *= var.getAccelerationFactor();
            x += k * dir;
            last = next;
            next = evaluateValue(x);
        }

        x -= k * dir;
        var.setValue(x);
    }

    protected ObjectiveValue evaluateValue(int value) throws TerminationException {
        var.setValue(value);
        return objFun.evaluate(vector);
    }
}

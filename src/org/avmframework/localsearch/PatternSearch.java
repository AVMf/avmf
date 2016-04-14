package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.localsearch.tiebreaking.TiedDirectionPolicy;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class PatternSearch {

    protected AtomicVariable var;
    protected Vector vector;
    protected ObjectiveFunction objFun;
    protected TiedDirectionPolicy tdp;

    protected ObjectiveValue initial, last, next;
    protected int k, x, dir;

    public PatternSearch(Vector vector, ObjectiveFunction objFun, TiedDirectionPolicy tdp) {
        this.objFun = objFun;
        this.vector = vector;
        this.tdp = tdp;
    }

    public void search(AtomicVariable var) throws TerminationException {
        this.var = var;
        initialize();
        establishDirection();
        doPatternMoves();
    }

    protected void initialize() throws TerminationException {
        initial = objFun.evaluate(vector);
        k = var.getStepSize();
        x = var.getValue();
        dir = 0;
    }

    protected void establishDirection() throws TerminationException {
        // evaluate left move
        var.setValue(x - k);
        ObjectiveValue left = objFun.evaluate(vector);

        // evaluate right move
        var.setValue(x + k);
        ObjectiveValue right = objFun.evaluate(vector);

        boolean leftBetter = left.betterThan(initial);
        boolean rightBetter = right.betterThan(initial);

        last = initial;

        if (leftBetter && rightBetter) {
            dir = tdp.resolveDirection(left, right);
            next = (dir == -1) ? left : right;
        } else if (leftBetter) {
            dir = -1;
            next = left;
        } else if (rightBetter) {
            dir = 1;
            next = right;
        } else {
            dir = 0;
            next = initial;
        }

        // set x and the variable according to the best outcome
        x += dir * k;
        var.setValue(x);
    }

    protected void doPatternMoves() throws TerminationException {

        while (next.betterThan(last)) {
            last = next;

            // make the pattern move
            k *= var.getAccelerationFactor();
            x += k * dir;
            var.setValue(x);

            // evaluate the move
            next = objFun.evaluate(vector);

            // if no better, reset x and the variable
            if (!next.betterThan(last)) {
                x -= k * dir;
                var.setValue(x);
            }
        }
    }
}

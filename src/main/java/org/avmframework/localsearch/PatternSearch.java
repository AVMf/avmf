package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

public class PatternSearch extends LocalSearch {

    protected RandomGenerator rg = null;

    protected AtomicVariable var;
    protected Vector vector;
    protected ObjectiveFunction objFun;

    protected ObjectiveValue initial, last, next;
    protected int k, x, dir;

    public PatternSearch() {
    }

    public PatternSearch(RandomGenerator rg) {
        this.rg = rg;
    }

    public void search(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        this.var = var;
        this.objFun = objFun;
        this.vector = vector;
        initialize();
        establishDirection();
        doPatternMoves();
    }

    protected void initialize() throws TerminationException {
        initial = objFun.evaluate(vector);
        k = 1;
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

        // find the best direction
        boolean leftBetter = left.betterThan(initial);
        boolean rightBetter = right.betterThan(initial);
        if (leftBetter && rightBetter) {
            if (rg == null) {
                dir = -1;
            } else {
                dir = rg.nextBoolean() ? -1 : 1;
            }
        } else if (leftBetter) {
            dir = -1;
        } else if (rightBetter) {
            dir = 1;
        } else {
            dir = 0;
        }

        // set x and the variable according to the best outcome
        x += dir * k;
        var.setValue(x);

        // set last and next objective values
        last = initial;
        if (dir == -1) {
            next = left;
        } else if (dir == 1) {
            next = right;
        } else if (dir == 0) {
            next = initial;
        }
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

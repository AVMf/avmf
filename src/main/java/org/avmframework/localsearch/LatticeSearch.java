package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;

import java.util.ArrayList;
import java.util.List;

import static org.avmframework.localsearch.IntegerFibonacciNumbers.fibonacci;
import static org.avmframework.localsearch.IntegerFibonacciNumbers.positionOfSmallestFibonacciNumberGreaterOrEqualTo;

public class LatticeSearch extends PatternSearch {

    public LatticeSearch() {
    }

    public LatticeSearch(RandomGenerator rg) {
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

        int n = positionOfSmallestFibonacciNumberGreaterOrEqualTo(r - l + 2);
        int minN = positionOfSmallestFibonacciNumberGreaterOrEqualTo(2);

        while (n > 2) { // is this constant 2 or 3?

            int nMinus1 = l + fibonacci(n - 1) - 1;
            int nMinus2 = l + fibonacci(n - 2) - 1;

            var.setValue(nMinus1);
            ObjectiveValue nMinus1ObjVal = objFun.evaluate(vector);

            var.setValue(nMinus2);
            ObjectiveValue nMinus2ObjVal = objFun.evaluate(vector);

            if (l + fibonacci(n - 1) - 1 <= r && nMinus1ObjVal.worseThan(nMinus2ObjVal)) {
                l += fibonacci(n - 2);
            }

            n --;
        }
    }
}

class IntegerFibonacciNumbers {

    static List<Integer> numbers = new ArrayList<>();

    static {
        long lastButOne = 0;
        long last = 1;

        numbers.add((int) lastButOne);
        numbers.add((int) last);

        boolean underIntMaxValue = true;

        while (underIntMaxValue) {
            long next = last + lastButOne;

            if (next > Integer.MAX_VALUE) {
                underIntMaxValue = false;
            } else {
                lastButOne = last;
                last = next;

                numbers.add((int) next);
            }
        }
    }

    static int fibonacci(int position) {
        if (position < 0 || position >= maxPosition()) {
            throw new IntegerFibonacciNumberException(
                    "Cannot return fibonacci number at position " + position +
                            " in sequences. Positions range from 0 to " + maxPosition());
        }
        return numbers.get(position);
    }

    static int maxPosition() {
        return numbers.size();
    }

    static int positionOfSmallestFibonacciNumberGreaterOrEqualTo(int target) {
        int n = -1;
        do {
            n ++;
        } while (n < maxPosition() && fibonacci(n) < target);
        return n;
    }
}

class IntegerFibonacciNumberException extends RuntimeException {

    IntegerFibonacciNumberException(String message) {
        super(message);
    }
}
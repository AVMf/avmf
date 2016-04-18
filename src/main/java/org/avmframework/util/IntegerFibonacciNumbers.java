package org.avmframework.util;

import java.util.ArrayList;
import java.util.List;

public class IntegerFibonacciNumbers {

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

    public static int fibonacci(int index) {
        return numbers.get(index);
    }

    public static int lengthOfIntegerFibonacciSequence() {
        return numbers.size();
    }
}

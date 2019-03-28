package org.avmframework.localsearch;

import java.util.ArrayList;
import java.util.List;

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
          "Cannot return fibonacci number at position "
              + position
              + " in sequences. Positions range from 0 to "
              + maxPosition());
    }
    return numbers.get(position);
  }

  static int maxPosition() {
    return numbers.size();
  }

  static int positionOfSmallestFibonacciNumberGreaterOrEqualTo(int target) {
    int num = -1;
    do {
      num++;
    }
    while (num < maxPosition() && fibonacci(num) < target);
    return num;
  }
}

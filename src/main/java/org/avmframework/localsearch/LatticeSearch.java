package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

import java.util.ArrayList;
import java.util.List;

import static org.avmframework.localsearch.IntegerFibonacciNumbers.fibonacci;
import static org.avmframework.localsearch.IntegerFibonacciNumbers.positionOfSmallestFibonacciNumberGreaterOrEqualTo;

public class LatticeSearch extends PatternThenEliminationSearch {

  public LatticeSearch() {}

  public LatticeSearch(int accelerationFactor) {
    super(accelerationFactor);
  }

  protected void performEliminationSearch(int left, int right) throws TerminationException {

    int interval = right - left + 2;
    int num = positionOfSmallestFibonacciNumberGreaterOrEqualTo(interval);

    while (num > 3) {

      int mid = left + fibonacci(num - 2) - 1;
      int midRight = left + fibonacci(num - 1) - 1;

      if (midRight <= right) {

        var.setValue(mid);
        ObjectiveValue midObjVal = objFun.evaluate(vector);

        var.setValue(midRight);
        ObjectiveValue midRightObjVal = objFun.evaluate(vector);

        if (midObjVal.worseThan(midRightObjVal)) {
          left = mid + 1;
        }
      }

      num--;
    }

    var.setValue(left);
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
    int n = -1;
    do {
      n++;
    } while (n < maxPosition() && fibonacci(n) < target);
    return n;
  }
}

class IntegerFibonacciNumberException extends RuntimeException {

  IntegerFibonacciNumberException(String message) {
    super(message);
  }
}

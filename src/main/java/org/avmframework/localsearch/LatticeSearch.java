package org.avmframework.localsearch;

import static org.avmframework.localsearch.IntegerFibonacciNumbers.fibonacci;
import static org.avmframework.localsearch.IntegerFibonacciNumbers.positionOfSmallestFibonacciNumberGreaterOrEqualTo;

import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

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

package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

public class GeometricSearch extends PatternThenEliminationSearch {

  public GeometricSearch() {}

  public GeometricSearch(int accelerationFactor) {
    super(accelerationFactor);
  }

  protected void performEliminationSearch(int l, int r) throws TerminationException {
    while (l < r) {
      int mid = (int) Math.floor((l + r) / 2.0);
      int midRight = mid + 1;

      var.setValue(mid);
      ObjectiveValue midObjVal = objFun.evaluate(vector);

      var.setValue(midRight);
      ObjectiveValue midRightObjVal = objFun.evaluate(vector);

      if (midObjVal.betterThan(midRightObjVal)) {
        r = mid;
      } else {
        l = midRight;
      }
    }

    var.setValue(l);
  }
}

package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

public class IteratedPatternSearch extends PatternSearch {

  public IteratedPatternSearch() {}

  public IteratedPatternSearch(int accelerationFactor) {
    super(accelerationFactor);
  }

  protected void performSearch() throws TerminationException {
    ObjectiveValue next = objFun.evaluate(vector);
    ObjectiveValue last;

    do {
      initialize();
      if (establishDirection()) {
        patternSearch();
      }

      last = next;
      next = objFun.evaluate(vector);
    }
    while (next.betterThan(last));
  }
}

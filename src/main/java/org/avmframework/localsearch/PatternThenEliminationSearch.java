package org.avmframework.localsearch;

import org.avmframework.TerminationException;

public abstract class PatternThenEliminationSearch extends PatternSearch {

  public PatternThenEliminationSearch() {}

  public PatternThenEliminationSearch(int accelerationFactor) {
    super(accelerationFactor);
  }

  protected void performSearch() throws TerminationException {
    initialize();
    if (establishDirection()) {
      patternSearch();
      eliminationSearch();
    }
  }

  protected void eliminationSearch() throws TerminationException {
    int numPrev = num - (modifier * dir / accelerationFactor);
    int numNext = num + (modifier * dir);

    int left = Math.min(numPrev, numNext);
    int right = Math.max(numPrev, numNext);

    performEliminationSearch(left, right);
  }

  protected abstract void performEliminationSearch(int left, int right) throws TerminationException;
}

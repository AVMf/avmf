package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

public class PatternSearch extends LocalSearch {

  public static int ACCELERATION_FACTOR_DEFAULT = 2;

  protected int accelerationFactor = ACCELERATION_FACTOR_DEFAULT;

  protected ObjectiveValue initial;
  protected ObjectiveValue last;
  protected ObjectiveValue next;
  protected int modifier;
  protected int num;
  protected int dir;

  public PatternSearch() {}

  public PatternSearch(int accelerationFactor) {
    this.accelerationFactor = accelerationFactor;
  }

  protected void performSearch() throws TerminationException {
    initialize();
    if (establishDirection()) {
      patternSearch();
    }
  }

  protected void initialize() throws TerminationException {
    initial = objFun.evaluate(vector);
    modifier = 1;
    num = var.getValue();
    dir = 0;
  }

  protected boolean establishDirection() throws TerminationException {
    // evaluate left move
    var.setValue(num - modifier);
    ObjectiveValue left = objFun.evaluate(vector);

    // evaluate right move
    var.setValue(num + modifier);
    ObjectiveValue right = objFun.evaluate(vector);

    // find the best direction
    boolean leftBetter = left.betterThan(initial);
    boolean rightBetter = right.betterThan(initial);
    if (leftBetter) {
      dir = -1;
    } else if (rightBetter) {
      dir = 1;
    } else {
      dir = 0;
    }

    // set num and the variable according to the best edge
    num += dir * modifier;
    var.setValue(num);

    // set last and next objective values
    last = initial;
    if (dir == -1) {
      next = left;
    } else if (dir == 1) {
      next = right;
    } else if (dir == 0) {
      next = initial;
    }

    return dir != 0;
  }

  protected void patternSearch() throws TerminationException {

    while (next.betterThan(last)) {
      last = next;

      // make the pattern move
      modifier *= accelerationFactor;
      num += modifier * dir;
      var.setValue(num);

      // evaluate the move
      next = objFun.evaluate(vector);

      // if no better, reset num and the variable
      if (!next.betterThan(last)) {
        num -= modifier * dir;
        var.setValue(num);
      }
    }
  }
}

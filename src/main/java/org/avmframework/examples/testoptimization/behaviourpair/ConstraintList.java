package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstraintList {

  /** String: variable's name ArrayList: e * 3 0. min 1. max 2. type(0-numerical, 1-categorical) */
  private HashMap<String, List<Integer>> constraints;

  public ConstraintList() {
    this.constraints = new HashMap<String, List<Integer>>();
  }

  public void addOneConstraint(String name, int min, int max, int type) {
    List<Integer> constraint = new ArrayList<Integer>();
    constraint.add(min);
    constraint.add(max);
    constraint.add(type);
    this.constraints.put(name, constraint);
  }

  public HashMap<String, List<Integer>> getconstraints() {
    return this.constraints;
  }
}

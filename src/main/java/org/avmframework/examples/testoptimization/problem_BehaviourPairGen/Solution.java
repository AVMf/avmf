package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.*;

public class Solution {

  /** Parse One Behaviour Pair to One Solution */
  private HashMap<String, Object> solution;

  private int solutionlength;

  public Solution() {
    this.solution = new HashMap<String, Object>();
    this.solutionlength = 0;
  }

  public int getsolutionlength() {
    return this.solutionlength;
  }

  public void setsolutionlength(int len) {
    this.solutionlength = len;
  }

  public void addsolutionmember(String membername, Object o) {
    this.solution.put(membername, o);
    this.solutionlength++;
  }

  public void addsolutionmemberWithoutChangeLength(String membername, Object o) {
    this.solution.put(membername, o);
  }

  public void emptysolution() {
    this.solution = new HashMap<String, Object>();
    this.solutionlength = 0;
  }

  public HashMap<String, Object> getsolution() {
    return this.solution;
  }

  public static void printSolution(Solution v) {
    String printstring = "Solution -";
    Iterator iter = v.getsolution().entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      String key = (String) entry.getKey();
      Object val = entry.getValue();
      printstring = printstring + " " + key + ":" + val;
    }
    System.out.println(printstring);
  }
}

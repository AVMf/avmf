package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Solution {

  /** Parse One Behaviour Pair to One Solution. */
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

  public void addSolutionMember(String membername, Object obj) {
    this.solution.put(membername, obj);
    this.solutionlength++;
  }

  public void addSolutionMemberWithoutChangeLength(String membername, Object obj) {
    this.solution.put(membername, obj);
  }

  public void emptysolution() {
    this.solution = new HashMap<String, Object>();
    this.solutionlength = 0;
  }

  public HashMap<String, Object> getsolution() {
    return this.solution;
  }

  public static void printSolution(Solution sol) {
    String printstring = "Solution -";
    Iterator iter = sol.getsolution().entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      String key = (String) entry.getKey();
      Object val = entry.getValue();
      printstring = printstring + " " + key + ":" + val;
    }
    System.out.println(printstring);
  }
}

package org.avmframework;

import static junit.framework.TestCase.assertTrue;
import static org.avmframework.AlternatingVariableMethods.anyAlternatingVariableMethodWithTerminationPolicy;
import static org.avmframework.ObjectiveFunctions.flat;
import static org.avmframework.Vectors.singleIntegerVector;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestTermination {

  @Test
  public void testNoRestart() {
    testRestarts(0);
  }

  @Test
  public void testOneRestart() {
    testRestarts(1);
  }

  @Test
  public void testTwoRestarts() {
    testRestarts(2);
  }

  protected void testRestarts(int limit) {
    TerminationPolicy tp = TerminationPolicy.createMaxRestartsTerminationPolicy(limit);
    AlternatingVariableMethod avm = anyAlternatingVariableMethodWithTerminationPolicy(tp);
    Monitor monitor = avm.search(singleIntegerVector(), flat());
    assertEquals(limit, monitor.getNumRestarts());
  }

  @Test
  public void testNoEvaluations() {
    testEvaluations(0);
  }

  @Test
  public void testOneEvaluation() {
    testEvaluations(1);
  }

  @Test
  public void testOneHundredEvaluations() {
    testEvaluations(100);
  }

  protected void testEvaluations(int limit) {
    TerminationPolicy tp = TerminationPolicy.createMaxEvaluationsTerminationPolicy(limit);
    AlternatingVariableMethod avm = anyAlternatingVariableMethodWithTerminationPolicy(tp);
    Monitor monitor = avm.search(singleIntegerVector(), flat());
    assertEquals(limit, monitor.getNumEvaluations());
  }

  @Test
  public void testTimeLimit() {
    int minTime = 5;
    TerminationPolicy tp = TerminationPolicy.createRunningTimeTerminationPolicy(minTime);
    AlternatingVariableMethod avm = anyAlternatingVariableMethodWithTerminationPolicy(tp);
    Monitor monitor = avm.search(singleIntegerVector(), flat());
    assertTrue(minTime <= monitor.getRunningTime());
  }
}

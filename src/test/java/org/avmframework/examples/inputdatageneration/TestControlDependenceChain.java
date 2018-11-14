package org.avmframework.examples.inputdatageneration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestControlDependenceChain {

  @Test
  public void testDivergence() {
    ExampleControlDependencies example = new ExampleControlDependencies();

    ControlDependenceChain depChain = example.getControlDependenceChain(new Branch(5, true));
    ExecutionTrace trace = new ExecutionTrace();
    trace.equals(1, 0, 0);
    trace.equals(2, 5, 5);
    trace.equals(3, 10, 0);

    DivergencePoint dp = depChain.getDivergencePoint(trace);
    assertEquals("Position is 2 in the dependence chain", 2, dp.chainIndex);
    assertEquals("Position is 2 in the execution trace", 2, dp.traceIndex);
  }
}

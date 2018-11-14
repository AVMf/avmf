package org.avmframework.examples.inputdatageneration.line;

import static org.junit.Assert.assertEquals;

import org.avmframework.examples.inputdatageneration.ExecutionTrace;
import org.junit.Test;

public class TestLineObjectiveFunction {

  class LineObjectiveFunctionTrace extends LineBranchTargetObjectiveFunction {

    public LineObjectiveFunctionTrace() {
      super(null);
    }

    public ExecutionTrace getTrace(Line line1, Line line2) {
      trace = new ExecutionTrace();
      intersect(line1, line2);
      return trace;
    }
  }

  @Test
  public void testTraceSecondLineStartsWhereFirstLineEnds() {
    LineObjectiveFunctionTrace loft = new LineObjectiveFunctionTrace();

    ExecutionTrace trace = loft.getTrace(new Line(10, 10, 20, 20), new Line(20, 20, 30, 30));
    assertEquals("ExecutionTrace should be of length 2", 2,
        trace.getBranchExecutions().size());
    assertEquals("1st entry should be 1F", "1F",
        trace.getBranchExecution(0).getBranch().toString());
    assertEquals("2nd entry should be 6T", "6T",
        trace.getBranchExecution(1).getBranch().toString());
  }

  @Test
  public void testTraceLinesIntersect() {
    LineObjectiveFunctionTrace loft = new LineObjectiveFunctionTrace();

    ExecutionTrace trace = loft.getTrace(new Line(10, 10, 20, 20), new Line(10, 20, 20, 10));
    assertEquals("ExecutionTrace should be of length 5", 5,
        trace.getBranchExecutions().size());
    assertEquals("1st entry should be 1T", "1T",
        trace.getBranchExecution(0).getBranch().toString());
    assertEquals("2nd entry should be 2T", "2T",
        trace.getBranchExecution(1).getBranch().toString());
    assertEquals("3rd entry should be 3T", "3T",
        trace.getBranchExecution(2).getBranch().toString());
    assertEquals("4th entry should be 4T", "4T",
        trace.getBranchExecution(3).getBranch().toString());
    assertEquals("5th entry should be 5T", "5T",
        trace.getBranchExecution(4).getBranch().toString());
  }

  @Test
  public void testTraceLinesParallel() {
    LineObjectiveFunctionTrace loft = new LineObjectiveFunctionTrace();

    ExecutionTrace trace = loft.getTrace(new Line(10, 10, 20, 20), new Line(10, 20, 30, 40));
    assertEquals("ExecutionTrace should be of length 3", 3,
        trace.getBranchExecutions().size());
    assertEquals("1st entry should be 1F", "1F",
        trace.getBranchExecution(0).getBranch().toString());
    assertEquals("2nd entry should be 6F", "6F",
        trace.getBranchExecution(1).getBranch().toString());
    assertEquals("3rd entry should be 7F", "7F",
        trace.getBranchExecution(2).getBranch().toString());
  }
}

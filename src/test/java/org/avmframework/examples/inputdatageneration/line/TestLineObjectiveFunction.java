package org.avmframework.examples.inputdatageneration.line;

import org.avmframework.examples.inputdatageneration.ExecutionTrace;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestLineObjectiveFunction {

    @Test
    public void testTrace() {
        class LineObjectiveFunctionTrace extends LineBranchTargetObjectiveFunction {

            public LineObjectiveFunctionTrace() {
                super(null);
            }

            public ExecutionTrace getTrace(Line a, Line b) {
                trace = new ExecutionTrace();
                intersect(a, b);
                return trace;
            }
        }

        LineObjectiveFunctionTrace loft = new LineObjectiveFunctionTrace();

        // Second line starts where first line ends
        ExecutionTrace t = loft.getTrace(new Line(10, 10, 20, 20), new Line(20, 20, 30, 30));
        assertEquals("ExecutionTrace is of length 2", 2, t.getBranchExecutions().size());
        assertEquals("1st entry in 1st trace should be 1f", "1f", t.getBranchExecution(0).getBranch().toString());
        assertEquals("2nd entry in 1st trace should be 6t", "6t", t.getBranchExecution(1).getBranch().toString());

        // Intersection
        t = loft.getTrace(new Line(10, 10, 20, 20), new Line(10, 20, 20, 10));
        assertEquals("ExecutionTrace is of length 5", 5, t.getBranchExecutions().size());
        assertEquals("1st entry in 2nd trace should be 1t", "1t", t.getBranchExecution(0).getBranch().toString());
        assertEquals("2nd entry in 2nd trace should be 2t", "2t", t.getBranchExecution(1).getBranch().toString());
        assertEquals("3rd entry in 2nd trace should be 3t", "3t", t.getBranchExecution(2).getBranch().toString());
        assertEquals("4th entry in 2nd trace should be 4t", "4t", t.getBranchExecution(3).getBranch().toString());
        assertEquals("5th entry in 2nd trace should be 5t", "5t", t.getBranchExecution(4).getBranch().toString());

        // Parallel
        t = loft.getTrace(new Line(10, 10, 20, 20), new Line(10, 20, 30, 40));
        assertEquals("ExecutionTrace is of length 3", 3, t.getBranchExecutions().size());
        assertEquals("1st entry in 3rd trace should be 1f", "1f", t.getBranchExecution(0).getBranch().toString());
        assertEquals("2nd entry in 3rd trace should be 6f", "6f", t.getBranchExecution(1).getBranch().toString());
        assertEquals("3rd entry in 3rd trace should be 7f", "7f", t.getBranchExecution(2).getBranch().toString());
    }

}

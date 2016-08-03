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

        // TODO: split these into three tests and update messages

        // Second line starts where first line ends
        ExecutionTrace t = loft.getTrace(new Line(10, 10, 20, 20), new Line(20, 20, 30, 30));
        assertEquals("ExecutionTrace should be of length 2", 2, t.getBranchExecutions().size());
        assertEquals("1st entry in 1st trace should be 1F", "1F", t.getBranchExecution(0).getBranch().toString());
        assertEquals("2nd entry in 1st trace should be 6T", "6T", t.getBranchExecution(1).getBranch().toString());

        // Intersection
        t = loft.getTrace(new Line(10, 10, 20, 20), new Line(10, 20, 20, 10));
        assertEquals("ExecutionTrace should be of length 5", 5, t.getBranchExecutions().size());
        assertEquals("1st entry in 2nd trace should be 1T", "1T", t.getBranchExecution(0).getBranch().toString());
        assertEquals("2nd entry in 2nd trace should be 2T", "2T", t.getBranchExecution(1).getBranch().toString());
        assertEquals("3rd entry in 2nd trace should be 3T", "3T", t.getBranchExecution(2).getBranch().toString());
        assertEquals("4th entry in 2nd trace should be 4T", "4T", t.getBranchExecution(3).getBranch().toString());
        assertEquals("5th entry in 2nd trace should be 5T", "5T", t.getBranchExecution(4).getBranch().toString());

        // Parallel
        t = loft.getTrace(new Line(10, 10, 20, 20), new Line(10, 20, 30, 40));
        assertEquals("ExecutionTrace should be of length 3", 3, t.getBranchExecutions().size());
        assertEquals("1st entry in 3rd trace should be 1F", "1F", t.getBranchExecution(0).getBranch().toString());
        assertEquals("2nd entry in 3rd trace should be 6F", "6F", t.getBranchExecution(1).getBranch().toString());
        assertEquals("3rd entry in 3rd trace should be 7F", "7F", t.getBranchExecution(2).getBranch().toString());
    }

}

package org.avmframework;

import org.junit.*;

import static org.avmframework.AVMs.anyAVMWithTerminationPolicy;
import static org.avmframework.ObjectiveFunctions.flatObjectiveFunction;
import static org.avmframework.Vectors.singleIntegerVector;
import static org.junit.Assert.assertEquals;

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
        int numRestarts = limit - 1;
        if (numRestarts < 0) {
            numRestarts = 0;
        }

        TerminationPolicy tp = TerminationPolicy.maxRestarts(limit);
        AVM avm = anyAVMWithTerminationPolicy(tp);
        Monitor monitor = avm.search(singleIntegerVector(), flatObjectiveFunction());
        assertEquals(numRestarts, monitor.getNumRestarts());
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
        TerminationPolicy tp = TerminationPolicy.maxEvaluations(limit);
        AVM avm = anyAVMWithTerminationPolicy(tp);
        Monitor monitor = avm.search(singleIntegerVector(), flatObjectiveFunction());
        assertEquals(limit, monitor.getNumEvaluations());
    }
}

package org.avmframework;

import org.avmframework.initialization.DefaultInitializer;
import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.IteratedPatternSearch;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class TestRestartLimit {

    Vector emptyVector = new Vector();

    ObjectiveFunction objFun = new ObjectiveFunction() {
        @Override
        protected ObjectiveValue computeObjectiveValue(Vector vector) {
            return NumericObjectiveValue.HigherIsBetterObjectiveValue(1.0);
        }
    };

    Initializer initializer = new DefaultInitializer();
    LocalSearch ls = new IteratedPatternSearch();

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
        AVM avm = new AVM(ls, tp, initializer);
        Monitor monitor = avm.search(emptyVector, objFun);
        assertEquals(numRestarts, monitor.getNumRestarts());
    }
}

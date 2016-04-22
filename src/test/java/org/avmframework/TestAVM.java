package org.avmframework;

import org.avmframework.initialization.DefaultInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.AtomicVariable;
import org.junit.Test;

import static org.avmframework.AVMs.anyAVM;
import static org.avmframework.ObjectiveFunctions.allZeros;
import static org.avmframework.ObjectiveFunctions.flat;
import static org.avmframework.Vectors.emptyVector;
import static org.avmframework.Vectors.integerVector;
import static org.junit.Assert.assertEquals;

public class TestAVM {

    @Test(expected=EmptyVectorException.class)
    public void testUsingEmptyVector() {
        anyAVM().search(emptyVector(), flat());
    }

    @Test
    public void testOneCycle() {
        Vector vector = integerVector(10, 5);
        ObjectiveFunction objFun = allZeros();

        LocalSearch mockLocalSearch = new LocalSearch() {
            @Override
            protected void performSearch() throws TerminationException {
            }
        };

        AVM avm = new AVM(mockLocalSearch, TerminationPolicy.createMaxRestartsTerminationPolicy(0), new DefaultInitializer());
        Monitor monitor = avm.search(vector, objFun);

        // 1 initial evaluation plus 10 following each variable search (of non-improvement)
        assertEquals(11, monitor.getNumEvaluations());
        assertEquals(1, monitor.getNumUniqueEvaluations());
    }

    @Test
    public void testTwoCycles() {
        Vector vector = integerVector(10, 5);
        ObjectiveFunction objFun = allZeros();

        LocalSearch mockLocalSearch = new LocalSearch() {
            int count = 4;

            @Override
            protected void performSearch() throws TerminationException {
                AtomicVariable var = (AtomicVariable) vector.getVariable(0);
                var.setValue(count);
                if (count > 0) {
                    count--;
                }
            }
        };

        AVM avm = new AVM(mockLocalSearch, TerminationPolicy.createMaxRestartsTerminationPolicy(0), new DefaultInitializer());
        Monitor monitor = avm.search(vector, objFun);

        // 1 initial evaluation plus 5 following each variable search improvement, plus 10 non-improvement.
        assertEquals(16, monitor.getNumEvaluations());
        assertEquals(6, monitor.getNumUniqueEvaluations());
    }
}

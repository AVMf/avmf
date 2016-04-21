package org.avmframework;

import org.avmframework.objective.ObjectiveFunction;
import org.junit.Test;

import static org.avmframework.AVMs.anyAVM;
import static org.avmframework.AVMs.anyAVMWithTerminationPolicy;
import static org.avmframework.ObjectiveFunctions.flat;
import static org.avmframework.ObjectiveFunctions.initialImprovement;
import static org.avmframework.Vectors.emptyVector;
import static org.avmframework.Vectors.integerVector;
import static org.junit.Assert.assertEquals;

public class TestAVM {

    @Test(expected=EmptyVectorException.class)
    public void testUsingEmptyVector() {
        AVM avm = anyAVM();
        avm.search(emptyVector(), flat());
    }

    @Test
    public void testOneCycle() {
        Vector vector = integerVector(10);
        ObjectiveFunction objFun = flat();

        AVM avm = anyAVMWithTerminationPolicy(TerminationPolicy.maxRestarts(0));
        Monitor monitor = avm.search(vector, objFun);

        assertEquals(10, monitor.getNumVariablesSearched());
        assertEquals(1, monitor.getNumVectorCycles());
    }

    @Test
    public void testTwoCycles() {
        Vector vector = integerVector(10);
        ObjectiveFunction objFun = initialImprovement(5);

        AVM avm = anyAVMWithTerminationPolicy(TerminationPolicy.maxRestarts(0));
        Monitor monitor = avm.search(vector, objFun);

        assertEquals(11, monitor.getNumVariablesSearched());
        assertEquals(2, monitor.getNumVectorCycles());
    }
}

package org.avmframework;

import org.junit.Test;

import static org.avmframework.AVMs.anyAVM;
import static org.avmframework.ObjectiveFunctions.flatObjectiveFunction;
import static org.avmframework.Vectors.emptyVector;

public class TestAVM {

    @Test(expected=EmptyVectorException.class)
    public void testUsingEmptyVector() {
        AVM avm = anyAVM();
        avm.search(emptyVector(), flatObjectiveFunction());
    }
}

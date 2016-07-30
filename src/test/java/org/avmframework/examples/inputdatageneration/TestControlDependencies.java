package org.avmframework.examples.inputdatageneration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestControlDependencies {

    @Test
    public void testGetControlDependenceChain() {
        ExampleControlDependencies example = new ExampleControlDependencies();

        ControlDependenceChain depChain = example.getControlDependenceChain(new Branch(5, true));
        assertEquals("Length of control dependence chain for 5t should be 5", 5, depChain.branches.size());
        assertEquals("1st element of chain for 5t should be branch 5t", "5t", depChain.branches.get(0).toString());
        assertEquals("2nd element of chain for 5t should be branch 4t", "4t", depChain.branches.get(1).toString());
        assertEquals("3rd element of chain for 5t should be branch 3t", "3t", depChain.branches.get(2).toString());
        assertEquals("4th element of chain for 5t should be branch 2t", "2t", depChain.branches.get(3).toString());
        assertEquals("5th element of chain for 5t should be branch 1t", "1t", depChain.branches.get(4).toString());

        depChain = example.getControlDependenceChain(new Branch(5, false));
        assertEquals("Length of control dependence chain for 5F should be 5", 5, depChain.branches.size());
        assertEquals("1st element of chain for 5f should be branch 5f", "5f", depChain.branches.get(0).toString());
        assertEquals("2nd element of chain for 5f should be branch 4t", "4t", depChain.branches.get(1).toString());
        assertEquals("3rd element of chain for 5f should be branch 3t", "3t", depChain.branches.get(2).toString());
        assertEquals("4th element of chain for 5f should be branch 2t", "2t", depChain.branches.get(3).toString());
        assertEquals("5th element of chain for 5f should be branch 1t", "1t", depChain.branches.get(4).toString());

        depChain = example.getControlDependenceChain(new Branch(6, true));
        assertEquals("Length of control dependence chain should be 2", 2, depChain.branches.size());
        assertEquals("1st element of chain for 6t should be branch 6t", "6t", depChain.branches.get(0).toString());
        assertEquals("2nd element of chain for 6t should be branch 1f", "1f", depChain.branches.get(1).toString());
    }
}

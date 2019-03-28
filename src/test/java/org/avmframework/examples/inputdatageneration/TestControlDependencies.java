package org.avmframework.examples.inputdatageneration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestControlDependencies {

  @Test
  public void testGetControlDependenceChain() {
    ExampleControlDependencies example = new ExampleControlDependencies();

    ControlDependenceChain depChain = example.getControlDependenceChain(new Branch(5, true));
    assertEquals(
        "Length of control dependence chain for 5T should be 5", 5, depChain.branches.size());
    assertEquals(
        "1st element of chain for 5t should be branch 5T",
        "5T",
        depChain.branches.get(0).toString());
    assertEquals(
        "2nd element of chain for 5t should be branch 4T",
        "4T",
        depChain.branches.get(1).toString());
    assertEquals(
        "3rd element of chain for 5t should be branch 3T",
        "3T",
        depChain.branches.get(2).toString());
    assertEquals(
        "4th element of chain for 5t should be branch 2T",
        "2T",
        depChain.branches.get(3).toString());
    assertEquals(
        "5th element of chain for 5t should be branch 1T",
        "1T",
        depChain.branches.get(4).toString());

    depChain = example.getControlDependenceChain(new Branch(5, false));
    assertEquals(
        "Length of control dependence chain for 5F should be 5", 5, depChain.branches.size());
    assertEquals(
        "1st element of chain for 5f should be branch 5F",
        "5F",
        depChain.branches.get(0).toString());
    assertEquals(
        "2nd element of chain for 5f should be branch 4T",
        "4T",
        depChain.branches.get(1).toString());
    assertEquals(
        "3rd element of chain for 5f should be branch 3T",
        "3T",
        depChain.branches.get(2).toString());
    assertEquals(
        "4th element of chain for 5f should be branch 2T",
        "2T",
        depChain.branches.get(3).toString());
    assertEquals(
        "5th element of chain for 5f should be branch 1T",
        "1T",
        depChain.branches.get(4).toString());

    depChain = example.getControlDependenceChain(new Branch(6, true));
    assertEquals(
        "Length of control dependence chain for 6T should be 2", 2, depChain.branches.size());
    assertEquals(
        "1st element of chain for 6t should be branch 6T",
        "6T",
        depChain.branches.get(0).toString());
    assertEquals(
        "2nd element of chain for 6t should be branch 1F",
        "1F",
        depChain.branches.get(1).toString());
  }
}

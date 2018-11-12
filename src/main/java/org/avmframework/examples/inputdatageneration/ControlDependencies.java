package org.avmframework.examples.inputdatageneration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ControlDependencies {

  protected Map<Branch, Branch> dependencies = new HashMap<>();

  public void add(Branch branch, Branch controlDependency) {
    dependencies.put(branch, controlDependency);
  }

  public ControlDependenceChain getControlDependenceChain(Branch branch) {
    List<Branch> chain = new LinkedList<>();
    Branch current = branch;

    while (current != null) {
      chain.add(current);
      current = dependencies.get(current);
    }
    return new ControlDependenceChain(chain);
  }
}

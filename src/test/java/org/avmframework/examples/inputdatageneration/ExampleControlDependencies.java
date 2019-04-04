package org.avmframework.examples.inputdatageneration;

public class ExampleControlDependencies extends ControlDependencies {

  public ExampleControlDependencies() {
    add(new Branch(1, true), null);
    add(new Branch(1, false), null);

    add(new Branch(2, true), new Branch(1, true));
    add(new Branch(2, false), new Branch(1, true));

    add(new Branch(3, true), new Branch(2, true));
    add(new Branch(3, false), new Branch(2, true));

    add(new Branch(4, true), new Branch(3, true));
    add(new Branch(4, false), new Branch(3, true));

    add(new Branch(5, true), new Branch(4, true));
    add(new Branch(5, false), new Branch(4, true));

    add(new Branch(6, true), new Branch(1, false));
    add(new Branch(6, false), new Branch(1, false));

    add(new Branch(7, true), new Branch(1, false));
    add(new Branch(7, false), new Branch(1, false));
  }
}

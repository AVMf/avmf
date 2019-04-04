package org.avmframework.initialization;

import org.avmframework.Vector;

public class DefaultInitializer extends Initializer {

  @Override
  public void initialize(Vector vector) {
    vector.setVariablesToInitial();
  }
}

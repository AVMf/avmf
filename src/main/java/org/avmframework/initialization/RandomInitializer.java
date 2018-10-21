package org.avmframework.initialization;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.Vector;

public class RandomInitializer extends Initializer {

  protected RandomGenerator rg;

  public RandomInitializer(RandomGenerator rg) {
    this.rg = rg;
  }

  @Override
  public void initialize(Vector vector) {
    vector.setVariablesToRandom(rg);
  }
}

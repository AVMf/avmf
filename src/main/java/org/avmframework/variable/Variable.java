package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public interface Variable {

  void setValueToInitial();

  void setValueToRandom(RandomGenerator randomGenerator);

  Variable deepCopy();
}

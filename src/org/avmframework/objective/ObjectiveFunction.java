package org.avmframework.objective;

import org.avmframework.Vector;

public interface ObjectiveFunction {

    ObjectiveValue compute(Vector vector);
}

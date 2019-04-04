package org.avmframework.objective;

import org.avmframework.Monitor;
import org.avmframework.TerminationException;
import org.avmframework.Vector;

import java.util.HashMap;
import java.util.Map;

public abstract class ObjectiveFunction {

  public static final boolean USE_CACHE_DEFAULT = true;

  protected boolean useCache = USE_CACHE_DEFAULT;
  protected Map<Vector, ObjectiveValue> previousVals = new HashMap<>();
  protected Monitor monitor;

  public void setMonitor(Monitor monitor) {
    this.monitor = monitor;
  }

  public ObjectiveValue evaluate(Vector vector) throws TerminationException {
    monitor.observeVector();

    if (useCache && previousVals.containsKey(vector)) {
      return previousVals.get(vector);
    }

    ObjectiveValue objVal = computeObjectiveValue(vector);

    if (useCache) {
      previousVals.put(vector.deepCopy(), objVal);
    }

    if (monitor != null) {
      monitor.observePreviouslyUnseenVector(vector, objVal);
    }

    return objVal;
  }

  protected abstract ObjectiveValue computeObjectiveValue(Vector vector);
}

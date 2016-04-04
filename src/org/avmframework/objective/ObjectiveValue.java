package org.avmframework.objective;

public interface ObjectiveValue<T extends ObjectiveValue> extends Comparable<T> {

    boolean isOptimal();
}

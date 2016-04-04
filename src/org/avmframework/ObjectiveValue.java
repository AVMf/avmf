package org.avmframework;

public interface ObjectiveValue<T extends ObjectiveValue> extends Comparable<T> {

    boolean isOptimal();
}

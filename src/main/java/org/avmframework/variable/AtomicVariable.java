package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public abstract class AtomicVariable extends Variable {

    public static final int MIN_DEFAULT = Integer.MIN_VALUE;
    public static final int MAX_DEFAULT = Integer.MAX_VALUE;
    public static final int ACCELERATION_FACTOR_DEFAULT = 2;

    protected int min = MIN_DEFAULT;
    protected int max = MAX_DEFAULT;
    protected int accelerationFactor = ACCELERATION_FACTOR_DEFAULT;

    protected int initialValue;
    protected int value;

    protected AtomicVariable() {
    }

    public AtomicVariable(int initialValue) {
        this.initialValue = initialValue;
        setValueToInitial();
    }

    public AtomicVariable(int initialValue, int min, int max) {
        this.initialValue = initialValue;
        this.min = min;
        this.max = max;
        setValueToInitial();
    }

    public AtomicVariable(int initialValue, int min, int max, int accelerationFactor) {
        this(initialValue, min, max);
        this.accelerationFactor = accelerationFactor;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getAccelerationFactor() {
        return accelerationFactor;
    }

    @Override
    public void setValueToInitial() {
        setValue(initialValue);
    }

    @Override
    public void setValueToRandom(RandomGenerator randomGenerator) {
        int range = max - min + 1;
        int randomValue = randomGenerator.nextInt(range);
        setValue(min + randomValue);
    }

    public void setValue(int value) {
        if (value > max) {
            value = max;
        }

        if (value < min) {
            value = min;
        }

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtomicVariable that = (AtomicVariable) o;

        if (min != that.min) return false;
        if (max != that.max) return false;
        if (accelerationFactor != that.accelerationFactor) return false;
        if (initialValue != that.initialValue) return false;
        return value == that.value;

    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + max;
        result = 31 * result + accelerationFactor;
        result = 31 * result + initialValue;
        result = 31 * result + value;
        return result;
    }
}

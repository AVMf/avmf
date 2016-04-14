package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public abstract class AtomicVariable extends Variable {

    public static final int PRECISION_DEFAULT = 0;
    public static final int STEP_DEFAULT = 1;
    public static final int ACCELERATION_FACTOR_DEFAULT = 2;

    protected int min = Integer.MIN_VALUE;
    protected int max = Integer.MAX_VALUE;
    protected int precision = PRECISION_DEFAULT;
    protected int stepSize = STEP_DEFAULT;
    protected int accelerationFactor = ACCELERATION_FACTOR_DEFAULT;

    protected int initialValue;
    protected int value;

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

    public AtomicVariable(int initialValue, int min, int max, int precision) {
        this(initialValue, min, max);
        this.precision = precision;
    }

    public AtomicVariable(int initialValue, int min, int max, int stepSize, int accelerationFactor) {
        this(initialValue, min, max);
        this.stepSize = stepSize;
        this.accelerationFactor = accelerationFactor;
    }

    public AtomicVariable(int initialValue, int min, int max, int precision, int stepSize, int accelerationFactor) {
        this(initialValue, min, max, precision);
        this.stepSize = stepSize;
        this.accelerationFactor = accelerationFactor;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getPrecision() {
        return precision;
    }

    public int getStepSize() {
        return stepSize;
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
    public <T extends Throwable> void accept(VariableTypeVisitor<T> vtv) throws T {
        vtv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtomicVariable that = (AtomicVariable) o;

        if (min != that.min) return false;
        if (max != that.max) return false;
        if (precision != that.precision) return false;
        if (stepSize != that.stepSize) return false;
        if (accelerationFactor != that.accelerationFactor) return false;
        if (initialValue != that.initialValue) return false;
        return value == that.value;

    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + max;
        result = 31 * result + precision;
        result = 31 * result + stepSize;
        result = 31 * result + accelerationFactor;
        result = 31 * result + initialValue;
        result = 31 * result + value;
        return result;
    }
}

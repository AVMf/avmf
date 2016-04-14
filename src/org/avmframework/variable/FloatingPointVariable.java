package org.avmframework.variable;

public class FloatingPointVariable extends AtomicVariable {

    protected int precision;

    protected FloatingPointVariable() {
    }

    public FloatingPointVariable(double initialValue, int precision) {
        super(doubleToInt(initialValue, precision));
        this.precision = precision;
    }

    public FloatingPointVariable(double initialValue, int precision, double min, double max) {
        super(doubleToInt(initialValue, precision), doubleToInt(min, precision), doubleToInt(max, precision));
        this.precision = precision;
    }

    public FloatingPointVariable(double initialValue, int precision, double min, double max, int accelerationBase) {
        super(doubleToInt(initialValue, precision), doubleToInt(min, precision), doubleToInt(max, precision), accelerationBase);
        this.precision = precision;
    }

    public double getValueAsDouble() {
        return intToDouble(value, precision);
    }

    @Override
    public FloatingPointVariable deepCopy() {
        FloatingPointVariable copy = new FloatingPointVariable();
        copy.initialValue = initialValue;
        copy.min = min;
        copy.max = max;
        copy.accelerationFactor = accelerationFactor;
        copy.value = value;
        copy.precision = precision;
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatingPointVariable)) return false;
        if (!super.equals(o)) return false;

        FloatingPointVariable that = (FloatingPointVariable) o;

        return precision == that.precision;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + precision;
        return result;
    }

    @Override
    public String toString() {
        return "" + getValueAsDouble();
    }

    protected static int doubleToInt(double value, int precision) {
        return (int) Math.round(value * Math.pow(10, precision));
    }

    protected static double intToDouble(int value, int precision) {
        return ((double) value) * Math.pow(10, -precision);
    }
}
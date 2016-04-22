package org.avmframework.variable;

public class FloatingPointVariable extends AtomicVariable {

    protected int precision;

    public FloatingPointVariable(double initialValue, int precision) {
        super(doubleToInt(initialValue, precision));
        this.precision = precision;
    }

    public FloatingPointVariable(double initialValue, int precision, double min, double max) {
        super(doubleToInt(initialValue, precision), doubleToInt(min, precision), doubleToInt(max, precision));
        this.precision = precision;
    }

    public FloatingPointVariable(double initialValue, int precision, double min, double max, int accelerationFactor) {
        super(doubleToInt(initialValue, precision), doubleToInt(min, precision),
                doubleToInt(max, precision), accelerationFactor);
        this.precision = precision;
    }

    public double getValueAsDouble() {
        return intToDouble(value, precision);
    }

    @Override
    public FloatingPointVariable deepCopy() {
        FloatingPointVariable copy = new FloatingPointVariable(initialValue, precision, min, max, accelerationFactor);
        copy.value = value;
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
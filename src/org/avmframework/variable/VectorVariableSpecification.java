package org.avmframework.variable;

/**
 * Created by phil on 04/04/2016.
 */
public class VectorVariableSpecification {

    public static final int INITIAL_LENGTH_DEFAULT = 0;
    public static final int MIN_LENGTH_DEFAULT = 0;
    public static final int MAX_LENGTH_DEFAULT = 20;

    protected int initialLength = INITIAL_LENGTH_DEFAULT;
    protected int minLength = MIN_LENGTH_DEFAULT;
    protected int maxLength = MAX_LENGTH_DEFAULT;

    public VectorVariableSpecification() {
    }

    public VectorVariableSpecification(int initialLength) {
        this.initialLength = initialLength;
    }

    public VectorVariableSpecification(int initialLength, int minLength, int maxLength) {
        this.initialLength = initialLength;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
}

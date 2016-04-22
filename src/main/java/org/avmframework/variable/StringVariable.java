package org.avmframework.variable;

public class StringVariable extends VectorVariable {

    protected int minLength, maxLength;
    protected int charMin, charMax;

    protected String initialValue;

    // what about character defaults
    public StringVariable(String initialValue, int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    // TODO method to set from string

}

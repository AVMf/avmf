package org.avmframework.variable;

public class StringVariable extends VectorVariable {

    public static final int MIN_LENGTH_DEFAULT = 0;
    public static final int MAX_LENGTH_DEFAULT = 0;

    protected int minLength = MIN_LENGTH_DEFAULT;
    protected int maxLength = MAX_LENGTH_DEFAULT;

    protected char charMin = CharacterVariable.MIN_DEFAULT;
    protected char charMax = CharacterVariable.MAX_DEFAULT;

    protected String initialValue;

    public StringVariable(String initialValue, int maxLength) {
        this.maxLength = maxLength;
    }

    public StringVariable(String initialValue, int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public StringVariable(String initialValue, int minLength, int maxLength, char charMin, char charMax) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.charMin = charMin;
        this.charMax = charMax;
    }

    // TODO method to set from string
    // TODO how does this work in general, checking max and min limits -- use long ints? what about overflow
    // then we're away?
}

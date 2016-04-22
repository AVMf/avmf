package org.avmframework.variable;

public class CharacterVariable extends AtomicVariable {

    // The default is the printable ASCII range
    public static final char MIN_DEFAULT = 32;
    public static final char MAX_DEFAULT = 126;

    protected CharacterVariable() {
    }

    public CharacterVariable(char initialValue) {
        super(initialValue, MIN_DEFAULT, MAX_DEFAULT);
    }

    public CharacterVariable(char initialValue, char min, char max) {
        super(initialValue, min, max);
    }

    public CharacterVariable(char initialValue, char min, char max, int accelerationFactor) {
        super(initialValue, min, max, accelerationFactor);
    }

    public char getValueAsChar() {
        return (char) value;
    }

    @Override
    public Variable deepCopy() {
        CharacterVariable copy = new CharacterVariable((char) initialValue, (char) min, (char) max, accelerationFactor);
        copy.value = value;
        return copy;
    }

    @Override
    public String toString() {
        return "" + getValueAsChar();
    }

}


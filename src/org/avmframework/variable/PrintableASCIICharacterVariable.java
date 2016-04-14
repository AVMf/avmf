package org.avmframework.variable;

public class PrintableASCIICharacterVariable extends AtomicVariable {
    public static final int FIRST_CHARACTER = 32, LAST_CHARACTER = 126;

    protected PrintableASCIICharacterVariable() {
    }

    public PrintableASCIICharacterVariable(String initialValue) {
        super(stringCharacterToInt(initialValue), FIRST_CHARACTER, LAST_CHARACTER);
    }

    public PrintableASCIICharacterVariable(String initialValue, int accelerationFactor) {
        super(stringCharacterToInt(initialValue), FIRST_CHARACTER, LAST_CHARACTER, accelerationFactor);
    }

    public String getValueAsString() {
        return intToStringCharacter(value);
    }

    @Override
    public Variable deepCopy() {
        PrintableASCIICharacterVariable copy = new PrintableASCIICharacterVariable();
        copy.initialValue = initialValue;
        copy.min = min;
        copy.max = max;
        copy.accelerationFactor = accelerationFactor;
        copy.value = value;
        return copy;
    }

    @Override
    public String toString() {
        return getValueAsString();
    }

    protected static int stringCharacterToInt(String value) {
        if (value.equals("")) {
            return FIRST_CHARACTER;
        }
        return (int) value.charAt(0);
    }

    protected static String intToStringCharacter(int value) {
        return Character.toString((char) value);
    }
}


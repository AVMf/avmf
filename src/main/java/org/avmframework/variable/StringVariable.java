package org.avmframework.variable;

public class StringVariable extends VectorVariable {

    protected int minLength, maxLength;
    protected char charInitialValue, charMin, charMax;

    protected String initialValue;

    public StringVariable(String initialValue, int minLength, int maxLength,
                          char charInitialValue, char charMin, char charMax) {
        this.initialValue = initialValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.charInitialValue = charInitialValue;
        this.charMin = charMin;
        this.charMax = charMax;
    }

    @Override
    public CharacterVariable getVariable(int index) {
        return (CharacterVariable) variables.get(index);
    }

    @Override
    public void setValueToInitial() {
        variables.clear();
        size = initialValue.length();
        for (int i=0; i < maxLength; i++) {
            char charValue = i < size ? initialValue.charAt(i) : charInitialValue;
            CharacterVariable charVar = new CharacterVariable(charValue, charMin, charMax);
            variables.add(charVar);
        }

    }

    public String getValueAsString() {
        String str = "";
        for (int i=0; i < size; i++) {
            str += getVariable(i).getValueAsChar();
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StringVariable that = (StringVariable) o;

        if (minLength != that.minLength) return false;
        if (maxLength != that.maxLength) return false;
        if (charInitialValue != that.charInitialValue) return false;
        if (charMin != that.charMin) return false;
        if (charMax != that.charMax) return false;
        return initialValue.equals(that.initialValue);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + minLength;
        result = 31 * result + maxLength;
        result = 31 * result + (int) charInitialValue;
        result = 31 * result + (int) charMin;
        result = 31 * result + (int) charMax;
        result = 31 * result + initialValue.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\"" + getValueAsString() + "\"";
    }
}

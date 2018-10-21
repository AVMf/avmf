package org.avmframework.variable;

import org.apache.commons.math3.random.RandomGenerator;

public class StringVariable extends VectorVariable {

  protected int maxSize;
  protected char charInitialValue, charMin, charMax;

  protected String initialValue;

  public static StringVariable createPrintableASCIICharacterVariable(
      String initialValue, int maxSize, char charInitialValue) {
    return new StringVariable(
        initialValue,
        maxSize,
        charInitialValue,
        CharacterVariable.MIN_PRINTABLE_ASCII,
        CharacterVariable.MAX_PRINTABLE_ASCII);
  }

  public StringVariable(
      String initialValue, int maxSize, char charInitialValue, char charMin, char charMax) {
    this.initialValue = initialValue;
    this.maxSize = maxSize;
    this.charInitialValue = charInitialValue;
    this.charMin = charMin;
    this.charMax = charMax;

    if (charMin > charMax) {
      throw new MinGreaterThanMaxException(charMin, charMax);
    }

    setValueToInitial();
  }

  @Override
  public CharacterVariable getVariable(int index) {
    return (CharacterVariable) variables.get(index);
  }

  @Override
  public void setValueToInitial() {
    variables.clear();
    size = initialValue.length();
    for (int i = 0; i < maxSize; i++) {
      char charValue = i < size ? initialValue.charAt(i) : charInitialValue;
      CharacterVariable charVar = new CharacterVariable(charValue, charMin, charMax);
      charVar.setValueToInitial();
      variables.add(charVar);
    }
  }

  @Override
  public void setValueToRandom(RandomGenerator rg) {
    size = rg.nextInt(maxSize);
    super.setValueToRandom(rg);
  }

  @Override
  public StringVariable deepCopy() {
    StringVariable copy =
        new StringVariable(initialValue, maxSize, charInitialValue, charMin, charMax);
    copy.size = size;
    deepCopyVariables(copy);
    return copy;
  }

  public String asString() {
    String str = "";
    for (int i = 0; i < size; i++) {
      str += getVariable(i).asChar();
    }
    return str;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StringVariable that = (StringVariable) o;

    if (maxSize != that.maxSize) return false;
    if (charInitialValue != that.charInitialValue) return false;
    if (charMin != that.charMin) return false;
    if (charMax != that.charMax) return false;

    return initialValue.equals(that.initialValue);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + maxSize;
    result = 31 * result + (int) charInitialValue;
    result = 31 * result + (int) charMin;
    result = 31 * result + (int) charMax;
    result = 31 * result + initialValue.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "\"" + asString() + "\"";
  }
}

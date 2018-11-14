package org.avmframework.variable;

public class CharacterVariable extends AtomicVariable {

  public static final char MIN_PRINTABLE_ASCII = 32;
  public static final char MAX_PRINTABLE_ASCII = 126;

  public static CharacterVariable createPrintableAsciiCharacterVariable(char initialValue) {
    return new CharacterVariable(initialValue, MIN_PRINTABLE_ASCII, MAX_PRINTABLE_ASCII);
  }

  public CharacterVariable(char initialValue, char min, char max) {
    super(initialValue, min, max);
    if (min > max) {
      throw new MinGreaterThanMaxException(min, max);
    }
    setValueToInitial();
  }

  public char asChar() {
    return (char) value;
  }

  @Override
  public Variable deepCopy() {
    CharacterVariable copy = new CharacterVariable((char) initialValue, (char) min, (char) max);
    copy.value = value;
    return copy;
  }

  @Override
  public String toString() {
    return "" + asChar();
  }
}

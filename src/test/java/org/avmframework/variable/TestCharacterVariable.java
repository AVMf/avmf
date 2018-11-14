package org.avmframework.variable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestCharacterVariable {

  @Test
  public void testAsChar() {
    CharacterVariable charVar = CharacterVariable.createPrintableAsciiCharacterVariable('a');
    assertEquals(97, charVar.getValue());
    assertEquals('a', charVar.asChar());
  }
}

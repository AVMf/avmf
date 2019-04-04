package org.avmframework.variable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStringVariable {

  StringVariable strVar =
      new StringVariable(
          "test",
          5,
          ' ',
          CharacterVariable.MIN_PRINTABLE_ASCII,
          CharacterVariable.MAX_PRINTABLE_ASCII);

  @Test
  public void testInitialValue() {
    assertEquals(4, strVar.size());
    assertEquals("test", strVar.asString());
  }

  @Test
  public void testIncreaseSize() {
    strVar.increaseSize();
    assertEquals(5, strVar.size());
    assertEquals("test ", strVar.asString());
  }

  @Test
  public void testDecreaseSize() {
    strVar.decreaseSize();
    assertEquals(3, strVar.size());
    assertEquals("tes", strVar.asString());
  }

  @Test
  public void testAsString() {
    assertEquals("test", strVar.asString());
  }
}

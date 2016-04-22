package org.avmframework.variable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStringVariable {

    StringVariable strVar = new StringVariable("test", 0, 5, ' ',
            CharacterVariable.MIN_PRINTABLE_ASCII, CharacterVariable.MAX_PRINTABLE_ASCII);

    @Test
    public void testInitialValue() {
        strVar.setValueToInitial();
        assertEquals(4, strVar.size());
        assertEquals("test", strVar.getValueAsString());
    }

    @Test
    public void testIncreaseSize() {
        strVar.setValueToInitial();
        strVar.increaseSize();
        assertEquals(5, strVar.size());
        assertEquals("test ", strVar.getValueAsString());
    }

    @Test
    public void testDecreaseSize() {
        strVar.setValueToInitial();
        strVar.decreaseSize();
        assertEquals(3, strVar.size());
        assertEquals("tes", strVar.getValueAsString());
    }
}

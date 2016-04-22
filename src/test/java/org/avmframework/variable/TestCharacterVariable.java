package org.avmframework.variable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCharacterVariable {

    @Test
    public void testAsChar() {
        CharacterVariable charVar = CharacterVariable.createPrintableASCIICharacterVariable('a');
        assertEquals(97, charVar.getValue());
        assertEquals('a', charVar.asChar());
    }
}

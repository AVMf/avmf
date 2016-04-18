package org.avmframework.util;

import org.junit.Test;

import static org.avmframework.util.IntegerFibonacciNumbers.fibonacci;
import static org.avmframework.util.IntegerFibonacciNumbers.lengthOfIntegerFibonacciSequence;
import static org.junit.Assert.assertEquals;

public class TestIntegerFibonacciNumbers {

    @Test
    public void testSequence() {
        for (int i=2; i < lengthOfIntegerFibonacciSequence(); i++) {
            assertEquals(fibonacci(i), fibonacci(i-1) + fibonacci(i-2));
        }
    }
}

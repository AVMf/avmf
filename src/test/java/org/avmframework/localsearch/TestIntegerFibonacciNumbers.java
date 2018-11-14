package org.avmframework.localsearch;

import static org.avmframework.localsearch.IntegerFibonacciNumbers.fibonacci;
import static org.avmframework.localsearch.IntegerFibonacciNumbers.maxPosition;
import static org.avmframework.localsearch.IntegerFibonacciNumbers.positionOfSmallestFibonacciNumberGreaterOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestIntegerFibonacciNumbers {

  @Test
  public void testSequence() {
    for (int i = 2; i < maxPosition(); i++) {
      assertEquals(fibonacci(i), fibonacci(i - 1) + fibonacci(i - 2));
    }
  }

  @Test
  public void testIndexOflowestFibonacciGreaterThan() {
    assertEquals(0, positionOfSmallestFibonacciNumberGreaterOrEqualTo(0));
    assertEquals(1, positionOfSmallestFibonacciNumberGreaterOrEqualTo(1));
    assertEquals(3, positionOfSmallestFibonacciNumberGreaterOrEqualTo(2));
    assertEquals(15, positionOfSmallestFibonacciNumberGreaterOrEqualTo(600));
  }

  // TODO: use proper JUnit exception policy
  @Test
  public void testException() {
    boolean failed = true;
    try {
      fibonacci(maxPosition());
    } catch (IntegerFibonacciNumberException exception) {
      failed = false;
    }
    assertFalse("Fibonacci number " + maxPosition() + " should not exist", failed);
  }
}

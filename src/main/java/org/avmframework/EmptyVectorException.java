package org.avmframework;

/**
 * An exception thrown when the AVM is given a vector with no variables in it.
 *
 * @author Phil McMinn
 */
public class EmptyVectorException extends RuntimeException {

  /** Constructor. */
  public EmptyVectorException() {
    super("The vector to be optimized does not contain any variables");
  }
}

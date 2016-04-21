package org.avmframework;

public class EmptyVectorException extends RuntimeException {

    public EmptyVectorException() {
        super("The vector to be optimized contains no variables");
    }
}

package org.avmframework.variable;

public interface VariableTypeVisitor<T extends Throwable> {

    void visit(AtomicVariable av) throws T;

    void visit(VectorVariable vv) throws T;

}

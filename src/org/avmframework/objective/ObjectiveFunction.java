package org.avmframework.objective;

import org.avmframework.Monitor;
import org.avmframework.TerminationException;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;

public abstract class ObjectiveFunction {

    protected Monitor monitor;
    protected TerminationPolicy terminationPolicy;

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public void setTerminationPolicy(TerminationPolicy terminationPolicy) {
        this.terminationPolicy = terminationPolicy;
    }

    public ObjectiveValue evaluate(Vector vector) throws TerminationException {
        ObjectiveValue objVal = computeObjectiveValue(vector);

        monitor.observe(vector, objVal);
        if (terminationPolicy.terminate(monitor)) {
            throw new TerminationException();
        }

        return objVal;
    }

    protected abstract ObjectiveValue computeObjectiveValue(Vector vector);
}

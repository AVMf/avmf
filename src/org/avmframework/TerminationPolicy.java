package org.avmframework;

public class TerminationPolicy {

    public static final int NO_MAX_EVALUATIONS = -1;

    protected boolean terminateOnOptimal;
    protected int maxEvaluations;

    public TerminationPolicy(boolean terminateOnOptimal, int maxEvaluations) {
        this.terminateOnOptimal = terminateOnOptimal;
        this.maxEvaluations = maxEvaluations;
    }

    public boolean terminate(Monitor monitor) {
        if (monitor != null) {
            if (terminateOnOptimal && monitor.getBestObjVal().isOptimal()) {
                return true;
            }

            if (maxEvaluations != NO_MAX_EVALUATIONS && monitor.getNumEvaluations() >= maxEvaluations) {
                return true;
            }
        }

        return false;
    }
}

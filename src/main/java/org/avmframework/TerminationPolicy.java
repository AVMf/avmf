package org.avmframework;

public class TerminationPolicy {

    public static final int NO_MAX_EVALUATIONS = -1;
    public static final int NO_MAX_RESTARTS = -1;

    protected boolean terminateOnOptimal;
    protected int maxEvaluations;
    protected int maxRestarts;

    public TerminationPolicy(boolean terminateOnOptimal, int maxEvaluations, int maxRestarts) {
        this.terminateOnOptimal = terminateOnOptimal;
        this.maxEvaluations = maxEvaluations;
        this.maxRestarts = maxRestarts;
    }

    public boolean checkTermination(Monitor monitor) {
        if (monitor != null) {
            if (terminateOnOptimal && monitor.getBestObjVal().isOptimal()) {
                return true;
            }

            if (maxEvaluations != NO_MAX_EVALUATIONS && monitor.getNumEvaluations() >= maxEvaluations) {
                return true;
            }

            if (maxRestarts != NO_MAX_RESTARTS && monitor.getNumRestarts() >= maxRestarts) {
                return true;
            }
        }
        return false;
    }

    public static TerminationPolicy maxEvaluations(int maxEvaluations) {
        return new TerminationPolicy(true, maxEvaluations, NO_MAX_RESTARTS);
    }

    public static TerminationPolicy maxRestarts(int maxRestarts) {
        return new TerminationPolicy(true, NO_MAX_EVALUATIONS, maxRestarts);
    }
}

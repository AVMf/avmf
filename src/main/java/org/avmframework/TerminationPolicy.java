package org.avmframework;

public class TerminationPolicy {

    public static final int NO_LIMIT = -1;

    protected boolean terminateOnOptimal;
    protected int maxEvaluations;
    protected int maxRestarts;
    protected long runningTime;

    public TerminationPolicy(boolean terminateOnOptimal, int maxEvaluations, int maxRestarts, long runningTime) {
        this.terminateOnOptimal = terminateOnOptimal;
        this.maxEvaluations = maxEvaluations;
        this.maxRestarts = maxRestarts;
        this.runningTime = runningTime;
    }

    public boolean checkFoundOptimal(Monitor monitor) {
        return terminateOnOptimal && monitor.getBestObjVal() != null && monitor.getBestObjVal().isOptimal();
    }

    public boolean checkExhaustedEvaluations(Monitor monitor) {
        return maxEvaluations != NO_LIMIT && monitor.getNumEvaluations() >= maxEvaluations;
    }

    public boolean checkExhaustedRestarts(Monitor monitor) {
        return maxRestarts != NO_LIMIT && monitor.getNumRestarts() >= maxRestarts;
    }

    public boolean checkExhaustedTime(Monitor monitor) {
        return runningTime != NO_LIMIT && System.currentTimeMillis() - monitor.startTime > runningTime;
    }

    public static TerminationPolicy maxEvaluations(int maxEvaluations) {
        return new TerminationPolicy(true, maxEvaluations, NO_LIMIT, NO_LIMIT);
    }

    public static TerminationPolicy maxRestarts(int maxRestarts) {
        return new TerminationPolicy(true, NO_LIMIT, maxRestarts, NO_LIMIT);
    }

    public static TerminationPolicy runningTime(long maxTime) {
        return new TerminationPolicy(true, NO_LIMIT, NO_LIMIT, maxTime);
    }
}

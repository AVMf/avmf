package org.avmframework;

public class TerminationPolicy {

  public static final int NO_LIMIT = -1;

  protected boolean terminateOnOptimal;
  protected int maxEvaluations;
  protected int maxRestarts;
  protected long runningTime;

  public static TerminationPolicy createMaxEvaluationsTerminationPolicy(int maxEvaluations) {
    return new TerminationPolicy(true, maxEvaluations, NO_LIMIT, NO_LIMIT);
  }

  public static TerminationPolicy createMaxRestartsTerminationPolicy(int maxRestarts) {
    return new TerminationPolicy(true, NO_LIMIT, maxRestarts, NO_LIMIT);
  }

  public static TerminationPolicy createRunningTimeTerminationPolicy(long maxTime) {
    return new TerminationPolicy(true, NO_LIMIT, NO_LIMIT, maxTime);
  }

  public TerminationPolicy(
      boolean terminateOnOptimal, int maxEvaluations, int maxRestarts, long runningTime) {
    this.terminateOnOptimal = terminateOnOptimal;
    this.maxEvaluations = maxEvaluations;
    this.maxRestarts = maxRestarts;
    this.runningTime = runningTime;
  }

  public void checkFoundOptimal(Monitor monitor) throws TerminationException {
    if (terminateOnOptimal
        && monitor.getBestObjVal() != null
        && monitor.getBestObjVal().isOptimal()) {
      throw new TerminationException();
    }
  }

  public void checkExhaustedEvaluations(Monitor monitor) throws TerminationException {
    if (maxEvaluations != NO_LIMIT && monitor.getNumEvaluations() >= maxEvaluations) {
      throw new TerminationException();
    }
  }

  public void checkExhaustedRestarts(Monitor monitor) throws TerminationException {
    if (maxRestarts != NO_LIMIT && monitor.getNumRestarts() >= maxRestarts) {
      throw new TerminationException();
    }
  }

  public void checkExhaustedTime(Monitor monitor) throws TerminationException {
    if (runningTime != NO_LIMIT && System.currentTimeMillis() - monitor.startTime > runningTime) {
      throw new TerminationException();
    }
  }
}

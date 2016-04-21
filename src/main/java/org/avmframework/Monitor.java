package org.avmframework;

import org.avmframework.objective.ObjectiveValue;

public class Monitor {

    protected TerminationPolicy tp;
    protected ObjectiveValue bestObjVal;
    protected Vector bestVector;

    protected int numEvaluations, numUniqueEvaluations, numRestarts;
    protected long startTime, endTime;

    public Monitor(TerminationPolicy tp) {
        this.tp = tp;
        bestObjVal = null;
        bestVector = null;
        numEvaluations = 0;
        numUniqueEvaluations = 0;
        numRestarts = 0;
        startTime = System.currentTimeMillis();
    }

    public ObjectiveValue getBestObjVal() {
        return bestObjVal;
    }

    public Vector getBestVector() {
        return bestVector;
    }

    public int getNumEvaluations() {
        return numEvaluations;
    }

    public int getNumUniqueEvaluations() {
        return numUniqueEvaluations;
    }

    public int getNumRestarts() {
        return numRestarts;
    }

    public long getRunningTime() {
        return endTime - startTime;
    }

    public void observeVector() throws TerminationException {
        tp.checkExhaustedEvaluations(this);
        tp.checkExhaustedTime(this);
        numEvaluations ++;
    }

    public void observePreviouslyUnseenVector(Vector vector, ObjectiveValue objVal) throws TerminationException {
        if (bestObjVal == null || objVal.betterThan(bestObjVal)) {
            bestObjVal = objVal;
            bestVector = vector.deepCopy();
        }
        numUniqueEvaluations ++;

        tp.checkFoundOptimal(this);
    }

    public void observeRestart() throws TerminationException {
        tp.checkExhaustedRestarts(this);
        tp.checkExhaustedTime(this);
        numRestarts ++;
    }

    public void observeTermination() {
        endTime = System.currentTimeMillis();
    }
}

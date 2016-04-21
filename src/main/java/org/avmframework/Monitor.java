package org.avmframework;

import org.avmframework.objective.ObjectiveValue;

public class Monitor {

    protected TerminationPolicy tp;
    protected ObjectiveValue bestObjVal;
    protected Vector bestVector;

    protected int numEvaluations, numUniqueEvaluations, numRestarts;
    protected long startTime, endTime;
    protected int numVariablesSearched, numVectorCycles;

    public Monitor(TerminationPolicy tp) {
        this.tp = tp;
        bestObjVal = null;
        bestVector = null;
        numEvaluations = 0;
        numUniqueEvaluations = 0;
        numRestarts = 0;
        startTime = System.currentTimeMillis();
        int numVariablesSearched = 0;
        int numVectorCycles = 0;
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

    public int getNumVariablesSearched() {
        return numVariablesSearched;
    }

    public int getNumVectorCycles() {
        return numVectorCycles;
    }

    public void observeVariable() {
        numVariablesSearched ++;
    }

    public void observeVectorCycle() {
        numVectorCycles ++;
    }

    public void observeVector() throws TerminationException {
        if (tp.checkExhaustedEvaluations(this) || tp.checkExhaustedTime(this)) {
            throw new TerminationException();
        }
        numEvaluations ++;
    }

    public void observePreviouslyUnseenVector(Vector vector, ObjectiveValue objVal) throws TerminationException {
        if (bestObjVal == null || objVal.betterThan(bestObjVal)) {
            bestObjVal = objVal;
            bestVector = vector.deepCopy();
        }
        numUniqueEvaluations ++;

        if (tp.checkFoundOptimal(this)) {
            throw new TerminationException();
        }
    }

    public void observeRestart() throws TerminationException {
        if (tp.checkExhaustedRestarts(this) || tp.checkExhaustedTime(this)) {
            throw new TerminationException();
        }
        numRestarts ++;
    }

    public void observeTermination() {
        endTime = System.currentTimeMillis();
    }
}

package org.avmframework;

import org.avmframework.objective.ObjectiveValue;

public class Monitor {

    protected TerminationPolicy tp;
    protected ObjectiveValue bestObjVal;
    protected Vector bestVector;
    protected int numEvaluations;
    protected int numRestarts;

    public Monitor(TerminationPolicy tp) {
        this.tp = tp;
        bestObjVal = null;
        bestVector = null;
        numEvaluations = 0;
        numRestarts = 0;
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

    public int getNumRestarts() {
        return numRestarts;
    }

    public void observeVector(Vector vector, ObjectiveValue objVal) throws TerminationException {
        if (bestObjVal == null || objVal.betterThan(bestObjVal)) {
            bestObjVal = objVal;
            bestVector = vector.deepCopy();
        }
        numEvaluations ++;
        if (tp.checkTermination(this)) {
            throw new TerminationException();
        }
    }

    public void observeRestart() throws TerminationException {
        numRestarts ++;
        if (tp.checkTermination(this)) {
            numRestarts --;
            throw new TerminationException();
        }
    }
}

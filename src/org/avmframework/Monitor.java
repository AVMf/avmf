package org.avmframework;

import org.avmframework.objective.ObjectiveValue;

public class Monitor {

    protected ObjectiveValue bestObjVal;
    protected Vector bestVector;
    protected int numEvaluations;

    public Monitor() {
        bestObjVal = null;
        bestVector = null;
        numEvaluations = 0;
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

    public void observe(Vector vector, ObjectiveValue objVal) {
        if (bestObjVal == null || objVal.betterThan(bestObjVal)) {
            bestObjVal = objVal;
            // TODO: clone the vector
        }
        numEvaluations ++;
    }
}

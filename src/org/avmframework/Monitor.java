package org.avmframework;

import org.avmframework.objective.ObjectiveValue;

import static org.avmframework.variable.VariableUtils.intVectorAsString;

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
            bestVector = vector.deepCopy();

            System.out.println("Monitor: new best" + intVectorAsString(bestVector));

        }
        numEvaluations ++;
    }
}

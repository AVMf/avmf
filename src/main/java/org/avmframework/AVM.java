package org.avmframework;

import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.Variable;
import org.avmframework.variable.VectorVariable;

public class AVM {

    protected LocalSearch localSearch;
    protected TerminationPolicy tp;
    protected Initializer initializer, restarter;

    protected Monitor monitor;
    protected ObjectiveFunction objFun;
    protected Vector vector;

    public AVM(LocalSearch localSearch, TerminationPolicy tp, Initializer initializer) {
        this(localSearch, tp, initializer, initializer);
    }

    public AVM(LocalSearch localSearch, TerminationPolicy tp, Initializer initializer, Initializer restarter) {
        this.localSearch = localSearch;
        this.tp = tp;
        this.initializer = initializer;
        this.restarter = restarter;
    }

    public Monitor search(Vector vector, ObjectiveFunction objFun) {
        // set up the monitor
        this.monitor = new Monitor(tp);

        // set up the objective function
        this.objFun = objFun;
        objFun.setMonitor(monitor);

        // initialize the vector
        this.vector = vector;
        initializer.initialize(vector);
        if (vector.size() == 0) {
            throw new EmptyVectorException();
        }

        try {
            // the loop terminates when a TerminationException is thrown
            while (true) {

                // search over the vector's variables
                alternatingVariableSearch(vector);

                // restart the search
                monitor.observeRestart();
                restarter.initialize(vector);
            }

        } catch (TerminationException e) {
            // the search has ended
            monitor.observeTermination();
        }

        return monitor;
    }

    protected void alternatingVariableSearch(VectorVariable vectorVar) throws TerminationException {
        ObjectiveValue lastImprovement = objFun.evaluate(vector);
        int nonImprovement = 0;

        while (nonImprovement < vectorVar.size()) {

            // alternate through the variables
            int variableIndex = 0;
            while (variableIndex < vectorVar.size() && nonImprovement < vectorVar.size()) {

                // perform a local search on this variable
                Variable var = vectorVar.getVariable(variableIndex);
                variableSearch(var);

                // check if the current objective value has improved on the last
                ObjectiveValue current = objFun.evaluate(vector);
                if (current.betterThan(lastImprovement)) {
                    lastImprovement = current;
                    nonImprovement = 0;
                } else {
                    nonImprovement++;
                }

                variableIndex++;
            }
        }
    }

    protected void variableSearch(Variable var) throws TerminationException {
        if (var instanceof AtomicVariable) {
            atomicVariableSearch((AtomicVariable) var);
        } else if (var instanceof VectorVariable) {
            vectorVariableSearch((VectorVariable) var);
        }
    }

    protected void atomicVariableSearch(AtomicVariable atomicVar) throws TerminationException {
        localSearch.search(atomicVar, vector, objFun);
    }

    protected void vectorVariableSearch(VectorVariable vectorVar) throws TerminationException {
        ObjectiveValue current = null, next = objFun.evaluate(vector);

        // try moves that increase the vector size
        changeVectorVariableSize(vectorVar, current, next, true);

        // try moves that decrease the vector size
        changeVectorVariableSize(vectorVar, current, next, false);

        // now for the alternating variable search...
        alternatingVariableSearch(vectorVar);
    }

    protected void changeVectorVariableSize(VectorVariable vectorVar,
                                            ObjectiveValue current, ObjectiveValue next,
                                            boolean increase) throws TerminationException {
        int currentSize, nextSize = vectorVar.size();

        // try moves that increase the vector size
        do {
            current = next;
            currentSize = nextSize;

            if (increase) {
                vectorVar.increaseSize();
            } else {
                vectorVar.decreaseSize();
            }

            next = objFun.evaluate(vector);
            nextSize = currentSize;
        } while (next.betterThan(current));

        // reverse the last move, if there was a change
        if (nextSize > currentSize) {
            if (increase) {
                vectorVar.decreaseSize();
            } else {
                vectorVar.increaseSize();
            }
        }
    }
}

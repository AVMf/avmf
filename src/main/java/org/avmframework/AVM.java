package org.avmframework;

import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.Variable;

public class AVM {

    protected LocalSearch localSearch;
    protected TerminationPolicy tp;
    protected Initializer initializer, restarter;

    protected ObjectiveFunction objFun;
    protected Monitor monitor;

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
        monitor = new Monitor(tp);

        // set up the objective function
        this.objFun = objFun;
        objFun.setMonitor(monitor);

        // initialize the vector
        initializer.initialize(vector);

        try {
            vectorSearch(vector);
        } catch (TerminationException e) {
            // the search has ended
            monitor.observeTermination();
        }

        return monitor;
    }

    protected void vectorSearch(Vector vector) throws TerminationException {
        // no variables to optimize, return
        if (vector.size() == 0) {
            return;
        }

        // the loop terminates when a TerminationException is thrown
        while (true) {
            // set up last improvement info
            ObjectiveValue lastImprovement = objFun.evaluate(vector);
            int nonImprovement = 0;

            while (nonImprovement < vector.size()) {

                // alternate through the variables
                int variableIndex = 0;
                while (variableIndex < vector.size() && nonImprovement < vector.size()) {

                    // perform a local search on this variable
                    Variable var = vector.getVariable(variableIndex);
                    variableSearch(var, vector);

                    // check if the current objective value has improved on last
                    ObjectiveValue current = objFun.evaluate(vector);
                    if (current.betterThan(lastImprovement)) {
                        lastImprovement = current;
                        nonImprovement = 0;
                    } else {
                        nonImprovement ++;
                    }

                    variableIndex ++;
                }
            }

            // restart the search
            monitor.observeRestart();
            restarter.initialize(vector);
        }
    }


    protected void variableSearch(Variable var, Vector vector) throws TerminationException {
        if (var instanceof AtomicVariable) {
            atomicVariableSearch((AtomicVariable) var, vector);
        } // else
        // TODO: vector variables
    }

    protected void atomicVariableSearch(AtomicVariable av, Vector vector) throws TerminationException {
        localSearch.search(av, vector, objFun);
    }
}

package org.avmframework;

import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.Variable;

public class AVM {

    protected LocalSearch localSearch;
    protected TerminationPolicy tp;
    protected Initializer initializer, restarter;

    protected Vector vector;
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
        // set up the vector
        this.vector = vector;
        if (vector.size() == 0) {
            // the vector contains no variables to optimize
            throw new EmptyVectorException();
        }

        // set up the objective function
        this.objFun = objFun;
        monitor = new Monitor(tp);
        objFun.setMonitor(monitor);

        try {
            performSearch();
        } catch (TerminationException e) {
            // the search has ended
            monitor.observeTermination();
        }

        return monitor;
    }

    protected void performSearch() throws TerminationException {
        // initialize the vector
        initializer.initialize(vector);

        do {
            ObjectiveValue lastImprovement = objFun.evaluate(vector);
            int nonImprovement = 0;

            do {
                // log this vector cycle
                monitor.observeVectorCycle();

                // alternate through the variables
                int index = 0;
                while (index < vector.size() && nonImprovement < vector.size()) {
                    // log this variable search
                    monitor.observeVariable();

                    // perform a local search on this variable
                    Variable var = vector.getVariable(index);
                    localSearch(var);

                    // check if the current objective value has improved on last
                    ObjectiveValue current = objFun.evaluate(vector);
                    if (current.betterThan(lastImprovement)) {
                        lastImprovement = current;
                        nonImprovement = 0;
                    } else {
                        nonImprovement ++;
                    }

                    index ++;
                }
            } while (nonImprovement < vector.size());

            // restart the search
            monitor.observeRestart();
            restarter.initialize(vector);

        // the loop terminates when a TerminationException is thrown
        } while (true);
    }


    protected void localSearch(Variable var) throws TerminationException {
        if (var instanceof AtomicVariable) {
            atomicVariableSearch((AtomicVariable) var);
        } // else
        // TODO: vector variables
    }

    protected void atomicVariableSearch(AtomicVariable av) throws TerminationException {
        localSearch.search(av, vector, objFun);
    }
}

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
        }

        return monitor;
    }

    protected void performSearch() throws TerminationException {
        // initialize the vector
        initializer.initialize(vector);

        do {
            int nonImprovement = 0;
            do {
                ObjectiveValue original = objFun.evaluate(vector);

                // alternate through the variables
                for (Variable var : vector.getVariables()) {

                    // perform a local search on this variable
                    localSearch(var);

                    // check if the current objective value has improvement
                    ObjectiveValue current = objFun.evaluate(vector);
                    nonImprovement = current.betterThan(original) ? 0 : nonImprovement + 1;
                }
            } while (nonImprovement < vector.size());

            // restart the search
            monitor.observeRestart();
            restarter.initialize(vector);

            System.out.println(monitor.getNumEvaluations());

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

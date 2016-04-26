package org.avmframework;

import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.Variable;
import org.avmframework.variable.VectorVariable;

/**
 * The main class for instantiating and running an AVM search
 * @author Phil McMinn
 *
 */
public class AVM {

    protected LocalSearch localSearch;
    protected TerminationPolicy tp;
    protected Initializer initializer, restarter;

    protected Monitor monitor;
    protected ObjectiveFunction objFun;
    protected Vector vector;

    /**
     * Constructs an AVM instance.
     * @param localSearch A local search instance.
     * @param tp The termination policy to be used by the search.
     * @param initializer The initializer to be used to initialize variables at the start <i>and</i> to restart the search.
     */
    public AVM(LocalSearch localSearch, TerminationPolicy tp, Initializer initializer) {
        this(localSearch, tp, initializer, initializer);
    }

    /**
     * Constructs an AVM instance.
     * @param localSearch A local search instance.
     * @param tp The termination policy to be used by the search.
     * @param initializer The initializer to be used to initialize variables at the start of the search.
     * @param restarter  The initializer to be used to initialize variables when restarting the search.
     */
    public AVM(LocalSearch localSearch, TerminationPolicy tp, Initializer initializer, Initializer restarter) {
        this.localSearch = localSearch;
        this.tp = tp;
        this.initializer = initializer;
        this.restarter = restarter;
    }

    /**
     * Performs the AVM search.
     * @param vector The vector of variables to be optimized.
     * @param objFun The objective function to optimize the variables against.
     * @return A Monitor instance detailing the progression statistics of the completed search process.
     */
    public Monitor search(Vector vector, ObjectiveFunction objFun) {
        // set up the monitor
        this.monitor = new Monitor(tp);

        // set up the objective function
        this.objFun = objFun;
        objFun.setMonitor(monitor);

        // initialize the vector
        this.vector = vector;
        initializer.initialize(vector);

        // is there anything to optimize?
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

    protected void alternatingVariableSearch(AbstractVector abstractVector) throws TerminationException {
        ObjectiveValue lastImprovement = objFun.evaluate(vector);
        int nonImprovement = 0;

        while (nonImprovement < abstractVector.size()) {

            // alternate through the variables
            int variableIndex = 0;
            while (variableIndex < abstractVector.size() && nonImprovement < abstractVector.size()) {

                // perform a local search on this variable
                Variable var = abstractVector.getVariable(variableIndex);
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
        ObjectiveValue next = objFun.evaluate(vector);

        // try moves that increase the vector size
        progressivelyChangeVectorVariableSize(vectorVar, next, true);

        // try moves that decrease the vector size
        progressivelyChangeVectorVariableSize(vectorVar, next, false);

        // now for the alternating variable search...
        alternatingVariableSearch(vectorVar);
    }

    protected void progressivelyChangeVectorVariableSize(VectorVariable vectorVar,
                                                         ObjectiveValue next,
                                                         boolean increase) throws TerminationException {
        int currentSize, nextSize = vectorVar.size();
        ObjectiveValue current;

        // try moves that increase the vector size
        do {
            current = next;
            currentSize = nextSize;

            changeVectorVariableSize(vectorVar, increase);

            next = objFun.evaluate(vector);
            nextSize = currentSize;
        } while (next.betterThan(current));

        // reverse the last move, if there was a change
        if (nextSize > currentSize) {
            changeVectorVariableSize(vectorVar, !increase);
        }
    }

    private void changeVectorVariableSize(VectorVariable vectorVar, boolean increase) {
        if (increase) {
            vectorVar.increaseSize();
        } else {
            vectorVar.decreaseSize();
        }
    }
}

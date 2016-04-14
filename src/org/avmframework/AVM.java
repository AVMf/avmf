package org.avmframework;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.Variable;
import org.avmframework.variable.VariableTypeVisitor;
import org.avmframework.variable.VectorVariable;

public class AVM {

    protected LocalSearch localSearch;
    protected TerminationPolicy tp;
    protected Initializer initializer, restarter;

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

        Monitor monitor = new Monitor();

        objFun.setMonitor(monitor);
        objFun.setTerminationPolicy(tp);

        try {
            // initialize the vector
            initializer.initialize(vector);

            do {
                boolean improvement;
                do {
                    improvement = false;
                    ObjectiveValue original = objFun.evaluate(vector);

                    // alternate through the variables
                    for (Variable variable : vector.getVariables()) {
                        localSearch(variable, vector, objFun);

                        ObjectiveValue current = objFun.evaluate(vector);
                        if (current.betterThan(original)) {
                            improvement = true;
                        }
                    }
                } while (improvement);

                // restart the search
                restarter.initialize(vector);

            } while (true);

        } catch (TerminationException e) {
            // the search has ended
        }

        return monitor;
    }

    protected void localSearch(Variable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        var.accept(new VariableTypeVisitor<TerminationException>() {

            @Override
            public void visit(AtomicVariable av) throws TerminationException {
                localSearch.search(av, vector, objFun);
            }

            @Override
            public void visit(VectorVariable vv) throws TerminationException {
                localSearch.search(vv, vector, objFun);
            }
        });
    }
}

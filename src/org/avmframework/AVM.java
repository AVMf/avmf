package org.avmframework;

import com.sun.xml.internal.xsom.impl.Ref;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.Variable;

public class AVM {

    protected LocalSearch localSearch;
    protected TerminationPolicy tp;

    public AVM(LocalSearch localSearch, TerminationPolicy tp) {
        this.localSearch = localSearch;
        this.tp = tp;
    }

    public Monitor search(Vector vector, ObjectiveFunction objFun) {

        Monitor monitor = new Monitor();

        objFun.setMonitor(monitor);
        objFun.setTerminationPolicy(tp);

        try {
/*
        // initialize the vector

        do {

            boolean improvement = false;
            do {
                for (int i = 0; i < vector.size(); i++) {
*/
            Variable variable = vector.getVariable(0);


            localSearch.search(variable, vector, objFun);

            // if fitness improves
            // improvement = true;

/*
                }
            } while (improvement);

            // restart

        } while (true);
*/
        } catch (TerminationException e) {

        }

        return monitor;
    }
}

package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.*;

public class IteratedPatternSearch extends LocalSearch {

    public final static TiedDirectionPolicy DEFAULT_TIED_DIRECTION_POLICY = new UseBest();

    protected TiedDirectionPolicy tdp = DEFAULT_TIED_DIRECTION_POLICY;

    public IteratedPatternSearch() {
        this.tdp = DEFAULT_TIED_DIRECTION_POLICY;
    }

    public IteratedPatternSearch(TiedDirectionPolicy tdp) {
        this.tdp = tdp;
    }

    public void search(Variable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {

        PatternSearch ps = new PatternSearch(vector, objFun, tdp);

        if (var instanceof AtomicVariable) {
            searchAV((AtomicVariable) var, ps);
        } else {
            searchVV((VectorVariable) var, vector, objFun);
        }
    }

    protected void searchAV(AtomicVariable var, PatternSearch ps) throws TerminationException {

        while (true) {  // while improvement over last value, else terminate and move onto next
            ps.search(var);
        }
    }

    protected void searchVV(VectorVariable variable, Vector vector, ObjectiveFunction objFun) {

    }
}

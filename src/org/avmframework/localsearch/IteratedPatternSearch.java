package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.Vector;
import org.avmframework.localsearch.tiebreaking.TiedDirectionPolicy;
import org.avmframework.localsearch.tiebreaking.UseLeft;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.*;

public class IteratedPatternSearch extends LocalSearch {

    public final static TiedDirectionPolicy DEFAULT_TIED_DIRECTION_POLICY = new UseLeft();

    protected TiedDirectionPolicy tdp = DEFAULT_TIED_DIRECTION_POLICY;
    protected Vector vector;
    protected ObjectiveFunction objFun;

    public IteratedPatternSearch() {
        this.tdp = DEFAULT_TIED_DIRECTION_POLICY;
    }

    public IteratedPatternSearch(TiedDirectionPolicy tdp) {
        this.tdp = tdp;
    }

    public void search(Variable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {

        var.accept(new VariableTypeVisitor<TerminationException>() {

            @Override
            public void visit(AtomicVariable av) throws TerminationException {
                searchAV((AtomicVariable) var, vector, objFun);
            }

            @Override
            public void visit(VectorVariable vv) throws TerminationException {
                searchVV((VectorVariable) var, vector, objFun);
            }
        });
     }

    protected void searchAV(AtomicVariable var, Vector vector, ObjectiveFunction objFun) throws TerminationException {
        PatternSearch ps = new PatternSearch(vector, objFun, tdp);
        ObjectiveValue next = objFun.evaluate(vector), last = null;

        do {
            ps.search(var);
            last = next;
            next = objFun.evaluate(vector);
        } while (next.betterThan(last));
    }

    protected void searchVV(VectorVariable variable, Vector vector, ObjectiveFunction objFun) {

    }
}

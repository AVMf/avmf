package org.avmframework.examples.inputdatageneration;

import java.util.LinkedList;
import java.util.List;

public class ControlDependenceChain {

    protected List<Branch> branches = new LinkedList<>();

    public ControlDependenceChain(List<Branch> branches) {
        this.branches.addAll(branches);
    }

    public DivergencePoint getDivergencePoint(ExecutionTrace trace) {
        int traceIndex = 0;
        for (BranchExection node : trace.getBranchExecutions()) {
            int chainIndex = 0;
            for (Branch branch : branches) {
                if (node.getBranch().getID() == branch.getID()) {
                    if (node.getBranch().getOutcome() != branch.getOutcome()) {
                        return new DivergencePoint(traceIndex, chainIndex);
                    }
                }
                chainIndex ++;
            }
            traceIndex ++;
        }
        return null;
    }

    @Override
    public String toString() {
        return branches.toString();
    }
}

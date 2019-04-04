package org.avmframework.examples.inputdatageneration;

import org.avmframework.Vector;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;

public abstract class BranchTargetObjectiveFunction extends ObjectiveFunction {

  protected Branch target;
  protected ControlDependenceChain controlDependenceChain;
  protected ExecutionTrace trace;

  public BranchTargetObjectiveFunction(Branch target) {
    this.target = target;
    this.controlDependenceChain = getControlDependenceChainForTarget();
  }

  protected abstract ControlDependenceChain getControlDependenceChainForTarget();

  protected ObjectiveValue computeObjectiveValue(Vector vector) {
    trace = new ExecutionTrace();
    executeTestObject(vector);
    DivergencePoint divergencePoint = controlDependenceChain.getDivergencePoint(trace);

    int approachLevel = 0;
    double distance = 0;
    if (divergencePoint != null) {
      approachLevel = divergencePoint.chainIndex;
      distance = trace.getBranchExecution(divergencePoint.traceIndex).getDistanceToAlternative();
    }
    return new BranchTargetObjectiveValue(approachLevel, distance);
  }

  protected abstract void executeTestObject(Vector vector);
}

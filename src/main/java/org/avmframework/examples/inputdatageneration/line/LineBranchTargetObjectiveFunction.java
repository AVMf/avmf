package org.avmframework.examples.inputdatageneration.line;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.BranchTargetObjectiveFunction;
import org.avmframework.examples.inputdatageneration.ControlDependenceChain;
import org.avmframework.examples.inputdatageneration.ControlDependencies;
import org.avmframework.variable.FixedPointVariable;

public class LineBranchTargetObjectiveFunction extends BranchTargetObjectiveFunction {

  public LineBranchTargetObjectiveFunction(Branch target) {
    super(target);
  }

  @Override
  protected ControlDependenceChain getControlDependenceChainForTarget() {
    ControlDependencies controlDependencies = new ControlDependencies();

    controlDependencies.add(new Branch(1, true), null);
    controlDependencies.add(new Branch(1, false), null);

    controlDependencies.add(new Branch(2, true), new Branch(1, true));
    controlDependencies.add(new Branch(2, false), new Branch(1, true));

    controlDependencies.add(new Branch(3, true), new Branch(2, true));
    controlDependencies.add(new Branch(3, false), new Branch(2, true));

    controlDependencies.add(new Branch(4, true), new Branch(3, true));
    controlDependencies.add(new Branch(4, false), new Branch(3, true));

    controlDependencies.add(new Branch(5, true), new Branch(4, true));
    controlDependencies.add(new Branch(5, false), new Branch(4, true));

    controlDependencies.add(new Branch(6, true), new Branch(1, false));
    controlDependencies.add(new Branch(6, false), new Branch(1, false));

    controlDependencies.add(new Branch(7, true), new Branch(1, false));
    controlDependencies.add(new Branch(7, false), new Branch(1, false));

    return controlDependencies.getControlDependenceChain(target);
  }

  @Override
  protected void executeTestObject(Vector vector) {
    intersect(getLine(vector, 0), getLine(vector, 4));
  }

  protected Line getLine(Vector vector, int startIndex) {
    return new Line(
        getVariable(vector, startIndex + 0),
        getVariable(vector, startIndex + 1),
        getVariable(vector, startIndex + 2),
        getVariable(vector, startIndex + 3));
  }

  protected double getVariable(Vector vector, int index) {
    return ((FixedPointVariable) vector.getVariable(index)).asDouble();
  }

  protected boolean intersect(Line line1, Line line2) {
    double u1t = (line2.x2 - line2.x1) * (line1.y1 - line2.y1)
        - (line2.y2 - line2.y1) * (line1.x1 - line2.x1);
    double u2t = (line1.x2 - line1.x1) * (line1.y1 - line2.y1)
        - (line1.y2 - line1.y1) * (line1.x1 - line2.x1);
    double u2 = (line2.y2 - line2.y1) * (line1.x2 - line1.x1)
        - (line2.x2 - line2.x1) * (line1.y2 - line1.y1);

    if (trace.notEquals(1, u2, 0)) {
      double u1 = u1t / u2;
      double u02 = u2t / u2;

      if (trace.lessThanOrEquals(2, 0, u1)) {
        if (trace.lessThanOrEquals(3, u1, 1)) {
          if (trace.lessThanOrEquals(4, 0, u02)) {
            if (trace.lessThanOrEquals(5, u02, 1)) {
              return true;
            }
          }
        }
      }
      return false;

    } else {
      if (trace.equals(6, u1t, 0)) {
        return true;
      }

      if (trace.equals(7, u2t, 0)) {
        return true;
      }

      return false;
    }
  }
}

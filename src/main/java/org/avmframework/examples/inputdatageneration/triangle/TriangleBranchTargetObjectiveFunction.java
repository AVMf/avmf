package org.avmframework.examples.inputdatageneration.triangle;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.BranchTargetObjectiveFunction;
import org.avmframework.examples.inputdatageneration.ControlDependenceChain;
import org.avmframework.examples.inputdatageneration.ControlDependencies;
import org.avmframework.variable.IntegerVariable;

public class TriangleBranchTargetObjectiveFunction extends BranchTargetObjectiveFunction {

  public TriangleBranchTargetObjectiveFunction(Branch target) {
    super(target);
  }

  @Override
  protected ControlDependenceChain getControlDependenceChainForTarget() {
    ControlDependencies controlDependencies = new ControlDependencies();

    controlDependencies.add(new Branch(1, true), null);
    controlDependencies.add(new Branch(1, false), null);

    controlDependencies.add(new Branch(2, true), null);
    controlDependencies.add(new Branch(2, false), null);

    controlDependencies.add(new Branch(3, true), null);
    controlDependencies.add(new Branch(3, false), null);

    controlDependencies.add(new Branch(4, true), null);
    controlDependencies.add(new Branch(4, false), null);

    controlDependencies.add(new Branch(5, true), new Branch(4, false));
    controlDependencies.add(new Branch(5, false), new Branch(4, false));

    controlDependencies.add(new Branch(6, true), new Branch(5, true));
    controlDependencies.add(new Branch(6, false), new Branch(5, true));

    controlDependencies.add(new Branch(7, true), new Branch(5, false));
    controlDependencies.add(new Branch(7, false), new Branch(5, false));

    controlDependencies.add(new Branch(8, true), new Branch(7, false));
    controlDependencies.add(new Branch(8, false), new Branch(7, false));

    return controlDependencies.getControlDependenceChain(target);
  }

  @Override
  protected void executeTestObject(Vector vector) {
    classify(getVariable(vector, 0), getVariable(vector, 1), getVariable(vector, 2));
  }

  protected int getVariable(Vector vector, int index) {
    return ((IntegerVariable) vector.getVariable(index)).asInt();
  }

  private Triangle.TriangleType classify(int num1, int num2, int num3) {
    Triangle.TriangleType type;

    if (trace.greaterThan(1, num1, num2)) {
      int temp = num1;
      num1 = num2;
      num2 = temp;
    }

    if (trace.greaterThan(2, num1, num2)) {
      int temp = num1;
      num1 = num3;
      num3 = temp;
    }

    if (trace.greaterThan(3, num2, num3)) {
      int temp = num2;
      num2 = num3;
      num3 = temp;
    }

    if (trace.lessThanOrEquals(4, num1 + num2, num3)) {
      type = Triangle.TriangleType.NOT_A_TRIANGLE;
    } else {
      type = Triangle.TriangleType.SCALENE;
      if (trace.equals(5, num1, num2)) {
        if (trace.equals(6, num2, num3)) {
          type = Triangle.TriangleType.EQUILATERAL;
        }
      } else {
        if (trace.equals(7, num1, num2)) {
          type = Triangle.TriangleType.ISOSCELES;
        } else if (trace.equals(8, num2, num3)) {
          type = Triangle.TriangleType.ISOSCELES;
        }
      }
    }
    return type;
  }
}

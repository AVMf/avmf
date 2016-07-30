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

    private Triangle.TriangleType classify(int a, int b, int c) {
        Triangle.TriangleType type;

        if (trace.greaterThan(1, a, b)) {
            int t = a;
            a = b;
            b = t;
        }

        if (trace.greaterThan(2, a, c)) {
            int t = a;
            a = c;
            c = t;
        }

        if (trace.greaterThan(3, b, c)) {
            int t = b;
            b = c;
            c = t;
        }

        if (trace.lessThanOrEquals(4, a + b, c)) {
            type = Triangle.TriangleType.NOT_A_TRIANGLE;
        } else {
            type = Triangle.TriangleType.SCALENE;
            if (trace.equals(5, a, b)) {
                if (trace.equals(6, b, c)) {
                    type = Triangle.TriangleType.EQUILATERAL;
                }
            } else {
                if (trace.equals(7, a, b)) {
                    type = Triangle.TriangleType.ISOSCELES;
                } else if (trace.equals(8, b, c)) {
                    type = Triangle.TriangleType.ISOSCELES;
                }
            }
        }
        return type;
    }
}

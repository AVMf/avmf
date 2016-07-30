package org.avmframework.examples.inputdatageneration.line;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.*;
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

    protected boolean intersect(Line a, Line b) {
        double ua_t = (b.x2 - b.x1) * (a.y1 - b.y1) - (b.y2 - b.y1) * (a.x1 - b.x1);
        double ub_t = (a.x2 - a.x1) * (a.y1 - b.y1) - (a.y2 - a.y1) * (a.x1 - b.x1);
        double u_b = (b.y2 - b.y1) * (a.x2 - a.x1) - (b.x2 - b.x1) * (a.y2 - a.y1);

        if (trace.notEquals(1, u_b, 0)) {
            double ua = ua_t / u_b;
            double ub = ub_t / u_b;

            if (trace.lessThanOrEquals(2, 0, ua)) {
                if (trace.lessThanOrEquals(3, ua, 1)) {
                    if (trace.lessThanOrEquals(4, 0, ub)) {
                        if (trace.lessThanOrEquals(5, ub, 1)) {
                            return true;
                        }
                    }
                }
            }
            return false;

        } else {
            if (trace.equals(6, ua_t, 0)) {
                return true;
            }

            if (trace.equals(7, ub_t, 0)) {
                return true;
            }

            return false;
        }
    }
}

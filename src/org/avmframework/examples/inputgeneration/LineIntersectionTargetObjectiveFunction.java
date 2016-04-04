package org.avmframework.examples.inputgeneration;

import org.avmframework.objective.InputGenerationObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.Vector;
import static org.avmframework.variable.VariableUtils.doubleValue;

public class LineIntersectionTargetObjectiveFunction implements ObjectiveFunction {

    private int approachLevel;
    private double branchDistance;

    public InputGenerationObjectiveValue compute(Vector vector) {

        Line a = new Line(
                doubleValue(vector, 0),
                doubleValue(vector, 1),
                doubleValue(vector, 2),
                doubleValue(vector, 3));

        Line b = new Line(
                doubleValue(vector, 4),
                doubleValue(vector, 5),
                doubleValue(vector, 6),
                doubleValue(vector, 7));

        intersect(a, b);

        return new InputGenerationObjectiveValue(approachLevel, branchDistance);
    }

    private boolean intersect(Line a, Line b) {

        double ua_t = (b.x2 - b.x1) * (a.y1 - b.y1) - (b.y2 - b.y1) * (a.x1 - b.x1);
        double ub_t = (a.x2 - a.x1) * (a.y1 - b.y1) - (a.y2 - a.y1) * (a.x1 - b.x1);
        double u_b = (b.y2 - b.y1) * (a.x2 - a.x1) - (b.x2 - b.x1) * (a.y2 - a.y1);

        if (u_b != 0) {
            double ua = ua_t / u_b;
            double ub = ub_t / u_b;

            if (0 <= ua) {
                if (ua <= 1) {
                    if (0 <= ub) {
                        if (ub <= 1) {
                            /* TARGET EXECUTED */
                            approachLevel = 0;
                            branchDistance = 0;
                            return true;
                        } else {
                            approachLevel = 1;
                            branchDistance = 0;
                        }
                    } else {
                        approachLevel = 2;
                        branchDistance = 0;
                    }
                } else {
                    approachLevel = 3;
                    branchDistance = 0;
                }
            } else {
                approachLevel = 4;
                branchDistance = 0;
            }
            return false;

        } else {
            approachLevel = 5;
            branchDistance = 0;

            if (ua_t == 0) {
                return true;
            }

            if (ub_t == 0) {
                return true;
            }

            return false;
        }
    }
}

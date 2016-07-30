package org.avmframework.examples.inputdatageneration.calendar;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.BranchTargetObjectiveFunction;
import org.avmframework.examples.inputdatageneration.ControlDependenceChain;
import org.avmframework.examples.inputdatageneration.ControlDependencies;
import org.avmframework.variable.IntegerVariable;

import static org.avmframework.examples.inputdatageneration.calendar.Calendar.isLeapYear;
import static org.avmframework.examples.inputdatageneration.calendar.Calendar.monthDays;

public class CalenderBranchTargetObjectiveFunction extends BranchTargetObjectiveFunction {

    public CalenderBranchTargetObjectiveFunction(Branch target) {
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

        controlDependencies.add(new Branch(5, true), null);
        controlDependencies.add(new Branch(5, false), null);

        controlDependencies.add(new Branch(6, true), null);
        controlDependencies.add(new Branch(6, false), null);

        controlDependencies.add(new Branch(7, true), null);
        controlDependencies.add(new Branch(7, false), null);

        controlDependencies.add(new Branch(8, true), null);
        controlDependencies.add(new Branch(8, false), null);

        controlDependencies.add(new Branch(9, true), null);
        controlDependencies.add(new Branch(9, false), null);

        controlDependencies.add(new Branch(10, true), null);
        controlDependencies.add(new Branch(10, false), null);

        controlDependencies.add(new Branch(11, true), new Branch(10, true));
        controlDependencies.add(new Branch(11, false), new Branch(10, true));

        controlDependencies.add(new Branch(12, true), null);
        controlDependencies.add(new Branch(12, false), null);

        controlDependencies.add(new Branch(13, true), new Branch(12, true));
        controlDependencies.add(new Branch(13, false), new Branch(12, true));

        controlDependencies.add(new Branch(14, true), new Branch(13, true));
        controlDependencies.add(new Branch(14, false), new Branch(13, true));

        controlDependencies.add(new Branch(15, true), null);
        controlDependencies.add(new Branch(15, false), null);

        controlDependencies.add(new Branch(16, true), null);
        controlDependencies.add(new Branch(16, false), null);

        controlDependencies.add(new Branch(17, true), new Branch(16, true));
        controlDependencies.add(new Branch(17, false), new Branch(16, true));

        controlDependencies.add(new Branch(18, true), new Branch(16, false));
        controlDependencies.add(new Branch(18, false), new Branch(16, false));

        controlDependencies.add(new Branch(19, true), new Branch(18, true));
        controlDependencies.add(new Branch(19, false), new Branch(18, true));

        controlDependencies.add(new Branch(20, true), new Branch(18, false));
        controlDependencies.add(new Branch(20, false), new Branch(18, false));

        controlDependencies.add(new Branch(21, true), new Branch(18, false));
        controlDependencies.add(new Branch(21, false), new Branch(18, false));

        controlDependencies.add(new Branch(22, true), new Branch(18, false));
        controlDependencies.add(new Branch(22, false), new Branch(18, false));

        controlDependencies.add(new Branch(23, true), new Branch(22, true));
        controlDependencies.add(new Branch(23, false), new Branch(22, true));

        return controlDependencies.getControlDependenceChain(target);
    }

    @Override
    protected void executeTestObject(Vector vector) {
        daysBetween(getVariable(vector, 0), getVariable(vector, 1), getVariable(vector, 2),
                    getVariable(vector, 3), getVariable(vector, 4), getVariable(vector, 5));
    }

    protected int getVariable(Vector vector, int index) {
        return ((IntegerVariable) vector.getVariable(index)).asInt();
    }

    public int daysBetween(int start_month, int start_day, int start_year,
                           int end_month, int end_day, int end_year) {
        int days = 0;

        // sanitize month inputs
        if (trace.lessThan(1, start_month, 1)) start_month = 1;
        if (trace.lessThan(2, end_month, 1)) end_month = 1;
        if (trace.greaterThan(3, start_month, 12)) start_month = 12;
        if (trace.greaterThan(4, end_month, 12)) end_month = 12;

        // sanitize day inputs
        if (trace.lessThan(5, start_day, 1)) start_day = 1;
        if (trace.lessThan(6, end_day, 1)) end_day = 1;
        if (trace.greaterThan(7, start_day, monthDays(start_month, start_year)))
            start_day = monthDays(start_month, start_year);
        if (trace.greaterThan(8, end_day, monthDays(end_month, end_year)))
            end_day = monthDays(end_month, end_year);

        // swap dates if start date before end date
        boolean swapDates = false;
        if (trace.lessThan(9, end_year, start_year)) {
            swapDates = true;
        }
        if (trace.equals(10, end_year, start_year)) {
            if (trace.lessThan(11, end_month, start_month)) {
                swapDates = true;
            }
        }
        if (trace.equals(12, end_year, start_year)) {
            if (trace.equals(13, end_month, start_month)) {
                if (trace.lessThan(14, end_day, start_day)) {
                    swapDates = true;
                }
            }
        }
        if (trace.isTrue(15, swapDates)) {
            int t = end_month;
            end_month = start_month;
            start_month = t;
            t = end_day;
            end_day = start_day;
            start_day = t;
            t = end_year;
            end_year = start_year;
            start_year = t;
        }

        // calculate days
        if (trace.equals(16, start_month, end_month)) {
            if (trace.equals(17, start_year, end_year)) {
                days = end_day - start_day;
            }
        } else {
            days += monthDays(start_month, start_year) - start_day;
            days += end_day;
            if (trace.equals(18, start_year, end_year)) {
                int month = start_month + 1;
                while (trace.lessThan(19, month, end_month)) {
                    days += monthDays(month, start_year);
                    month++;
                }
            } else {
                int year;
                int month = start_month + 1;
                while (trace.lessThanOrEquals(20, month, 12)) {
                    days += monthDays(month, start_year);
                    month++;
                }
                month = 1;
                while (trace.lessThan(21, month, end_month)) {
                    days += monthDays(month, end_year);
                    month++;
                }
                year = start_year + 1;
                while (trace.lessThan(22, year, end_year)) {
                    days += 365;
                    if (trace.isTrue(23, isLeapYear(year))) days++;
                    year++;
                }
            }
        }

        return days;
    }
}

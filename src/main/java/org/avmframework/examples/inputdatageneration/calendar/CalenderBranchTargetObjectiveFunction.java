package org.avmframework.examples.inputdatageneration.calendar;

import static org.avmframework.examples.inputdatageneration.calendar.Calendar.isLeapYear;
import static org.avmframework.examples.inputdatageneration.calendar.Calendar.monthDays;

import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.BranchTargetObjectiveFunction;
import org.avmframework.examples.inputdatageneration.ControlDependenceChain;
import org.avmframework.examples.inputdatageneration.ControlDependencies;
import org.avmframework.variable.IntegerVariable;

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
    daysBetween(
        getVariable(vector, 0),
        getVariable(vector, 1),
        getVariable(vector, 2),
        getVariable(vector, 3),
        getVariable(vector, 4),
        getVariable(vector, 5));
  }

  protected int getVariable(Vector vector, int index) {
    return ((IntegerVariable) vector.getVariable(index)).asInt();
  }

  public int daysBetween(
      int startMonth, int startDay, int startYear, int endMonth, int endDay, int endYear) {
    int days = 0;

    // sanitize month inputs
    if (trace.lessThan(1, startMonth, 1)) {
      startMonth = 1;
    }
    if (trace.lessThan(2, endMonth, 1)) {
      endMonth = 1;
    }
    if (trace.greaterThan(3, startMonth, 12)) {
      startMonth = 12;
    }
    if (trace.greaterThan(4, endMonth, 12)) {
      endMonth = 12;
    }

    // sanitize day inputs
    if (trace.lessThan(5, startDay, 1)) {
      startDay = 1;
    }
    if (trace.lessThan(6, endDay, 1)) {
      endDay = 1;
    }
    if (trace.greaterThan(7, startDay, monthDays(startMonth, startYear))) {
      startDay = monthDays(startMonth, startYear);
    }
    if (trace.greaterThan(8, endDay, monthDays(endMonth, endYear))) {
      endDay = monthDays(endMonth, endYear);
    }

    // swap dates if start date before end date
    boolean swapDates = false;
    if (trace.lessThan(9, endYear, startYear)) {
      swapDates = true;
    }
    if (trace.equals(10, endYear, startYear)) {
      if (trace.lessThan(11, endMonth, startMonth)) {
        swapDates = true;
      }
    }
    if (trace.equals(12, endYear, startYear)) {
      if (trace.equals(13, endMonth, startMonth)) {
        if (trace.lessThan(14, endDay, startDay)) {
          swapDates = true;
        }
      }
    }
    if (trace.isTrue(15, swapDates)) {
      int temp = endMonth;
      endMonth = startMonth;
      startMonth = temp;
      temp = endDay;
      endDay = startDay;
      startDay = temp;
      temp = endYear;
      endYear = startYear;
      startYear = temp;
    }

    // calculate days
    if (trace.equals(16, startMonth, endMonth)) {
      if (trace.equals(17, startYear, endYear)) {
        days = endDay - startDay;
      }
    } else {
      days += monthDays(startMonth, startYear) - startDay;
      days += endDay;
      if (trace.equals(18, startYear, endYear)) {
        int month = startMonth + 1;
        while (trace.lessThan(19, month, endMonth)) {
          days += monthDays(month, startYear);
          month++;
        }
      } else {
        int month = startMonth + 1;
        while (trace.lessThanOrEquals(20, month, 12)) {
          days += monthDays(month, startYear);
          month++;
        }
        month = 1;
        while (trace.lessThan(21, month, endMonth)) {
          days += monthDays(month, endYear);
          month++;
        }
        int year = startYear + 1;
        while (trace.lessThan(22, year, endYear)) {
          days += 365;
          if (trace.isTrue(23, isLeapYear(year))) {
            days++;
          }
          year++;
        }
      }
    }

    return days;
  }
}

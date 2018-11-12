package org.avmframework.examples.inputdatageneration.calendar;

public class Calendar {

  public static boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
  }

  public static int monthDays(int month, int year) {
    int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    return month == 2 && isLeapYear(year) ? 29 : monthDays[month - 1];
  }

  public static int daysBetween(
      int startMonth, int startDay, int startYear, int endMonth, int endDay, int endYear) {
    int days = 0;

    // sanitize month inputs
    if (startMonth < 1) {
      startMonth = 1;
    }
    if (endMonth < 1) {
      endMonth = 1;
    }
    if (startMonth > 12) {
      startMonth = 12;
    }
    if (endMonth > 12) {
      endMonth = 12;
    }

    // sanitize day inputs
    if (startDay < 1) {
      startDay = 1;
    }
    if (endDay < 1) {
      endDay = 1;
    }
    if (startDay > monthDays(startMonth, startYear)) {
      startDay = monthDays(startMonth, startYear);
    }
    if (endDay > monthDays(endMonth, endYear)) {
      endDay = monthDays(endMonth, endYear);
    }

    // swap dates if start date before end date
    boolean swapDates = false;
    if (endYear < startYear) {
      swapDates = true;
    }
    if (endYear == startYear) {
      if (endMonth < startMonth) {
        swapDates = true;
      }
    }
    if (endYear == startYear) {
      if (endMonth == startMonth) {
        if (endDay < startDay) {
          swapDates = true;
        }
      }
    }
    if (swapDates) {
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
    if (startMonth == endMonth) {
      if (startYear == endYear) {
        days = endDay - startDay;
      }
    } else {
      days += monthDays(startMonth, startYear) - startDay;
      days += endDay;
      if (startYear == endYear) {
        int month = startMonth + 1;
        while (month < endMonth) {
          days += monthDays(month, startYear);
          month++;
        }
      } else {
        int month = startMonth + 1;
        while (month <= 12) {
          days += monthDays(month, startYear);
          month++;
        }
        month = 1;
        while (month < endMonth) {
          days += monthDays(month, endYear);
          month++;
        }
        int year = startYear + 1;
        while (year < endYear) {
          days += 365;
          if (isLeapYear(year)) {
            days++;
          }
          year++;
        }
      }
    }

    return days;
  }
}

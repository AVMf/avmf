package org.avmframework.examples.inputdatageneration.calendar;

public class Calendar {

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int monthDays(int month, int year) {
        int month_days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return month == 2 && isLeapYear(year) ? 29 : month_days[month - 1];
    }

    public static int daysBetween(int start_month, int start_day, int start_year,
                                  int end_month, int end_day, int end_year) {
        int days = 0;

        // sanitize month inputs
        if (start_month < 1) start_month = 1;
        if (end_month < 1) end_month = 1;
        if (start_month > 12) start_month = 12;
        if (end_month > 12) end_month = 12;

        // sanitize day inputs
        if (start_day < 1) start_day = 1;
        if (end_day < 1) end_day = 1;
        if (start_day > monthDays(start_month, start_year))
            start_day = monthDays(start_month, start_year);
        if (end_day > monthDays(end_month, end_year))
            end_day = monthDays(end_month, end_year);

        // swap dates if start date before end date
        boolean swapDates = false;
        if (end_year < start_year) {
            swapDates = true;
        }
        if (end_year == start_year) {
            if (end_month < start_month) {
                swapDates = true;
            }
        }
        if (end_year == start_year) {
            if (end_month == start_month) {
                if (end_day < start_day) {
                    swapDates = true;
                }
            }
        }
        if (swapDates) {
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
        if (start_month == end_month) {
            if (start_year == end_year) {
                days = end_day - start_day;
            }
        } else {
            days += monthDays(start_month, start_year) - start_day;
            days += end_day;
            if (start_year == end_year) {
                int month = start_month + 1;
                while (month < end_month) {
                    days += monthDays(month, start_year);
                    month++;
                }
            } else {
                int year;
                int month = start_month + 1;
                while (month <= 12) {
                    days += monthDays(month, start_year);
                    month++;
                }
                month = 1;
                while (month < end_month) {
                    days += monthDays(month, end_year);
                    month++;
                }
                year = start_year + 1;
                while (year < end_year) {
                    days += 365;
                    if (isLeapYear(year)) days++;
                    year++;
                }
            }
        }

        return days;
    }
}

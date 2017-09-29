/*
 * MIT License
 *
 * Copyright (c) 2016 Kartik Sharma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.crazyhitty.chdev.ks.predator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/11/2017 10:52 AM
 * Description: Unavailable
 */

public class DateUtils {
    private static final String TAG = "DateUtils";

    private static final String PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT = "yyyy-MM-dd";
    private static final String PREDATOR_DATE_PATTERN_FINAL_FORMAT = "MMMM d";
    private static final String PREDATOR_COMPLETE_DATE_PATTERN_ORIGINAL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String PREDATOR_COMPLETE_DATE_PATTERN_FINAL_FORMAT = "EEE, d MMM yyyy h:mm a";

    private DateUtils() {

    }

    /**
     * @return Get current date in default date format.
     */
    public static String getPredatorCurrentDate() {
        return new SimpleDateFormat(PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT, Locale.US)
                .format(Calendar.getInstance().getTime());
    }

    /**
     * Convert post publish date into milliseconds.
     *
     * @param date Post publish date in complete date format
     * @return Returns time in milliseconds according to the date provided.
     */
    public static long predatorDateToMillis(String date) {
        Date providedDate = null;
        try {
            providedDate = new SimpleDateFormat(PREDATOR_COMPLETE_DATE_PATTERN_ORIGINAL_FORMAT, Locale.US)
                    .parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return providedDate.getTime();
    }

    /**
     * Get previous date from current.
     *
     * @param date Post publish date in default date format
     * @return Returns one day before date from provided date.
     */
    public static String getPredatorPostPreviousDate(String date) {
        Date providedDate = null;
        try {
            providedDate = new SimpleDateFormat(PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT, Locale.US)
                    .parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse date: " + date + " with available format: " + PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(providedDate);
        calendar.add(Calendar.DATE, -1);

        return new SimpleDateFormat(PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT, Locale.US)
                .format(calendar.getTime());
    }

    /**
     * Get a appropriate date string that can be shown to the user.
     *
     * @param date Post publish date
     * @return A valid string which can either be "Today", "Yesterday" or some other date.
     */
    public static String getPredatorPostDate(String date) {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar providedCalendar = Calendar.getInstance();

        Date providedDate = null;
        try {
            providedDate = new SimpleDateFormat(PREDATOR_DATE_PATTERN_ORIGINAL_FORMAT, Locale.US)
                    .parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

        providedCalendar.setTime(providedDate);
        String newDate = null;

        switch (daysBetween(currentCalendar, providedCalendar)) {
            case 0:
                newDate = "Today";
                Logger.d(TAG, "getPredatorPostDate: provided: " + date + " ; new: " + newDate);
                return newDate;
            case 1:
                newDate = "Yesterday";
                Logger.d(TAG, "getPredatorPostDate: provided: " + date + " ; new: " + newDate);
                return newDate;
            default:
                newDate = new SimpleDateFormat(PREDATOR_DATE_PATTERN_FINAL_FORMAT, Locale.US)
                        .format(providedDate);
                Logger.d(TAG, "getPredatorPostDate: provided: " + date + " ; new: " + newDate);
                return newDate;
        }
    }

    /**
     * Get complete post publish date.
     *
     * @param date Post publish date, in complete format (Example: 2017-01-11T00:07:13.396-08:00)
     * @return Post publish date in appropriate format (Example: Thu, 13 Jan 2017 1:58 PM).
     */
    public static String getPredatorPostCompleteDate(String date) {
        Date providedDate = null;
        try {
            providedDate = new SimpleDateFormat(PREDATOR_COMPLETE_DATE_PATTERN_ORIGINAL_FORMAT, Locale.US)
                    .parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

        return new SimpleDateFormat(PREDATOR_COMPLETE_DATE_PATTERN_FINAL_FORMAT, Locale.US)
                .format(providedDate);
    }

    /**
     * Converts the number of hours into seconds.
     *
     * @param hours Number of hours in string, for eg: "6"
     * @return Number of hours in seconds.
     */
    public static long hoursToSeconds(String hours) {
        return hoursToSeconds(Integer.parseInt(hours));
    }

    /**
     * Converts the number of hours into seconds.
     *
     * @param hours Number of hours in integer, for eg: 6
     * @return Number of hours in seconds.
     */
    public static long hoursToSeconds(int hours) {
        return hours * 60 * 60;
    }

    /**
     * <a href="http://stackoverflow.com/a/28865648">Source</a>
     * <p>Get number of days between two calendar objects.</p>
     *
     * @param day1 First calendar object
     * @param day2 Second calendar object
     * @return Number of days in int between two available calendars.
     */
    private static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum(), important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }
}

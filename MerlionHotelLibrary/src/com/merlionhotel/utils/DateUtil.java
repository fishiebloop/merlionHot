package com.merlionhotel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }
    
    public static Date convertToDate(String dateString) throws ParseException {
        // Parse only the date part
        Date date = dateTimeFormat.parse(dateString);
        
        // Format to include time as 00:00:00
        String fullDateTimeString = dateTimeFormat.format(date);
        return dateTimeFormat.parse(fullDateTimeString);
    }
    
    public static boolean sameDayLaterThan2AM(Date date, Date checkIn) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        Calendar calendarCheckIn = Calendar.getInstance();
        calendarCheckIn.setTime(checkIn);

        // Check if date and checkIn are on the same day
        boolean sameDay = calendarDate.get(Calendar.YEAR) == calendarCheckIn.get(Calendar.YEAR) &&
                          calendarDate.get(Calendar.DAY_OF_YEAR) == calendarCheckIn.get(Calendar.DAY_OF_YEAR);

        // Create a calendar for 2 AM on the same day
        Calendar twoAM = Calendar.getInstance();
        twoAM.setTime(checkIn);
        twoAM.set(Calendar.HOUR_OF_DAY, 23);
        twoAM.set(Calendar.MINUTE, 15);
        twoAM.set(Calendar.SECOND, 0);
        twoAM.set(Calendar.MILLISECOND, 0);

        // Check if it's the same day and the time is later than 2 AM
        return sameDay && calendarDate.after(twoAM);
    }
}

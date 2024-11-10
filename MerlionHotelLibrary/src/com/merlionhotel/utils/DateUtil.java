package com.merlionhotel.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }
    
    public static Date convertToDate(String[] date, Boolean early) {
        Integer day = Integer.parseInt(date[0]);
        Integer mth = Integer.parseInt(date[1]);
        Integer yr = Integer.parseInt(date[2]);
        if (early) {
            LocalDateTime localDateTime = LocalDateTime.of(yr, mth, day, 9, 0);  
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        LocalDateTime localDateTime = LocalDateTime.of(yr, mth, day, 14, 0);  
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}

package com.merlionhotel.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }
}

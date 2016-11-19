package com.gizwits.util;

import org.joda.time.DateTime;

/**
 * Created by feel on 16/3/26.
 */
public class DateUtil {

    private final static String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";


    public static String getDateString() {
        DateTime date = new DateTime(System.currentTimeMillis());

        return date.toString(DATEFORMAT);

    }
}

package com.openwudi.animal.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by diwu on 17/7/3.
 */

public class TimeUtil {
    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        return sdf.format(new Date());
    }
}

package com.openwudi.animal.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by diwu on 17/7/3.
 */

public class TimeUtil {
    public static final String SDF_WS = "yyyy-MM-ddTHH:mm:ss.fff";

    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getDateTimeAll(){
        SimpleDateFormat sdf = new SimpleDateFormat(SDF_WS);
        return sdf.format(new Date());
    }
}

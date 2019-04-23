package com.openwudi.animal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.api.track.TrackPoint;
import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.ImageUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by diwu on 17/6/28.
 */
public class Utils {
    public static double getDirectDistance(List<TrackPoint> points) {
        if (points == null || points.size() < 2) {
            return 0;
        }
        LatLng current = new LatLng(points.get(0).getLocation().getLatitude(), points.get(0).getLocation().getLongitude());
        LatLng last = new LatLng(points.get(points.size() - 1).getLocation().getLatitude(), points.get(points.size() - 1).getLocation().getLongitude());
        double linearDistance = DistanceUtil.getDistance(current, last);
        return linearDistance;
    }

    public static double getRouteDistance(List<TrackPoint> points) {
        if (points == null || points.size() < 2) {
            return 0;
        }
        // 轨迹距离
        double routeDistance = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            LatLng current = new LatLng(points.get(i).getLocation().getLatitude(), points.get(i).getLocation().getLongitude());
            LatLng next = new LatLng(points.get(i + 1).getLocation().getLatitude(), points.get(i + 1).getLocation().getLongitude());
            routeDistance = routeDistance
                    + DistanceUtil.getDistance(current,
                    next);
        }
        return routeDistance;
    }

    /***
     * 文本截断
     *
     * @param paint      画笔
     * @param ori        要截断的文本
     * @param lengthLeft 最大长度
     * @param moreLength 与画笔的 textSize 相对应的 "..." 的长度
     * @return
     */
    public static String breakTxts(Paint paint, String ori, int lengthLeft, int moreLength) {
        if (TextUtils.isEmpty(ori)) {
            return ori;
        }
        if (lengthLeft <= 0) {
            return "";
        }
        int count = paint.breakText(ori, true, lengthLeft, null);
        if (count < ori.length()) { // 说明需要截断
            lengthLeft -= moreLength;
            if (lengthLeft <= 0) {
                return "";
            }
            return ori.substring(0, paint.breakText(ori, true, lengthLeft, null)) + "...";
        } else {
            return ori;
        }
    }

    public static byte[] compressImage(byte[] original,double lengthM) {
        int w = 0;
        int h = 0;
        while (original != null && ConvertUtils.byte2Size(original.length, ConstUtils.MemoryUnit.MB) > lengthM) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(original, 0, original.length, options);
            if (w == 0) {
                w = options.outWidth;
                h = options.outHeight;
                double ratio=1D;
                if(lengthM>0){
                    ratio=ConvertUtils.byte2Size(original.length, ConstUtils.MemoryUnit.MB)/lengthM;
                }
                if (ratio> 1.0D) {
                    w /= Math.sqrt(ratio);
                    h /= Math.sqrt(ratio);
                }
            }
            try {
                Bitmap b = ImageUtils.getBitmap(original, 0, w, h);
                original = ConvertUtils.bitmap2Bytes(b, Bitmap.CompressFormat.JPEG);
                b.recycle();
            } catch (OutOfMemoryError | Exception ignored) {
                ignored.printStackTrace();
            } finally {
                w *= 0.75;
                h *= 0.75;
            }
        }
        return original;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long string2Long(String strTime, String formatType) {
        Date date = string2Date(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date2Long(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date string2Date(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long date2Long(Date date) {
        return date.getTime();
    }
}

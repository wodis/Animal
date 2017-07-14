package com.openwudi.animal.utils;

import android.graphics.Paint;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.api.track.TrackPoint;

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
}

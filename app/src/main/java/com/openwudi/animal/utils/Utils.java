package com.openwudi.animal.utils;

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
}

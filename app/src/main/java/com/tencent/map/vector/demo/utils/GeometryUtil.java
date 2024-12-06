package com.tencent.map.vector.demo.utils;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class GeometryUtil {
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 计算两个经纬度点之间的距离
     *
     * @param lat1 纬度
     * @param lon1 经度
     * @param lat2 纬度
     * @param lon2 经度
     * @return 单位:米
     */
    public static double CalcDistanceFromLatLon(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378137.0f;
        double d_lat = Math.toRadians(lat2 - lat1);
        double d_lon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(d_lat / 2) * Math.sin(d_lat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(d_lon / 2) * Math.sin(d_lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0f - a));
        return R * c;
    }

    /**
     * 经纬度移动公式
     *
     * @param latLng 指定坐标
     * @param dLat   纬度偏移量，单位：米
     * @param dLon   经度偏移量，单位：米
     * @return 移动之后的坐标
     */
    public static LatLng move(LatLng latLng, double dLat, double dLon) {
        double latRad = Math.toRadians(latLng.latitude);
        double lonRad = Math.toRadians(latLng.longitude);

        // 计算纬度变化对应的弧度
        double latChangeRad = dLat / EARTH_RADIUS;
        // 计算经度变化对应的弧度（需要考虑纬度的影响，因为地球是椭球体）
        double lonChangeRad = dLon / (EARTH_RADIUS * Math.cos(latRad));

        // 计算新的经纬度（弧度制）
        double newLatRad = latRad + latChangeRad;
        double newLonRad = lonRad + lonChangeRad;

        // 将弧度转换回角度
        double newLat = Math.toDegrees(newLatRad);
        double newLon = Math.toDegrees(newLonRad);

        return new LatLng(newLat, newLon);
    }
}

package com.tencent.map.vector.demo.utils;

public class GeometryUtil {

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
}

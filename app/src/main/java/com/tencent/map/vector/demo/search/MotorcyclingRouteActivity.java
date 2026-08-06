package com.tencent.map.vector.demo.search;

import android.os.Bundle;
import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.MotorcyclingParam;
import com.tencent.lbssearch.object.result.MotorcyclingResultObject;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.Arrays;

public class MotorcyclingRouteActivity extends SupportMapFragmentActivity {

    private LatLng fromPoint = new LatLng(40.040219, 116.273348); // 起点坐标
    private LatLng toPoint = new LatLng(40.048055, 116.281166); //终点坐标
    private LatLng wayPoint = new LatLng(40.044000, 116.277000); // 途经点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMotorcyclingRoute();
    }

    /**
     * 获取摩托车路线规划（6.13.0 新增）
     */
    private void getMotorcyclingRoute() {
        MotorcyclingParam param = new MotorcyclingParam(fromPoint, toPoint);
        // 设置策略：最短时间，并偏好实时路况
        param.policy(MotorcyclingParam.Policy.LEAST_TIME, MotorcyclingParam.Preference.REAL_TRAFFIC);
        // 添加途经点
        param.addWayPoint(wayPoint);
        // 批量添加途经点
        // param.addWayPoints(Arrays.asList(wayPoint));
        // 指定起点 POI（可选）
        // param.fromPOI("POI标识");
        // 车牌号、排量、能源类型、多路线、是否考虑实时路况、规避台阶等（可选）
        // param.setPlateNumber("京A12345");
        // param.setDisplacement(125);
         param.setEnergyType(MotorcyclingParam.EnergyType.GASOLINE);
        // param.setMultRoute(1);
        // param.setTrafficSpeed(true);
        // param.setNoStep(0);

        TencentSearch tencentSearch = new TencentSearch(getApplicationContext());
        Log.i("TAG", "checkParams:" + param.checkParams());
        tencentSearch.getRoutePlan(param, new HttpResponseListener<MotorcyclingResultObject>() {
            @Override
            public void onSuccess(int statusCode, MotorcyclingResultObject object) {
                if (object == null) {
                    Log.i("TAG", "baseObject为空");
                    return;
                }
                showMotorcyclingRoute(object);
                Log.i("TAG", "message:" + object.message);
            }

            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                Log.i("TAG:", statusCode + "  " + responseString);
            }
        });
    }

    private void showMotorcyclingRoute(MotorcyclingResultObject object) {
        tencentMap.clearAllOverlays();
        if (object.result != null && object.result.routes != null && object.result.routes.size() > 0) {
            for (int i = 0; i < object.result.routes.size(); i++) {
                MotorcyclingResultObject.Route result = object.result.routes.get(i);
                tencentMap.addPolyline(new PolylineOptions().addAll(result.polyline).color(i + 1).width(20));
                Log.i("TAG", "distance:" + result.distance + " duration:" + result.duration
                        + " mode:" + result.mode + " direction:" + result.direction
                        + " trafficLightCount:" + result.traffic_light_count);
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder()
                        .include(result.polyline).build(), 100));
            }
        } else {
            Log.i("TAG", "路线结果为空");
        }
    }
}

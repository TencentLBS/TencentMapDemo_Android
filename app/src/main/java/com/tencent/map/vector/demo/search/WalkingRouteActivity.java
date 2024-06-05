package com.tencent.map.vector.demo.search;

import android.os.Bundle;
import android.util.Log;

import com.tencent.lbssearch.object.result.RoutePlanningObject;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.WalkingResultObject;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;


public class WalkingRouteActivity extends SupportMapFragmentActivity {

    private LatLng fromPoint = new LatLng(40.040219, 116.273348); // 起点坐标
    private LatLng toPoint = new LatLng(40.048055, 116.281166); //终点坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWalkingRoute();
    }

    /**
     * 获取步行路线规划
     */
    private void getWalkingRoute() {
        WalkingParam walkingParam = new WalkingParam();
        walkingParam.from(fromPoint);
        walkingParam.to(toPoint);
        TencentSearch tencentSearch = new TencentSearch(getApplicationContext());
        Log.i("TAG", "checkParams:" + walkingParam.checkParams());
        tencentSearch.getRoutePlan(walkingParam, new HttpResponseListener<WalkingResultObject>() {
            @Override
            public void onSuccess(int statusCode, WalkingResultObject object) {
                if (object == null) {
                    Log.i("TAG", "baseObject为空");
                    return;
                }
                showWalkingRoute(object);
                Log.i("TAG", "message:" + object.message);
            }

            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                Log.i("TAG:", statusCode + "  " + responseString);
            }
        });
    }

    private void showWalkingRoute(WalkingResultObject object) {
        tencentMap.clearAllOverlays();
        if (object.result != null && object.result.routes != null && object.result.routes.size() > 0) {
            for (int i = 0; i < object.result.routes.size(); i++) {
                WalkingResultObject.Route result = object.result.routes.get(i);
                tencentMap.addPolyline(new PolylineOptions().addAll(result.polyline).color(i + 1).width(20));
                Log.i("TAG", "distance:" + result.distance + " duration:" + result.duration
                        + " mode:" + result.mode + " direction:" + result.direction);
                for (RoutePlanningObject.Step step : result.steps) {
                    Log.i("TAG", "step:" + step.road_name + " " + step.distance + " "
                            + step.instruction + " " + step.act_desc + " " + step.dir_desc);
                }
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder()
                        .include(result.polyline).build(), 100));
            }

        } else {
            Log.i("TAG", "路线结果为空");
        }
    }
}

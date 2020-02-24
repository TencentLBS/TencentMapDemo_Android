package com.tencent.map.vector.demo.search;

import android.os.Bundle;

import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.DrivingParam;
import com.tencent.lbssearch.object.result.DrivingResultObject;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.List;

public class DrivingRouteActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(24.66493,117.09568),
                        15,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
        getWalkingRoute();
    }

    private LatLng fromPoint = new LatLng(24.66493, 117.09568); // 起点坐标
    private LatLng toPoint = new LatLng(26.8857,120.00514); //终点坐标

    /**
     * 获取步行导航规划
     */
    private void getWalkingRoute(){
        DrivingParam drivingParam = new DrivingParam(fromPoint, toPoint); //创建导航参数

        drivingParam.roadType(DrivingParam.RoadType.ON_MAIN_ROAD_BELOW_BRIDGE);
        drivingParam.heading(90);
        drivingParam.accuracy(30);
        TencentSearch tencentSearch = new TencentSearch(this);
        tencentSearch.getRoutePlan(drivingParam, new HttpResponseListener<DrivingResultObject>() {

            @Override
            public void onSuccess(int i, DrivingResultObject drivingResultObject) {
                if (drivingResultObject == null){
                    return;
                }
                for (DrivingResultObject.Route route : drivingResultObject.result.routes){
                    List<LatLng> lines = route.polyline;
                    tencentMap.addPolyline(new PolylineOptions().addAll(lines).color(0x22ff0000));
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {

            }
        });
    }

}


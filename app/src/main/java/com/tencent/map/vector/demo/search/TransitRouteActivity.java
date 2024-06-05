package com.tencent.map.vector.demo.search;

import android.os.Bundle;
import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.TransitParam;
import com.tencent.lbssearch.object.result.TransitResultObject;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.List;

public class TransitRouteActivity extends SupportMapFragmentActivity {
    private LatLng fromPoint = new LatLng(40.127265, 116.208051); // 起点坐标
    private LatLng toPoint = new LatLng(40.040219, 116.273348); //终点坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //移动地图
        tencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(40.080287, 116.244949),
                12,
                0f,
                0f)));
        getTransitRoute();
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("TAG", "onMapClick: "+latLng);
            }
        });
    }

    /**
     * 获取公交路径规划
     * TransitResultObject中的Segment是抽象类，要转换成具体的子类才能获取数据
     */
    private void getTransitRoute() {
        TransitParam transitParam = new TransitParam(fromPoint, toPoint);
        TencentSearch tencentSearch = new TencentSearch(this);
        transitParam.policy(TransitParam.Policy.LEAST_WALKING, TransitParam.Preference.NO_SUBWAY);
        tencentSearch.getRoutePlan(transitParam, new HttpResponseListener<TransitResultObject>() {

            @Override
            public void onSuccess(int i, TransitResultObject transitResultObject) {
                if (transitResultObject == null) {
                    Log.i("TAG", "baseObject为空");
                    return;
                }
                showTransitRoute(transitResultObject);

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Log.d("TransitRouteActivity", "onFailure: " + s);
            }
        });
    }

    private void showTransitRoute(TransitResultObject transitResultObject) {
        tencentMap.clearAllOverlays();
        if (transitResultObject.result != null && transitResultObject.result.routes != null && transitResultObject.result.routes.size() > 0) {
            for (int i = 0; i < transitResultObject.result.routes.size(); i++) {
                TransitResultObject.Route route = transitResultObject.result.routes.get(i);

                List<TransitResultObject.Segment> steps = route.steps;
                for (int j = 0; j < steps.size(); j++) {
                    TransitResultObject.Segment segment = steps.get(j);
                    if (segment instanceof TransitResultObject.Transit) {
                        TransitResultObject.Transit transit = (TransitResultObject.Transit) segment;
                        tencentMap.addPolyline(new PolylineOptions().addAll(transit.lines.get(0).polyline).color(i + 1).width(20));

                    } else if (segment instanceof TransitResultObject.Walking) {
                        TransitResultObject.Walking walking = (TransitResultObject.Walking) segment;
                        tencentMap.addPolyline(new PolylineOptions().addAll(walking.polyline).color(j + 1).lineType(PolylineOptions.LineType.LINE_TYPE_IMAGEINARYLINE).width(20));
                    }

                }
            }

        } else {
            Log.i("TAG", "路线结果为空");
        }
    }
}
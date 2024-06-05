package com.tencent.map.vector.demo.search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.TransitParam;
import com.tencent.lbssearch.object.param.TruckingParam;
import com.tencent.lbssearch.object.result.RoutePlanningObject;
import com.tencent.lbssearch.object.result.TransitResultObject;
import com.tencent.lbssearch.object.result.TruckingResultObject;
import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.List;

public class TruckingRouteActivity extends SupportMapFragmentActivity {
    private LatLng fromPoint = new LatLng(40.127265, 116.208051); // 起点坐标
    private LatLng toPoint = new LatLng(40.040219, 116.273348); //终点坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(40.040219, 116.273348),
                        15,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
        geTruckingRoute();
    }

    private void geTruckingRoute() {
        TruckingParam truckingParam = new TruckingParam();
        truckingParam.from(fromPoint);
        truckingParam.to(toPoint);
        truckingParam.setMultRoute(1);
        //  truckingParam.setNoPolyline(1);
        truckingParam.trafficSpeed(true);
        TencentSearch tencentSearch = new TencentSearch(this);
        tencentSearch.getRoutePlan(truckingParam, new HttpResponseListener<TruckingResultObject>() {

            @Override
            public void onSuccess(int i, TruckingResultObject truckingResultObject) {
                if (null != truckingResultObject.result && null != truckingResultObject.result.routes && truckingResultObject.result.routes.size() > 0) {
                    for (int j = 0; j < truckingResultObject.result.routes.size(); j++) {
                        TruckingResultObject.Result result = truckingResultObject.result;
                        TruckingResultObject.Route route = result.routes.get(j);
                        // List<TruckingResultObject.Step> steps = route.steps;
                        tencentMap.addPolyline(new PolylineOptions().addAll(route.polyline).color(i + 1).width(20));
                        tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder()
                                .include(route.polyline).build(), 100));

                    }
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Log.d("TruckingRouteActivity", "onFailure: " + s);

            }
        });

    }
}
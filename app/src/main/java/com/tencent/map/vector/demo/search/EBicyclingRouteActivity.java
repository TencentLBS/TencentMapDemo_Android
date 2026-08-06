package com.tencent.map.vector.demo.search;

import android.os.Bundle;
import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.EBicyclingParam;
import com.tencent.lbssearch.object.result.EBicyclingResultObject;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

public class EBicyclingRouteActivity extends SupportMapFragmentActivity {

    private LatLng fromPoint = new LatLng(40.040219, 116.273348); // 起点坐标
    private LatLng toPoint = new LatLng(40.048055, 116.281166); //终点坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEBicyclingRoute();
    }

    /**
     * 获取电动自行车路线规划（6.13.0 新增）
     */
    private void getEBicyclingRoute() {
        EBicyclingParam param = new EBicyclingParam(fromPoint, toPoint);
        // 设置策略：最短距离
        param.setPolicy(EBicyclingParam.Policy.SHORT_DISTANCE);
        // 指定起终点 POI（可选）
        // param.fromPOI("POI标识");
        // 途经轮渡时的摆渡次数（可选）
        // param.withFerryCount(true);

        TencentSearch tencentSearch = new TencentSearch(getApplicationContext());
        Log.i("TAG", "checkParams:" + param.checkParams());
        tencentSearch.getRoutePlan(param, new HttpResponseListener<EBicyclingResultObject>() {
            @Override
            public void onSuccess(int statusCode, EBicyclingResultObject object) {
                if (object == null) {
                    Log.i("TAG", "baseObject为空");
                    return;
                }
                showEBicyclingRoute(object);
                Log.i("TAG", "message:" + object.message);
            }

            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                Log.i("TAG:", statusCode + "  " + responseString);
            }
        });
    }

    private void showEBicyclingRoute(EBicyclingResultObject object) {
        tencentMap.clearAllOverlays();
        if (object.result != null && object.result.routes != null && object.result.routes.size() > 0) {
            for (int i = 0; i < object.result.routes.size(); i++) {
                EBicyclingResultObject.Route result = object.result.routes.get(i);
                tencentMap.addPolyline(new PolylineOptions().addAll(result.polyline).color(i + 1).width(20));
                Log.i("TAG", "distance:" + result.distance + " duration:" + result.duration
                        + " mode:" + result.mode + " direction:" + result.direction
                        + " ferryCount:" + result.ferryCount);
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder()
                        .include(result.polyline).build(), 100));
            }
        } else {
            Log.i("TAG", "路线结果为空");
        }
    }
}

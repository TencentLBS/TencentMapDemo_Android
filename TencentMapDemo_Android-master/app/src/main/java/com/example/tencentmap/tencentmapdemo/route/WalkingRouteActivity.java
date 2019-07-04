package com.example.tencentmap.tencentmapdemo.route;

import android.os.Bundle;
import android.widget.Toast;

import com.example.tencentmap.tencentmapdemo.basic.SupportMapFragmentActivity;
import com.google.gson.Gson;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.RoutePlanningParam;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.WalkingResultObject;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.List;

public class WalkingRouteActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWalkingRoute();
    }

    private LatLng fromPoint = new LatLng(39.843, 116.343); // 起点坐标
    private LatLng toPoint = new LatLng(39.232,116.323); //终点坐标

    /**
     * 获取步行导航规划
     */
    private void getWalkingRoute(){
        RoutePlanningParam param = new WalkingParam(); //创建导航参数
        param = param.from(fromPoint).to(toPoint); //写入起终点
        TencentSearch tencentSearch = new TencentSearch(getApplicationContext());
        tencentSearch.getRoutePlan(param, new HttpResponseListener() {

            /**
             * 获取回调
             * @param i
             * @param o
             */
            @Override
            public void onSuccess(int i, Object o) {


                String json = new Gson().toJson(o);
                Toast.makeText(getApplicationContext(), json,Toast.LENGTH_LONG).show();

                //根据返回结果解析

                WalkingResultObject walkingResultObject = new Gson().fromJson(json,WalkingResultObject.class);

                if (walkingResultObject == null || walkingResultObject.result == null || walkingResultObject.result.routes==null)
                    return;

                // 显示其中一条步行路线
                for (WalkingResultObject.Route route : walkingResultObject.result.routes){
                    List<LatLng> points = route.polyline;
                    tencentMap.addPolyline(new PolylineOptions().addAll(points));
                    break;
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), s + new Gson().toJson(throwable),Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.example.tencentmap.tencentmapdemo.basic;

import android.os.Bundle;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class OverseaMapActivity extends SupportMapFragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //纽约时代广场海外地图，需Key开通海外位置服务权限
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(40.75797,-73.985542),
                        11,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
    }
}

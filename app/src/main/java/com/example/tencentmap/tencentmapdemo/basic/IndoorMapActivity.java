package com.example.tencentmap.tencentmapdemo.basic;

import android.os.Bundle;
import android.util.Log;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.IndoorBuilding;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class IndoorMapActivity  extends SupportMapFragmentActivity implements TencentMap.OnIndoorStateChangeListener {
    static IndoorBuilding indoorBuilding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tencentMap.setIndoorEnabled(true);
        //欧美汇室内地图，需Key开通室内地图权限
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(39.979381,116.314128),
                        18,
                        0f,
                        0f));
        tencentMap.moveCamera(cameraSigma);
        //设置室内图状态变化监听
        tencentMap.setOnIndoorStateChangeListener(this);



    }

    @Override
    public boolean onIndoorBuildingFocused() {
        return false;
    }

    @Override
    public boolean onIndoorLevelActivated(IndoorBuilding indoor) {

            indoorBuilding = indoor;
            tencentMap.setIndoorFloor(indoorBuilding.getBuidlingId(), indoorBuilding.getBuildingName());
            tencentMap.setIndoorFloor(3);

        return true;
    }

    @Override
    public boolean onIndoorBuildingDeactivated() {
        return false;
    }
}

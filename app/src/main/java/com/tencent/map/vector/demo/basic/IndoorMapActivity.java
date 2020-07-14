package com.tencent.map.vector.demo.basic;

import android.os.Bundle;
import android.util.Log;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.IndoorBuilding;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class IndoorMapActivity extends SupportMapFragmentActivity implements TencentMap.OnIndoorStateChangeListener {

    private static final String TAG = "IndoorMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //欧美汇室内地图，需Key开通室内地图权限
        CameraUpdate camera =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(39.979381, 116.314128),
                        18,
                        0f,
                        0f));
        tencentMap.moveCamera(camera);
        //设置室内蒙层颜色 sdk版本4.3.5新增接口
        tencentMap.setIndoorMaskColor(R.color.color_C71585);
        //设置室内图状态变化监听
        tencentMap.setOnIndoorStateChangeListener(this);
        tencentMap.setIndoorEnabled(true);
        tencentMap.setMapType(TencentMap.MAP_TYPE_DARK);

    }

    @Override
    public boolean onIndoorBuildingFocused() {
        return false;
    }

    @Override
    public boolean onIndoorLevelActivated(IndoorBuilding indoor) {
        Log.i(TAG, "building name:" + indoor.getBuildingName()
                + ", id:" + indoor.getBuidlingId()
                + ", floor:" + indoor.getLevels().get(indoor.getActiveLevelIndex()).getName());
        return false;
    }

    @Override
    public boolean onIndoorBuildingDeactivated() {
        return false;
    }
}

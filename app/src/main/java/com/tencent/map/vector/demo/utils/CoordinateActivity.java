package com.tencent.map.vector.demo.utils;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.google.gson.Gson;
import com.tencent.tencentmap.mapsdk.maps.Projection;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapLongClickListener;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.VisibleRegion;

public class CoordinateActivity extends SupportMapFragmentActivity implements OnMapLongClickListener, TencentMap.OnMapClickListener {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = findViewById(R.id.tv_info);
        textView.setVisibility(View.VISIBLE);
        tencentMap.setOnMapClickListener(this);
        tencentMap.setOnMapLongClickListener(this);
    }

    /**
     * 点击地图，显示对应点击点的屏幕坐标
     *
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        Projection projection = tencentMap.getProjection();
        Point screen = projection.toScreenLocation(latLng);
        LatLng transferLatLng = projection.fromScreenLocation(screen);
        textView.setText("屏幕坐标：" + new Gson().toJson(screen));
        Toast.makeText(this, new Gson().toJson(transferLatLng), Toast.LENGTH_SHORT).show();
    }


    /**
     * 长点击地图， 显示当前地图的坐标范围
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Projection projection = tencentMap.getProjection();
        VisibleRegion region = projection.getVisibleRegion();
        textView.setText("当前地图视野的经纬度：" + new Gson().toJson(region));
        Toast.makeText(this, new Gson().toJson(region), Toast.LENGTH_LONG).show();
    }
}

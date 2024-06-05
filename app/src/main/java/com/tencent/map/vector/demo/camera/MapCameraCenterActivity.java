package com.tencent.map.vector.demo.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class MapCameraCenterActivity extends SupportMapFragmentActivity implements TencentMap.OnCameraChangeListener, TencentMap.OnMapClickListener {
    private TextView textView;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView = findViewById(R.id.tv_info);
        textView.setVisibility(View.VISIBLE);
        tencentMap.setOnCameraChangeListener(this);
        tencentMap.setOnMapClickListener(this);
    }


    /**
     * 地图视图改变回调
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //获取当前地图视图信息
        info = "经纬度："+cameraPosition.target.latitude+","+cameraPosition.target.longitude+";zoom："+cameraPosition.zoom;
        textView.setText(info);
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {
        //获取当前地图视图信息
        info = "经纬度："+cameraPosition.target.latitude+","+cameraPosition.target.longitude+";zoom："+cameraPosition.zoom;
        Toast.makeText(MapCameraCenterActivity.this,info,Toast.LENGTH_SHORT).show();
        textView.setText(info);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        tencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,15f, 0, 0)));
    }
}

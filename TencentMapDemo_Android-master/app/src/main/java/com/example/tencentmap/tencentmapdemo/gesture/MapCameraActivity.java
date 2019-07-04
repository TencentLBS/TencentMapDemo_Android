package com.example.tencentmap.tencentmapdemo.gesture;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tencentmap.tencentmapdemo.R;
import com.example.tencentmap.tencentmapdemo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;

public class MapCameraActivity extends SupportMapFragmentActivity implements TencentMap.OnCameraChangeListener{
    private TextView textView;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView = findViewById(R.id.tv_info);
        textView.setVisibility(View.VISIBLE);
        tencentMap.setOnCameraChangeListener(this);

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
        textView.setText(info);

    }
}

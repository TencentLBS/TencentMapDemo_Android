package com.example.tencentmap.tencentmapdemo.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.tencentmap.tencentmapdemo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;

public class UiSettingsActivity extends SupportMapFragmentActivity {

    private Switch zoomControl;
    private Switch logoControl;
    private Switch compassControl;
    private Switch scaleViewControl;
    private Switch locationControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        zoomControl = findViewById(R.id.switch_zoom);
        logoControl = findViewById(R.id.switch_logo);
        compassControl = findViewById(R.id.switch_compass);
        scaleViewControl = findViewById(R.id.switch_scale_view);
        locationControl = findViewById(R.id.switch_location);
        zoomControl.setVisibility(View.VISIBLE);
        zoomControl.setText("缩放开关");
        logoControl.setVisibility(View.VISIBLE);
        logoControl.setText("logo位置");
        compassControl.setVisibility(View.VISIBLE);
        compassControl.setText("指南针开关");
        scaleViewControl.setVisibility(View.VISIBLE);
        scaleViewControl.setText("比例尺");
        locationControl.setVisibility(View.VISIBLE);
        locationControl.setText("定位标志");
        zoomControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //打开缩放
                    mapUiSettings.setZoomControlsEnabled(true);
                }else{
                    //关闭缩放
                    mapUiSettings.setZoomControlsEnabled(false);
                }
            }
        });

        logoControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //logo右下角
                    mapUiSettings.setLogoPosition(TencentMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
                }else{
                    //logo左下角
                    mapUiSettings.setLogoPosition(TencentMapOptions.LOGO_POSITION_BOTTOM_LEFT);
                }

            }
        });

        compassControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //显示指南针
                    mapUiSettings.setCompassEnabled(true);
                }else{
                    //隐藏指南针
                    mapUiSettings.setCompassEnabled(false);
                }
            }
        });

        scaleViewControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //显示比例尺
                    mapUiSettings.setScaleViewEnabled(true);
                }else{
                    //关闭比例尺
                    mapUiSettings.setScaleViewEnabled(false);
                }
            }
        });

        locationControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //打开位置标志
                    mapUiSettings.setMyLocationButtonEnabled(true);
                }else{
                    mapUiSettings.setMyLocationButtonEnabled(false);
                }
            }
        });
    }
}

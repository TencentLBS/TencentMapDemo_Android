package com.tencent.map.vector.demo.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;

public class UiSettingsActivity extends SupportMapFragmentActivity {


    private Switch logoControl;
    private Switch compassControl;
    private Switch scaleViewControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){

        logoControl = findViewById(R.id.switch_logo);
        compassControl = findViewById(R.id.switch_compass);
        scaleViewControl = findViewById(R.id.switch_scale_view);
        logoControl.setVisibility(View.VISIBLE);
        logoControl.setText("logo位置");
        compassControl.setVisibility(View.VISIBLE);
        compassControl.setText("指南针");
        scaleViewControl.setVisibility(View.VISIBLE);
        scaleViewControl.setText("比例尺");

        //打开缩放
        mapUiSettings.setZoomControlsEnabled(true);
        //打开位置标志
        mapUiSettings.setMyLocationButtonEnabled(true);


        logoControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //logo左下角
                    mapUiSettings.setLogoPosition(TencentMapOptions.LOGO_POSITION_BOTTOM_LEFT);
                }else{
                    //logo右上角
                    mapUiSettings.setLogoPosition(TencentMapOptions.LOGO_POSITION_TOP_RIGHT);
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
                    //打开比例尺
                    mapUiSettings.setScaleViewEnabled(true);
                }else{
                    //关闭比例尺
                    mapUiSettings.setScaleViewEnabled(false);
                }
            }
        });




    }
}

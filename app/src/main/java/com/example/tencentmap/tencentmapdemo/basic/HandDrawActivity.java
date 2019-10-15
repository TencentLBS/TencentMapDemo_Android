package com.example.tencentmap.tencentmapdemo.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.tencentmap.tencentmapdemo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class HandDrawActivity extends SupportMapFragmentActivity{
    private Switch drawMapControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.072295, 102.761478), 14));
        drawMapControl = findViewById(R.id.switch_map);
        drawMapControl.setVisibility(View.VISIBLE);
        drawMapControl.setText("展示手绘图");
        drawMapControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //打开手绘图
                    tencentMap.setHandDrawMapEnable(true);
                    //长按显示或关闭手绘图
                    tencentMap.setOnMapLongClickListener(new TencentMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            if (tencentMap.isHandDrawMapEnable()){
                                tencentMap.setHandDrawMapEnable(false);
                            }else{
                                tencentMap.setHandDrawMapEnable(true);
                            }
                        }
                    });
                }else{
                    //关闭手绘图
                    tencentMap.setHandDrawMapEnable(false);
                }
            }
        });
    }
}

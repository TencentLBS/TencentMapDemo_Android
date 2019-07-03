package com.example.tencentmap.tencentmapdemo.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.tencentmap.tencentmapdemo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;


public class SetMapStyleActivity extends SupportMapFragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView(){
        radioGroup = findViewById(R.id.lay_map_style);
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.btn_none:
                tencentMap.setMapType(TencentMap.MAP_TYPE_NONE);
                break;
            case R.id.btn_normal:
                tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
                break;
            case R.id.btn_satellite:
                tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.btn_navi:
                tencentMap.setMapType(TencentMap.MAP_TYPE_NAVI);
                break;
            case R.id.btn_night:
                tencentMap.setMapType(TencentMap.MAP_TYPE_NIGHT);
                break;
        }
    }
}

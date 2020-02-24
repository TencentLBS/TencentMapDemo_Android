package com.tencent.map.vector.demo.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;


public class SetMapTypeActivity extends SupportMapFragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView(){
        radioGroup = findViewById(R.id.lay_map_type);
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.btn_normal:

                tencentMap.setMapStyle(TencentMap.MAP_TYPE_NORMAL);
                break;
            case R.id.btn_satellite:

                tencentMap.setMapStyle(TencentMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.btn_navi:
                tencentMap.setMapStyle(TencentMap.MAP_TYPE_NAVI);
                break;
            case R.id.btn_night:
                tencentMap.setMapStyle(TencentMap.MAP_TYPE_NIGHT);
                break;
        }
    }
}

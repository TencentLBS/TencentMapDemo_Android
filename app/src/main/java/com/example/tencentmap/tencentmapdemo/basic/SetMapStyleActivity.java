package com.example.tencentmap.tencentmapdemo.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.tencentmap.tencentmapdemo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

public class SetMapStyleActivity extends SupportMapFragmentActivity implements RadioGroup.OnCheckedChangeListener{
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
        //编号i在官网控制台自行设置，每个Key最多绑定3个自定义地图样式
        switch (i){
            case R.id.btn_baiqian:
                tencentMap.setMapStyle(1);
                break;
            case R.id.btn_moyuan:

                tencentMap.setMapStyle(2);
                break;
            case R.id.btn_zhanyue:
                tencentMap.setMapStyle(3);
                break;
        }
    }
}

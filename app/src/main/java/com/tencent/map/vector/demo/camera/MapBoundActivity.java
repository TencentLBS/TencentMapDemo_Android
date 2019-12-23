package com.tencent.map.vector.demo.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.RestrictBoundsFitMode;

public class MapBoundActivity extends SupportMapFragmentActivity {
    private RadioGroup radioGroup;
    private CheckBox fitHeight;
    private CheckBox fitWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LatLng northeastLatLng = new LatLng(39.984066, 116.307548);
        final LatLng southwestLatLng = new LatLng(39.974066, 116.297548);
        radioGroup = findViewById(R.id.lay_map_bound);
        radioGroup.setVisibility(View.VISIBLE);
        fitWidth = findViewById(R.id.btn_fit_width);
        fitWidth.setText("适应宽度");
        fitHeight = findViewById(R.id.btn_fit_height);
        fitHeight.setText("适应高度");
        fitHeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    tencentMap.setRestrictBounds(new LatLngBounds(northeastLatLng, southwestLatLng), RestrictBoundsFitMode.FIT_HEIGHT);
                }else{

                }
            }
        });

        fitWidth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    tencentMap.setRestrictBounds(new LatLngBounds(northeastLatLng, southwestLatLng), RestrictBoundsFitMode.FIT_WIDTH);
                }else{

                }
            }
        });
    }


}

package com.tencent.map.vector.demo.gesture;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;

public class GestureSettingsActivity extends SupportMapFragmentActivity {

    private CheckBox scrollBox;
    private CheckBox zoomBox;
    private CheckBox rotateBox;
    private CheckBox tiltBox;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioGroup = findViewById(R.id.lay_checkbox);
        radioGroup.setVisibility(View.VISIBLE);
        initView();
    }

    private void initView(){
        scrollBox = findViewById(R.id.btn_scroll);
        zoomBox = findViewById(R.id.btn_zoom);
        rotateBox = findViewById(R.id.btn_rotate);
        tiltBox = findViewById(R.id.btn_slope);


        scrollBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    mapUiSettings.setScrollGesturesEnabled(true);
                }else{
                    mapUiSettings.setScrollGesturesEnabled(false);
                }
            }
        });


        zoomBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    mapUiSettings.setZoomGesturesEnabled(true);
                }else{
                    mapUiSettings.setZoomGesturesEnabled(false);
                }
            }
        });

        rotateBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    mapUiSettings.setRotateGesturesEnabled(true);
                }else{
                    mapUiSettings.setRotateGesturesEnabled(false);
                }
            }
        });

        tiltBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    mapUiSettings.setTiltGesturesEnabled(true);
                }else{
                    mapUiSettings.setTiltGesturesEnabled(false);
                }
            }
        });
    }

}

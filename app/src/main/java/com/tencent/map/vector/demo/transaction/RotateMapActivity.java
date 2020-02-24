package com.tencent.map.vector.demo.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;

public class RotateMapActivity extends SupportMapFragmentActivity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn = findViewById(R.id.btn_bottom);
        btn.setVisibility(View.VISIBLE);
        btn.setText("旋转地图");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.rotateTo(90f,1.5f);
                tencentMap.animateCamera(cameraUpdate);
            }
        });
    }
}

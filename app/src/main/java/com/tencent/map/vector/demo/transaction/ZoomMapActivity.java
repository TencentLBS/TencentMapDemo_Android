package com.tencent.map.vector.demo.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;

public class ZoomMapActivity extends SupportMapFragmentActivity implements View.OnClickListener{

    private Button btn,zoomin,zoomout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn = findViewById(R.id.btn_bottom);
        zoomin = findViewById(R.id.btn_zoomin);
        zoomout = findViewById(R.id.btn_zoomout);
        btn.setOnClickListener(this);
        zoomout.setOnClickListener(this);
        zoomin.setOnClickListener(this);
        btn.setVisibility(View.VISIBLE);
        btn.setText("缩放至17级");
        zoomin.setVisibility(View.VISIBLE);
        zoomout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bottom:
                CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(17f);
                tencentMap.animateCamera(cameraUpdate);
                break;
            case R.id.btn_zoomin:
                CameraUpdate cameraUpdate1 = CameraUpdateFactory.zoomIn();
                tencentMap.animateCamera(cameraUpdate1);
                break;
            case R.id.btn_zoomout:
                CameraUpdate cameraUpdate2 = CameraUpdateFactory.zoomOut();
                tencentMap.animateCamera(cameraUpdate2);
                break;
        }
    }
}

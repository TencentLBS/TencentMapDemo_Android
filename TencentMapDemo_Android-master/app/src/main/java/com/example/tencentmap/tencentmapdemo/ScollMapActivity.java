package com.example.tencentmap.tencentmapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;

public class ScollMapActivity extends SupportMapFragmentActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn = findViewById(R.id.btn_bottom);
        btn.setVisibility(View.VISIBLE);
        btn.setText("平移地图");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.scrollBy(50f,50f);
                tencentMap.animateCamera(cameraUpdate);
            }
        });
    }
}

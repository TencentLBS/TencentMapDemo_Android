package com.tencent.map.vector.demo.gesture;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class MapListenActivity extends SupportMapFragmentActivity implements TencentMap.OnMapClickListener {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = findViewById(R.id.tv_info);
        textView.setVisibility(View.VISIBLE);
        tencentMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        String info = "经纬度：" + latLng.latitude + "," + latLng.longitude;
        Toast.makeText(MapListenActivity.this, info, Toast.LENGTH_SHORT).show();
        textView.setText(info);
    }
}

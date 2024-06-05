package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;

public class MarkerClickActivity extends MarkerActivity implements TencentMap.OnMarkerClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marker.setInfoWindowEnable(false);
        aSwitch.setVisibility(View.GONE);
        tencentMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(),"您点击了标注", Toast.LENGTH_SHORT).show();
        return false;
    }
}

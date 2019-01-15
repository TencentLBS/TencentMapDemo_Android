package com.example.tencentmap.tencentmapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;

public class MarkerInfoWindowActivity extends MarkerActivity implements TencentMap.OnInfoWindowClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aSwitch.setVisibility(View.GONE);

        tencentMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e("marker:","infoWindow click");
        Toast.makeText(getApplicationContext(),"您点击了信息窗", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClickLocation(int i, int i1, int i2, int i3) {

    }

}

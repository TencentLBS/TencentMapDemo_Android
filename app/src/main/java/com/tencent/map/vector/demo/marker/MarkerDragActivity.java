package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;

public class MarkerDragActivity extends MarkerActivity implements TencentMap.OnMarkerDragListener{
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marker.setInfoWindowEnable(false);
        aSwitch.setVisibility(View.GONE);

        textView = findViewById(R.id.tv_info);
        textView.setText("长按标记开始拖动");
        textView.setVisibility(View.VISIBLE);

        marker.setDraggable(true);
        tencentMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.e("marker:","drag");
        Toast.makeText(this, "拖拽开始", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(this, "拖拽结束", Toast.LENGTH_SHORT).show();
    }
}

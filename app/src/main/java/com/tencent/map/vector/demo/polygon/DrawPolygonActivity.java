package com.tencent.map.vector.demo.polygon;

import android.os.Bundle;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;

public class DrawPolygonActivity extends SupportMapFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LatLng[] latLngs = {
                new LatLng(39.984864, 116.305756),
                new LatLng(39.983618, 116.305848),
                new LatLng(39.982347, 116.305966),
                new LatLng(39.982412, 116.308111),
                new LatLng(39.984122, 116.308224),
                new LatLng(39.984955, 116.308099),
                new LatLng(39.984864, 116.305756)
                };
        Polygon polygon = tencentMap.addPolygon(new PolygonOptions().
                add(latLngs).
                fillColor(getResources().getColor(R.color.style)).
                strokeColor(getResources().getColor(R.color.colorPrimary)).
                strokeWidth(1));
        tencentMap.setOnPolygonClickListener(new TencentMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon, LatLng latLng) {

            }
        });
    }
}

package com.tencent.map.vector.demo.poi;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MapPoi;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class PoiClickActivity extends SupportMapFragmentActivity implements TencentMap.OnMapPoiClickListener {
    private Switch poiSwitch;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poiSwitch = findViewById(R.id.switch_poi);
        poiSwitch.setVisibility(View.VISIBLE);
        poiSwitch.setText("Poi显示");
        tencentMap.setOnMapPoiClickListener(this);
        poiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    tencentMap.setPoisEnabled(true);
                }else{
                    tencentMap.setPoisEnabled(false);
                    if (marker != null)
                        marker.remove();
                }
            }
        });
    }

    @Override
    public void onClicked(MapPoi mapPoi) {
        setMarker(mapPoi.getPosition(), mapPoi.getName());
    }

    /**
     * 设置标注
     */
    private void setMarker(LatLng latLng, String name){

        if(marker != null)
            marker.remove();
        MarkerOptions options = new MarkerOptions().position(latLng);
        //设置infowindow
        options.title("地址：");
        options.snippet(name);
        marker = tencentMap.addMarker(options);
        marker.showInfoWindow();
    }
}

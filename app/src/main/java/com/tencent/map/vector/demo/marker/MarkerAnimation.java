package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;

public class MarkerAnimation extends MarkerActivity implements TencentMap.OnMapClickListener{

    private com.tencent.tencentmap.mapsdk.maps.model.TranslateAnimation up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marker.setInfoWindowEnable(false);
        aSwitch.setVisibility(View.GONE);

        up = animate(marker);
        marker.setAnimation(up);
        tencentMap.setOnMapClickListener(this);
    }

    private com.tencent.tencentmap.mapsdk.maps.model.TranslateAnimation animate(Marker marker){
        LatLng latLng = marker.getPosition();
        LatLng newLatlon = new LatLng(latLng.latitude+0.001,latLng.longitude-0.001);
        com.tencent.tencentmap.mapsdk.maps.model.TranslateAnimation translateAnimation = new com.tencent.tencentmap.mapsdk.maps.model.TranslateAnimation(newLatlon);
        translateAnimation.setDuration(3000);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        return translateAnimation;

    }

    @Override
    public void onMapClick(LatLng latLng) {

        marker.startAnimation();
    }
}

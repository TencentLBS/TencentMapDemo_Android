package com.tencent.map.vector.demo.camera;

import android.os.Bundle;

import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.Projection;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.RestrictBoundsFitMode;
import com.tencent.tencentmap.mapsdk.maps.model.VisibleRegion;

public class MapAnchorZoomActivity extends SupportMapFragmentActivity {
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LatLng latLng = new LatLng(39.984108,116.307557);

        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng,
                        15,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
        Projection projection = tencentMap.getProjection();

        VisibleRegion region = projection.getVisibleRegion();

        tencentMap.setRestrictBounds(new LatLngBounds(region.farRight, region.nearLeft), RestrictBoundsFitMode.FIT_HEIGHT);
        tencentMap.setRestrictBounds(new LatLngBounds(region.farRight, region.nearLeft), RestrictBoundsFitMode.FIT_WIDTH);
        setMarker(latLng);
    }
    /**
     * 设置标注
     */
    private void setMarker(LatLng latLng){

        MarkerOptions options = new MarkerOptions().position(latLng);

        //设置infowindow
        options.title("锚点");
        marker = tencentMap.addMarker(options);
        marker.setInfoWindowEnable(true);
    }
}

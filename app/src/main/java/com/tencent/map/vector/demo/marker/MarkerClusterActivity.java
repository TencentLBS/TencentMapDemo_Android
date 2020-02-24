package com.tencent.map.vector.demo.marker;

import android.os.Bundle;

import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterItem;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterManager;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;

public class MarkerClusterActivity extends SupportMapFragmentActivity {
    private ClusterManager clusterManager;
    private DefaultClusterRenderer defaultClusterRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clusterManager = new ClusterManager(this, tencentMap);
        defaultClusterRenderer = new DefaultClusterRenderer(this, tencentMap, clusterManager);
        defaultClusterRenderer.setMinClusterSize(1);
        clusterManager.setRenderer(defaultClusterRenderer);
        clusterManager.addItems(getMarkers());

        //添加聚合
        tencentMap.setOnCameraChangeListener(clusterManager);
    }

    private ArrayList<MapMarker> getMarkers(){
        ArrayList<MapMarker> items = new ArrayList<>();
        items.add(new MapMarker(39.984059,116.307621));
        items.add(new MapMarker(39.981954,116.304703));
        items.add(new MapMarker(39.984355,116.312256));
        items.add(new MapMarker(39.980442,116.315346));
        items.add(new MapMarker(39.981527,116.308994));
        items.add(new MapMarker(39.979751,116.310539));
        items.add(new MapMarker(39.977252,116.305776));
        items.add(new MapMarker(39.984026,116.316419));
        items.add(new MapMarker(39.976956,116.314874));
        items.add(new MapMarker(39.978501,116.311827));
        items.add(new MapMarker(39.980277,116.312814));
        items.add(new MapMarker(39.980236,116.369022));
        return items;

    }
    /**
     * 实现ClusterItem接口
     */
    private class MapMarker implements ClusterItem{
        private double lat, lon;
        public MapMarker(double latitude,double longitude) {
            lat = latitude;
            lon = longitude;
        }

        @Override
        public LatLng getPosition() {
            return new LatLng(lat, lon);
        }
    }
}

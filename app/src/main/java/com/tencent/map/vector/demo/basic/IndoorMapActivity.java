package com.tencent.map.vector.demo.basic;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.IndoorBuilding;
import com.tencent.tencentmap.mapsdk.maps.model.IndoorInfo;
import com.tencent.tencentmap.mapsdk.maps.model.IndoorLevel;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MapPoi;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterItem;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterManager;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;

public class IndoorMapActivity extends AppCompatActivity implements TencentMap.OnIndoorStateChangeListener {

    private static final String TAG = "IndoorMapActivity";
    private MapView mapView;
    private TencentMap tencentMap;
    private IndoorFloorView indoorFloorView;
    private UiSettings uiSettings;
    private Handler handler = new Handler();
    private String floorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);
        mapView = findViewById(R.id.mapView);
        indoorFloorView = findViewById(R.id.indoor_floor);
        if (tencentMap == null) {
            tencentMap = mapView.getMap();
        }
        //欧美汇室内地图，需Key开通室内地图权限
        CameraUpdate camera =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(39.979382, 116.314106),
                        18,
                        0f,
                        0f));
        tencentMap.moveCamera(camera);
        final ArrayList<MarkerCluster> markerClusters = new ArrayList<>();

        uiSettings = tencentMap.getUiSettings();
        tencentMap.setIndoorEnabled(true);
        //设置室内蒙层颜色 sdk版本4.3.5新增接口
        tencentMap.setIndoorMaskColor(R.color.color_C71585);
        //设置室内图状态变化监听
        tencentMap.setOnIndoorStateChangeListener(this);
        tencentMap.setMapType(TencentMap.MAP_TYPE_DARK);
        //设置是否隐藏楼层控件
        uiSettings.setIndoorLevelPickerEnabled(false);
        indoorFloorView.setOnIndoorFloorListener(new MyIndoorViewAdapter());
        ClusterManager<MarkerCluster> markerClusterClusterManager = new ClusterManager<MarkerCluster>(this, tencentMap);
        NonHierarchicalDistanceBasedAlgorithm<MarkerCluster> nba = new NonHierarchicalDistanceBasedAlgorithm<>(this);
        nba.setMaxDistanceAtZoom(15);
        markerClusterClusterManager.setAlgorithm(nba);
        DefaultClusterRenderer<MarkerCluster> renderer = new DefaultClusterRenderer<>(this, tencentMap, markerClusterClusterManager);
        renderer.setMinClusterSize(2);
        renderer.setBuckets(new int[]{5, 10, 20});
        markerClusterClusterManager.setRenderer(renderer);
        //添加室内图marker
        tencentMap.setOnMapPoiClickListener(new TencentMap.OnMapPoiClickListener() {
            @Override
            public void onClicked(MapPoi mapPoi) {
                LatLng position = mapPoi.getPosition();
                MarkerOptions markerOptions = new MarkerOptions().position(position).title(mapPoi.name).infoWindowEnable(true);
                if (!TextUtils.isEmpty("F3")) {
                    markerOptions.indoorInfo(new IndoorInfo("11000023805", "F3"));

                }
                Marker marker = tencentMap.addMarker(markerOptions);
                LatLng position1 = mapPoi.getPosition();
                markerClusters.add(new MarkerCluster(position1.getLatitude(), position1.getLongitude()));

            }
        });
        tencentMap.setOnCameraChangeListener(markerClusterClusterManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public boolean onIndoorBuildingFocused() {
        Log.d(TAG, "onIndoorBuildingFocused: 室内图场景激活回调");
        return false;
    }

    @Override
    public boolean onIndoorLevelActivated(final IndoorBuilding indoorBuilding) {
        if (indoorBuilding != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    indoorFloorView.setVisible(true);
                    if (mIndoorBuilding == null || !mIndoorBuilding.getBuidlingId().equals(indoorBuilding.getBuidlingId())) {
                        List<IndoorLevel> levels = indoorBuilding.getLevels();
                        String[] activedIndoorFloorNames1 = tencentMap.getActivedIndoorFloorNames();
                        indoorFloorView.setItems(activedIndoorFloorNames1);
                        for (int i = 0; i < levels.size(); i++) {
                            floorName = levels.get(i).getName();
                            indoorFloorView.setSeletion(floorName);
                        }
                    }
                    mIndoorBuilding = indoorBuilding;
                }
            });
        } else {
            indoorFloorView.setVisible(false);
        }
        return false;
    }

    @Override
    public boolean onIndoorBuildingDeactivated() {
        Log.d(TAG, "onIndoorBuildingDeactivated: 当前室内图处于无效状态");
        return false;
    }

    IndoorBuilding mIndoorBuilding = null;

    private class MyIndoorViewAdapter implements IndoorFloorView.OnIndoorFloorListener {

        @Override
        public void onSelected(int selectedIndex) {
            if (mIndoorBuilding != null) {

                List<IndoorLevel> levels = mIndoorBuilding.getLevels();
                String activedIndoorFloorName = tencentMap.getActivedIndoorFloorNames()[selectedIndex];
                String name = levels.get(selectedIndex).getName();
                name = activedIndoorFloorName;
                String buidlingId = mIndoorBuilding.getBuidlingId();
                tencentMap.setIndoorFloor(buidlingId, name);
            }
        }
    }
}

class MarkerCluster implements ClusterItem {

    private final LatLng latLng;

    public MarkerCluster(double lat, double longitude) {
        latLng = new LatLng(lat, longitude);
    }

    @Override
    public LatLng getPosition() {

        return latLng;
    }
}
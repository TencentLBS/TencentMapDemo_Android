package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerCollisionItem;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerCollisionRelation;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerCollisionRelationUnit;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.OverlayLevel;

import java.util.List;

public class MarkerUpgradedCollisions extends AppCompatActivity {

    private MapView mapView;
    private TencentMap tencentMap;
    private boolean mMarkerAdded;
    private Marker fixedMarker;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_collisions);
        intit();
    }

    private void intit() {
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(39.984066, 116.307548),
                        15,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
        // 设置碰撞监听
        tencentMap.setOnMarkerCollisionStatusListener(new TencentMap.OnMarkerCollisionStatusListener() {
            @Override
            public void onCollisionShown(Marker marker, List<MarkerCollisionRelationUnit> list) {

            }

            @Override
            public void onCollisionHidden(Marker marker, List<MarkerCollisionRelationUnit> list) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.markercolupgrade, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_open_collisionsmap).setVisible(!mMarkerAdded);
        menu.findItem(R.id.menu_close_collisionsmap).setVisible(mMarkerAdded);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_marker_default:
                mMarkerAdded = true;
                fixedMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(1));
                // 缩放地图使两个Marker碰撞，zIndex小的会被碰掉
                mMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066 + 0.005, 116.307548 + 0.006))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(2));
                break;
            case R.id.menu_add_marker_sub_alone:
                mMarkerAdded = true;
                fixedMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(1));
                // 缩放地图使两个Marker碰撞，zIndex小的会被碰掉，碰撞关系为Alone时，子Marker被碰掉时，父Marker不会被碰掉
                // 父Marker被碰掉时，子Marker也会被碰掉
                mMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066 - 0.01, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .setCollisionRelation(MarkerCollisionRelation.ALONE)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                // 添加子Marker
                mMarker.addCollisionUnit(new MarkerOptions(new LatLng(39.984066 - 0.005, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.navi_marker_location))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                break;
            case R.id.menu_add_marker_sub_together:
                mMarkerAdded = true;
                fixedMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(1));
                // zIndex小的会被碰掉，碰撞关系为Together时，子Marker被碰掉时，父Marker也会被碰掉
                mMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066 - 0.01, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .setCollisionRelation(MarkerCollisionRelation.TOGETHER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                // 添加子Marker
                mMarker.addCollisionUnit(new MarkerOptions(new LatLng(39.984066 - 0.005, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.navi_marker_location))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                break;
            case R.id.menu_add_marker_infowindow_poi:
                mMarkerAdded = true;
                fixedMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(1));
                // 点击Marker后InfoWindow会出现，InfoWindow会碰掉POI
                mMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066 - 0.01, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .title("标题")
                        .infoWindowCollisionBy(MarkerCollisionItem.POI)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                break;
            case R.id.menu_add_marker_infowindow_alone:
                mMarkerAdded = true;
                fixedMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(1));
                // 点击Marker后InfoWindow会出现，InfoWindow会碰掉POI，碰撞关系为Alone时，InfoWindow被碰掉时，Marker不会被碰掉
                // 父Marker被碰掉时，InfoWindow也会被碰掉
                mMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066 - 0.01, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .setCollisionRelation(MarkerCollisionRelation.ALONE)
                        .title("标题")
                        .infoWindowCollisionBy(MarkerCollisionItem.POI, MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                break;
            case R.id.menu_add_marker_infowindow_together:
                mMarkerAdded = true;
                fixedMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(1));
                // 点击Marker后InfoWindow会出现，碰撞关系为Together时，InfoWindow碰撞关系跟随Marker。
                // InfoWindow被碰掉时，Marker也会被碰掉
                mMarker = tencentMap.addMarker(new MarkerOptions(new LatLng(39.984066 - 0.01, 116.307548))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
                        .collisionBy(MarkerCollisionItem.MARKER)
                        .setCollisionRelation(MarkerCollisionRelation.TOGETHER)
                        .title("标题")
                        // 设置了碰撞POI但不会生效，因为碰撞关系跟随Marker
                        .infoWindowCollisionBy(MarkerCollisionItem.POI, MarkerCollisionItem.MARKER)
                        .level(OverlayLevel.OverlayLevelAboveLabels)
                        .zIndex(0));
                break;
            case R.id.menu_close_collisionsmap:
                mMarkerAdded = false;
                fixedMarker.remove();
                mMarker.remove();
                break;
        }

        supportInvalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
}
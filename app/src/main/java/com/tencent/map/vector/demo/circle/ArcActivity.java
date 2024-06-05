package com.tencent.map.vector.demo.circle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.Arc;
import com.tencent.tencentmap.mapsdk.maps.model.ArcOptions;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerCollisionItem;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class ArcActivity extends AppCompatActivity {

    private MapView mapView;
    private TencentMap tencentMap;
    private boolean mMarkerAdded;
    private Arc arc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);
        init();
    }

    private void init() {
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(32.059352, 118.796623),
                        5,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.markercol, menu);
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
            case R.id.menu_open_collisionsmap:
                mMarkerAdded = true;
                LatLng startLat = new LatLng(39.89491, 116.322056);
                LatLng endtLat = new LatLng(22.547, 114.085947);
                LatLng passLat = new LatLng(32.059352, 118.796623);
                ArcOptions arcOptions = new ArcOptions();
                arcOptions.points(startLat, endtLat);
                arcOptions.pass(passLat);
                //设置起点到终点，与起点外切线逆时针旋转的夹角角度
                arcOptions.angle(30);
                //设置线宽，默认5
                arcOptions.width(2);
                //设置线颜色，默认黑色
                arcOptions.color(Color.BLUE);
                arc = tencentMap.addArc(arcOptions);
                break;
            case R.id.menu_close_collisionsmap:
                mMarkerAdded = false;
                arc.remove();
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
package com.tencent.map.vector.demo.heatoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tencent.map.sdk.utilities.visualization.datamodels.WeightedLatLng;
import com.tencent.map.sdk.utilities.visualization.heatmap.GradientVectorOverlay;
import com.tencent.map.sdk.utilities.visualization.heatmap.GradientVectorOverlayProvider;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HeatMapVectorOverlayActivity extends AppCompatActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;
    private GradientVectorOverlay heatTileOverlay;
    private GradientVectorOverlayProvider heatMapTileProvider;
    boolean mIsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map_vector_overlay_design);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vectorheatmap, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_open_vectorheatmap).setVisible(!mIsSelected);
        menu.findItem(R.id.menu_close_vectorheatmap).setVisible(mIsSelected);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_vectorheatmap:
                addHeatMap();
                break;
            case R.id.menu_close_vectorheatmap:
                mIsSelected = false;
                if (heatTileOverlay != null) {
                    heatTileOverlay.remove();
                    heatTileOverlay = null;
                }
                break;
        }

        supportInvalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    private void init() {
        mMapView = findViewById(R.id.mapView);
        mTencentMap = mMapView.getMap();
        mTencentMap.setMapStyle(3);
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(31.085414, 111.024319),
                        4,
                        20f,
                        1f));
        mTencentMap.moveCamera(cameraSigma);
    }


    private void addHeatMap() {
        mIsSelected = true;
        if (heatTileOverlay != null) {
            return;
        }
        mTencentMap.setBuildingEnable(false);
        mTencentMap.setPoisEnabled(false);
        BufferedReader br = null;
        try {
            ArrayList<WeightedLatLng> nodes = new ArrayList<WeightedLatLng>();
            br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("data2k")));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split("\t");
                if (lines.length == 3) {
                    double value = Double.parseDouble(lines[2]);
                    LatLng latLng = new LatLng((Double.parseDouble(lines[1])), (Double.parseDouble(lines[0])));
                    nodes.add(new WeightedLatLng(latLng, value));
                }
            }

            // 通用配色1
            int[] colors = {
                    Color.argb(255, 77, 111, 255),
                    Color.argb(255, 0, 238, 227),
                    Color.argb(255, 113, 236, 80),
                    Color.argb(255, 255, 176, 0),
                    Color.argb(255, 255, 72, 0)
            };
            float[] startPoints = {0.1f, 0.185f, 0.3571f, 0.6142f, 1f};


            heatMapTileProvider = new GradientVectorOverlayProvider()
                    .weightedData(nodes).enable3D(true)
                    .radius(56).setMaxHeight(500).setAnimateDuration(3000);
            heatMapTileProvider.opacity(0.95f);
            heatMapTileProvider.gradient(colors, startPoints);
            heatTileOverlay = mTencentMap.addVectorOverlay(heatMapTileProvider);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
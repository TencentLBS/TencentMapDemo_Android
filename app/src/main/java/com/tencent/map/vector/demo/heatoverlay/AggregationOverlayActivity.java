package com.tencent.map.vector.demo.heatoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tencent.map.sdk.utilities.visualization.aggregation.AggregationOverlay;
import com.tencent.map.sdk.utilities.visualization.aggregation.AggregationOverlayProvider;
import com.tencent.map.sdk.utilities.visualization.aggregation.HoneyCombVectorOverlayProvider;
import com.tencent.map.sdk.utilities.visualization.datamodels.WeightedLatLng;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AggregationOverlayActivity extends AppCompatActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;
    private AggregationOverlay aggregationOverlay;
    private AggregationOverlayProvider aggregationOverlayProvider;
    private Switch btnSwitch3D;

    boolean mIsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggregation_overlay_design);
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
                addAggregation();
                break;
            case R.id.menu_close_vectorheatmap:
                mIsSelected = false;
                if (aggregationOverlay != null) {
                    aggregationOverlay.remove();
                    aggregationOverlay = null;
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
        mTencentMap.moveCamera(CameraUpdateFactory.zoomTo(10f));
        btnSwitch3D = (Switch) findViewById(R.id.btn_switch_3D);
        bindListener();
    }


    private void bindListener() {

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.btn_switch_3D:
                        if (aggregationOverlay != null) {
                            if (compoundButton.isChecked()) {
                                aggregationOverlayProvider.enable3D(true);
                                aggregationOverlayProvider.enable3D(true).setAnimateDuration(3000);
                            } else {
                                aggregationOverlayProvider.enable3D(false);
                            }
                            mTencentMap.updateVectorOverlay(aggregationOverlay, aggregationOverlayProvider);
                        }
                        break;
                    default:
                        break;
                }
            }
        };


        btnSwitch3D.setOnCheckedChangeListener(onCheckedChangeListener);

    }

    protected void addAggregation() {
        mIsSelected = true;
        if (aggregationOverlay != null) {
            return;
        }
        //  mTencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.961629, 116.355343), 12, 0, 0)));
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

            int[] colors = {
                    Color.argb(255, 0x01, 0x2C, 0x4B),
                    Color.argb(255, 0x17, 0x3E, 0x59),
                    Color.argb(255, 0x3A, 0x73, 0x78),
                    Color.argb(255, 0x98, 0xCD, 0x9A),
                    Color.argb(255, 0xF6, 0xFB, 0xB6)
            };
            double[] startPoints = {0.f, 0.1f, 0.15f, 0.3f, 0.5f};


            aggregationOverlayProvider = new HoneyCombVectorOverlayProvider()
                    .nodes(nodes.toArray(new WeightedLatLng[0]))
                    .setHeightRange(0, 4000);
            aggregationOverlayProvider.colors(colors, startPoints);
            aggregationOverlay = mTencentMap.addVectorOverlay(aggregationOverlayProvider);


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
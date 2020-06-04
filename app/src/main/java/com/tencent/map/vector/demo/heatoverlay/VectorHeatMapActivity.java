package com.tencent.map.vector.demo.heatoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.tencent.map.sdk.utilities.heatmap.WeightedLatLng;
import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.VectorHeatAggregationUnit;
import com.tencent.tencentmap.mapsdk.maps.model.VectorHeatOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.VectorHeatOverlayOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 蜂窝热力图
 */
public class VectorHeatMapActivity extends SupportMapFragmentActivity {

    MapView mMapView;
    TencentMap mTencentMap;
    VectorHeatOverlay mOverlay;
    boolean mIsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_heat_map);
        mMapView = findViewById(R.id.map_view);
        mTencentMap = mMapView.getMap();
        mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(36.139985, 120.402243),
                11));

        mTencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {
                if (mOverlay == null) {
                    return;
                }
                VectorHeatAggregationUnit unit = mOverlay.getUnit(latlng);
                if (unit == null) {
                    Log.e("VectorHeatMapActivity", "no unit found");
                    return;
                }
                Log.e("VectorHeatMapActivity", "unit center:" + unit.getCenter() +
                        ", intensity:" + unit.getIntensity() +
                        ", size:" + unit.getNodes().length);
            }
        });
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
                addVectorHeatMap();
                break;
            case R.id.menu_close_vectorheatmap:
                removeVectorHeatMap();
                break;
        }

        supportInvalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<WeightedLatLng> getHeatNodes() {
        BufferedReader br = null;
        ArrayList<WeightedLatLng> nodes = null;
        try {
            nodes = new ArrayList<>();
            br = new BufferedReader(new InputStreamReader(
                    getResources().getAssets().open("honeycomb.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split("\t");
                if (lines.length == 3) {
                    double value = Double.parseDouble(lines[2]);
                    LatLng latLng = new LatLng((Double.parseDouble(lines[1])),
                            (Double.parseDouble(lines[0])));
                    nodes.add(new WeightedLatLng(latLng, 1));
                }
            }
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
            return nodes;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMapView != null) {
            mMapView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    private void addVectorHeatMap() {
        mIsSelected = true;
        if (mOverlay != null) {
            return;
        }
        WeightedLatLng[] nodes = getHeatNodes().toArray(new WeightedLatLng[0]);
        VectorHeatOverlayOptions options = new VectorHeatOverlayOptions()
                .nodes(nodes)
                .size(2000)
                .gap(100)
                .colors(new int[]{0x45fa4b14, 0x99fa4b14, 0xd9fa4b14})
                .startPoints(new double[]{0, 0.6, 0.8});
        mOverlay = mTencentMap.addVectorHeatOverlay(options);
    }

    public void removeVectorHeatMap() {
        mIsSelected = false;
        if (mOverlay == null) {
            return;
        }
        mOverlay.remove();
        mOverlay = null;
    }
}

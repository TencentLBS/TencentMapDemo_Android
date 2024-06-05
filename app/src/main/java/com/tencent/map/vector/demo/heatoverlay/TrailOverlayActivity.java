package com.tencent.map.vector.demo.heatoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tencent.map.sdk.utilities.visualization.datamodels.TimeLatLng;
import com.tencent.map.sdk.utilities.visualization.datamodels.TrailLatLng;
import com.tencent.map.sdk.utilities.visualization.trails.TrailOverlayProvider;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.VectorOverlay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TrailOverlayActivity extends AppCompatActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;
    private VectorOverlay vectorOverlay;
    boolean mIsSelected;
    private TrailOverlayProvider trailOverlayProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_overlay);
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
                addArcLineLayer();
                break;
            case R.id.menu_close_vectorheatmap:
                mIsSelected = false;
                if (vectorOverlay != null) {
                    vectorOverlay.remove();
                    vectorOverlay = null;
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
        mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.942117, 116.33668), 11));
    }

    private void addArcLineLayer() {
        mIsSelected = true;
        if (vectorOverlay == null) {

            mTencentMap.setPoisEnabled(false);
            BufferedReader br = null;
            long currentTime = System.currentTimeMillis();
            List<TrailLatLng> startLats = new ArrayList<>();
            try {
                br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("trailData.dat")));

                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] oneDot = line.split(",");
                    TimeLatLng[] timeLatLngs = new TimeLatLng[oneDot.length];
                    int i = 0;
                    for (String timeLatLng : oneDot) {
                        String[] values = timeLatLng.split("\t");
                        LatLng latlng = new LatLng((Double.parseDouble(values[0])), (Double.parseDouble(values[1])));
                        int timeStamp = Integer.parseInt(values[2]);
                        timeLatLngs[i++] = new TimeLatLng(latlng, timeStamp);
                    }
                    TrailLatLng tmp = new TrailLatLng(timeLatLngs);
                    startLats.add(tmp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int[] colors = {Color.argb(0xff, 0x00, 0x00, 0xff),
                    Color.argb(0x00, 0xff, 0xff, 0x00)};

            trailOverlayProvider = new TrailOverlayProvider().type(TrailOverlayProvider.TrailOverlayType.Trail).data(startLats).opacity(2f).width(10).gradient(colors).setHighlightDuration(8000).setAnimateStartTime(0, 25000);
            vectorOverlay = mTencentMap.addVectorOverlay(trailOverlayProvider);


        }

    }
}
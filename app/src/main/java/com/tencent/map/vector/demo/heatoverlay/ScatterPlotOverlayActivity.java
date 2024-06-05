package com.tencent.map.vector.demo.heatoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.tencent.map.sdk.utilities.visualization.datamodels.ScatterLatLng;
import com.tencent.map.sdk.utilities.visualization.scatterplot.BitmapScatterPlotOverlayProvider;
import com.tencent.map.sdk.utilities.visualization.scatterplot.DotScatterPlotOverlayProvider;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.VectorOverlay;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;

public class ScatterPlotOverlayActivity extends AppCompatActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;
    private VectorOverlay vectorOverlay;
    boolean mIsSelected;
    private DotScatterPlotOverlayProvider dotScatterPlotOverlayProvider;
    private BitmapScatterPlotOverlayProvider provider;
    private String[] styles = new String[]{"添加点散点", "添加图片散点"};
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter_plot_overlay);
        init();
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
        mTencentMap.moveCamera(CameraUpdateFactory.zoomTo(5f));
        mTencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(40.040219, 116.273348), 20, 0, 0)));
        mSpinner = findViewById(R.id.sp_style);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, styles);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (vectorOverlay != null) {
                            vectorOverlay.remove();
                            vectorOverlay = null;
                        }
                        addDotVectorOverlay();
                        mTencentMap.updateVectorOverlay(vectorOverlay, dotScatterPlotOverlayProvider);
                        break;
                    case 1:
                        if (vectorOverlay != null) {
                            vectorOverlay.remove();
                            vectorOverlay = null;
                        }
                        addBitmapVectorOverlay();
                        mTencentMap.updateVectorOverlay(vectorOverlay, provider);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void addDotVectorOverlay() {
        mIsSelected = true;
        if (vectorOverlay == null) {
            BufferedReader br = null;
            final ArrayList<ScatterLatLng> nodes = new ArrayList<ScatterLatLng>();
            double lat = 40.040219;
            double lng = 116.273348;
            double value = 100;
            Random r = new Random();
            for (int i = 0; i < 2000; i++) {
                double a = r.nextInt(10) * 1E-5;
                double b = r.nextInt(10) * 1E-5;
                LatLng latLng = new LatLng(lat + a, lng + b);
                nodes.add(new ScatterLatLng(latLng, value));
            }
            int[] colors = {
                    Color.argb(255, 0, 0xd4, 0xff),
                    Color.argb((int) (255 * 0.3), 0, 0xd4, 0xff),
                    Color.argb(255, 0, 0xd4, 0xff)
            };

            dotScatterPlotOverlayProvider = new DotScatterPlotOverlayProvider().data(nodes).opacity(2f).radius(2).animate(true).colors(colors);

            vectorOverlay = mTencentMap.addVectorOverlay(dotScatterPlotOverlayProvider);

        }

    }

    private void addBitmapVectorOverlay() {
        mIsSelected = true;
        if (vectorOverlay == null) {
            final ArrayList<ScatterLatLng> nodes = new ArrayList<ScatterLatLng>();
            double lat = 40.040219;
            double lng = 116.273348;
            double value = 10;
            Random r = new Random();
            for (int i = 0; i < 1000; i++) {
                double a = r.nextInt(10) * 1E-5;
                double b = r.nextInt(10) * 1E-5;
                LatLng latLng = new LatLng(lat + a, lng + b);
                nodes.add(new ScatterLatLng(latLng, value));
            }
            BitmapDescriptor[] bitmaps = {
                    BitmapDescriptorFactory.fromAsset("21.jpg"),
                    BitmapDescriptorFactory.fromAsset("22.jpg"),
                    BitmapDescriptorFactory.fromAsset("23.jpg"),
                    BitmapDescriptorFactory.fromAsset("24.jpg"),
                    BitmapDescriptorFactory.fromAsset("25.jpg"),
                    BitmapDescriptorFactory.fromAsset("26.jpg"),
            };
            provider = new BitmapScatterPlotOverlayProvider().data(nodes).bitmaps(bitmaps).opacity(2f).scale(20, 10);
            vectorOverlay = mTencentMap.addVectorOverlay(provider);
        }

    }
}
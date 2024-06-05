package com.tencent.map.vector.demo.heatoverlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tencent.map.sdk.utilities.visualization.datamodels.FromToLatLng;
import com.tencent.map.sdk.utilities.visualization.od.ArcLineOverlay;
import com.tencent.map.sdk.utilities.visualization.od.ArcLineOverlayProvider;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.VectorOverlay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ArcLineLayerActivity extends AppCompatActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;
    private VectorOverlay vectorOverlay;
    private ArcLineOverlayProvider options;
    private Switch btnSwitch3D;
    boolean mIsSelected;
    private Switch btnani3D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_line_layer_design);
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
                btnSwitch3D.setVisibility(View.VISIBLE);
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
        mTencentMap.moveCamera(CameraUpdateFactory.zoomTo(5f));
        btnSwitch3D = (Switch) findViewById(R.id.btn_switch_3D);
        bindListener();

    }

    private void bindListener() {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.btn_switch_3D:
                        if (vectorOverlay != null) {
                            if (compoundButton.isChecked()) {
                                options.enable3D(true);
                            } else {
                                options.enable3D(false);
                            }

                            mTencentMap.updateVectorOverlay(vectorOverlay, options);
                        }
                        break;

                    default:
                        break;
                }
            }
        };

        btnSwitch3D.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void addArcLineLayer() {
        mIsSelected = true;
        if (vectorOverlay == null) {
            //  mTencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.9229731, 116.422880668), 8, 0f, 0f)));
            mTencentMap.setMapStyle(3);

            int color = Color.argb(255, 0, 255, 170);
            BufferedReader br = null;
            final ArrayList<FromToLatLng> nodes = new ArrayList<FromToLatLng>();
            try {
                br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("arc.dat")));
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] lines = line.split(" ");
                    if (lines.length == 5) {
                        double arc = Double.parseDouble(lines[4]);
                        LatLng startLatLng = new LatLng((Double.parseDouble(lines[0])), (Double.parseDouble(lines[1])));
                        LatLng endLatLng = new LatLng((Double.parseDouble(lines[2])), (Double.parseDouble(lines[3])));
                        nodes.add(new FromToLatLng(startLatLng, endLatLng));
                        //nodes.add(new FromToLatLng(startLatLng, endLatLng, arc));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int[] colors = {
                    Color.argb(255, 0, 0xd4, 0xff),
                    Color.argb((int) (255 * 0.3), 0, 0xd4, 0xff),
                    Color.argb(255, 0, 0xd4, 0xff)
            };
            options = new ArcLineOverlayProvider().data(nodes).opacity(2f).width(4).gradient(colors).setAnimateDuration(3000).setHighlightDuration(200).animateColor(Color.argb(255, 0xff, 0xff, 0xff));
            vectorOverlay = mTencentMap.addVectorOverlay(options);
            mTencentMap.setPoisEnabled(false);

        }
    }
}
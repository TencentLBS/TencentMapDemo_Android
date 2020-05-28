package com.tencent.map.vector.demo.heatoverlay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.map.sdk.utilities.heatmap.Gradient;
import com.tencent.map.sdk.utilities.heatmap.HeatMapTileProvider;
import com.tencent.map.sdk.utilities.heatmap.WeightedLatLng;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlayOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DrawHeatOverlayActivity extends SupportMapFragmentActivity implements HeatMapTileProvider.OnHeatMapReadyListener{

    private TileOverlay mHeatmapTileOverlay;
    private HeatMapTileProvider mProvider;
    //热力图半径
    private static final int ALT_HEATMAP_RADIUS = HeatMapTileProvider.DEFAULT_RADIUS;
    //热力图透明度
    private static final double ALT_HEATMAP_OPACITY = HeatMapTileProvider.DEFAULT_OPACITY;
    //热力图渐变方案
    public static final Gradient ALT_HEATMAP_GRADIENT = HeatMapTileProvider.DEFAULT_GRADIENT;

    private static final int[] CUSTOM_GRADIENT_COLORS = {
            Color.argb(0,0, 225, 225),
            Color.rgb(102, 125, 200),
            Color.rgb(255, 0, 0)
    };

    private static final float[] CUSTOM_GRADIENT_START_POINTS = {
            0.0f, 0.2f, 1f
    };

    public static final Gradient CUSTOM_HEATMAP_GRADIENT = new Gradient(CUSTOM_GRADIENT_COLORS,
            CUSTOM_GRADIENT_START_POINTS);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMap();
        Button first = findViewById(R.id.btn_bottom);
        first.setVisibility(View.VISIBLE);
        first.setText("自定义热力图");
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProvider.setHeatTileGenerator(new HeatMapTileProvider.HeatTileGenerator() {
                    @Override
                    public double[] generateKernel(int radius) {
                        double[] kernel = new double[radius * 2 + 1];
                        for (int i = -radius; i <= radius; i++) {
                            kernel[i + radius] = Math.exp(-i * i / (2 * (radius / 2f) * (radius / 2f)));
                        }
                        return kernel;
                    }

                    @Override
                    public int[] generateColorMap(double opacity) {
                        return CUSTOM_HEATMAP_GRADIENT.generateColorMap(opacity);
                    }
                });
                mHeatmapTileOverlay.clearTileCache();
                mHeatmapTileOverlay.reload();
            }
        });

    }
    private void setUpMap() {
        tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new com.tencent.tencentmap.mapsdk.maps.model.LatLng(39.917128, 116.399266),
                7.946870f));
        initHeatMap();
    }
    private void initHeatMap() {
        BufferedReader br = null;
        try {
            ArrayList<WeightedLatLng> nodes = new ArrayList<>();
            br = new BufferedReader(new InputStreamReader(
                    getResources().getAssets().open("data2k")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split("\t");
                if (lines.length == 3) {
                    double value = Double.parseDouble(lines[2]);
                    LatLng latLng = new LatLng((Double.parseDouble(lines[1])),
                            (Double.parseDouble(lines[0])));
                    nodes.add(new WeightedLatLng(latLng, value));
                }
            }

            mProvider = new HeatMapTileProvider.Builder()
                    .weightedData(nodes)
                    .gradient(ALT_HEATMAP_GRADIENT)
                    .opacity(ALT_HEATMAP_OPACITY)
                    .radius(ALT_HEATMAP_RADIUS)
                    .readyListener(this)
                    .build(tencentMap);
            mHeatmapTileOverlay = tencentMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
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

    @Override
    public void onHeatMapReady() {
        mHeatmapTileOverlay.clearTileCache();
        mHeatmapTileOverlay.reload();
    }
}

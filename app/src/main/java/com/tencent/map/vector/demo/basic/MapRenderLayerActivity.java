package com.tencent.map.vector.demo.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapRenderLayer;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;

public class MapRenderLayerActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private MapRenderLayer mapRenderLayer;
    private TencentMap tencentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_render_layer);
        textureView = findViewById(R.id.textureView);
        textureView.setOpaque(false);
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        TencentMapOptions mapOptions = new TencentMapOptions();
        // mapOptions.setMapKey("");
        mapOptions.setExtSurface(surface);
        mapOptions.setExtSurfaceDimension(width, height);
        mapRenderLayer = new MapRenderLayer(this, mapOptions);
        // mapRenderLayer.onResume();
        textureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mapRenderLayer.onTouchEvent(event);
                mapRenderLayer.dispatchTouchEvent(event);
                return true;
            }
        });
        tencentMap = mapRenderLayer.getMap();
        tencentMap.setMapStyle(3);
        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        tencentMap.moveCamera(CameraUpdateFactory.zoomTo(8));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (mapRenderLayer != null) {
            mapRenderLayer.onSurfaceChanged(surface, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mapRenderLayer != null) {
            mapRenderLayer.onDestroy();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (mapRenderLayer != null) {
            mapRenderLayer.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapRenderLayer != null) {
            mapRenderLayer.onResume();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mapRenderLayer != null) {
            mapRenderLayer.onRestart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapRenderLayer != null) {
            mapRenderLayer.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapRenderLayer != null) {
            mapRenderLayer.onDestroy();
        }
    }
}
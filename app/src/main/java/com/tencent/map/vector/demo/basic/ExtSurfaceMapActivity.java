package com.tencent.map.vector.demo.basic;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.MapViewType;

/**
 * 扩展屏地图
 */
public class ExtSurfaceMapActivity extends AppCompatActivity {

    private MapView mMapView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extsurface);
        final TextureView textureView = findViewById(R.id.mapview);

        //TODO 1. 设置view未透明
        textureView.setOpaque(false);

        //TODO 2. 设置Surface生命周期监听
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

                if (mMapView == null) {
                    //构造同层渲染地图
                    TencentMapOptions mapOptions = new TencentMapOptions();
                    mapOptions.setExtSurface(surface);
                    mapOptions.setExtSurfaceDimension(textureView.getWidth(), textureView.getHeight());

                    // 设置地图为扩展渲染
                    mapOptions.setMapViewType(MapViewType.RenderLayer);
                    mMapView = new MapView(getApplicationContext(), mapOptions);

                    TencentMap mMap = mMapView.getMap();

                    // 关闭建筑物
                    mMap.showBuilding(false);

                    // 关闭室内
                    mMap.setIndoorEnabled(false);

                    // 关闭POI
                    mMap.setPoisEnabled(false);

                    // 设置透明的个性化地图样式（需要用户自行配置）
                    mMap.setMapStyle(3);

                    // 关闭比例尺
                    mMap.getUiSettings().setScaleViewEnabled(false);
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(6));
                } else {
                    mMapView.onSurfaceChanged(surface, width, height);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                if (mMapView != null) {
                    mMapView.onSurfaceChanged(surface, width, height);
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (mMapView != null) {
                    mMapView.onDestroy();
                }
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                if (mMapView != null) {
                    mMapView.onSurfaceChanged(surface, textureView.getWidth(), textureView.getHeight());
                }
            }
        });

        //TODO 3.分发touch事件到地图中
        textureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mMapView != null) {
                    //4.4.2 之后，请求使用dispatchTouchEvent
                    mMapView.onTouchEvent(event);
                }
                return true;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mMapView != null) {
            mMapView.onRestart();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMapView != null) {
            mMapView.onStart();
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
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
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
}

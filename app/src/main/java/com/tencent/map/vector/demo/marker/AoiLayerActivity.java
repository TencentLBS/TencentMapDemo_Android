package com.tencent.map.vector.demo.marker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.AoiLayer;
import com.tencent.tencentmap.mapsdk.maps.model.AoiLayerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.MapPoi;
import com.tencent.tencentmap.mapsdk.maps.model.SubPoi;

public class AoiLayerActivity extends AppCompatActivity implements AoiLayer.OnAoiLayerLoadListener {
    private MapView mMapView;
    private RelativeLayout mRelativeLayout;
    private TencentMap mTencentMap;
    private boolean mIsSelected;
    private AoiLayer mAoiLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aoi_layer);
        mRelativeLayout = findViewById(R.id.relativelayout);
        TencentMapOptions options = new TencentMapOptions();
        mMapView = new MapView(this, options);
        mRelativeLayout.addView(mMapView);
        mTencentMap = mMapView.getMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
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
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onAoiLayerLoaded(boolean b, AoiLayer aoiLayer) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marker_groundoverlay, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add_groundoverlay).setVisible(!mIsSelected);
        menu.findItem(R.id.menu_remove_groundoverlay).setVisible(mIsSelected);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_groundoverlay:
                addAOI();
                break;
            case R.id.menu_remove_groundoverlay:
                removeAOI();
                break;
        }

        supportInvalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加AOI面
     */
    private void addAOI() {
        mIsSelected = true;
        //通过检索能力获取Poi的ID ,以北京大学为例
        String poiId = "11107399966856067949";
        //AOI面配置项
        AoiLayerOptions options = new AoiLayerOptions();
        //设置显示级别 有效范围是(0-20]
        options.setDisplayLevel(10, 20);

        //添加一个Aoi面
        mAoiLayer = mTencentMap.addAoiLayer(poiId, options, new AoiLayer.OnAoiLayerLoadListener() {

            @Override
            public void onAoiLayerLoaded(boolean b, AoiLayer aoiLayer) {
                if (b) {
                    Toast.makeText(AoiLayerActivity.this, "添加[" + aoiLayer.name() + "]成功！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AoiLayerActivity.this, "添加[" + aoiLayer.getId() + "]失败！", Toast.LENGTH_LONG).show();
                }
            }
        });

        //设置AOi面上的子POI点的点击回调
        mTencentMap.setOnMapPoiClickListener(new TencentMap.OnMapPoiClickListener() {

            @Override
            public void onClicked(MapPoi mapPoi) {
                if (mapPoi instanceof SubPoi) {
                    Toast.makeText(AoiLayerActivity.this, "SubPoi[" + mapPoi.getName() + "]被点击！", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * 移除AOI面
     */
    private void removeAOI() {
        mIsSelected = false;
        if (mAoiLayer != null) {
            mAoiLayer.remove();
        }
    }
}



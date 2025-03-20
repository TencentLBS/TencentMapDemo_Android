package com.tencent.map.vector.demo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class SnapshotOffScreenActivity extends Activity {
    private MapView mMapView;
    private TencentMap mTencentMap;

    private ImageView mIvShot;
    private RelativeLayout mRlContainer;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_off_screen);
        mRlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mIvShot = (ImageView) findViewById(R.id.iv_shot);
    }

    public void shotMap(View view) {
        if (mMapView == null) {

            Log.d("SnapshotTest", "new map view: " + System.currentTimeMillis());
            mMapView = new MapView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mMapView.onStart();
            mMapView.onResume();
            mRlContainer.addView(mMapView, params);
            mMapView.setX(-3000);

            mTencentMap = mMapView.getMap();

            mTencentMap.addOnMapLoadedCallback(new TencentMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    Log.d("SnapshotTest", "onMapLoaded: " + System.currentTimeMillis());
                    mTencentMap.snapshot(new TencentMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap snapshot) {
                            Log.d("SnapshotTest", "onSnapshotReady: " + System.currentTimeMillis());
                            mIvShot.setImageBitmap(snapshot);

                            mRlContainer.removeView(mMapView);
                            mMapView.onPause();
                            mMapView.onStop();
                            mMapView.onDestroy();
                            mMapView = null;
                        }
                    });
                }
            });
            int id = mRadioGroup.getCheckedRadioButtonId();
            switch (id) {
                case R.id.rb_beijing:
                    mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.908823,116.397174), 13));
                    break;
                case R.id.rb_shanghai:
                    mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.231573,121.470273), 13));
                    break;
                case R.id.rb_shenzhen:
                    mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.546851,114.049327), 13));
                    break;
                case R.id.rb_guangzhou:
                    mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.127414,113.261975), 13));
                    break;
            }
        }
    }
}

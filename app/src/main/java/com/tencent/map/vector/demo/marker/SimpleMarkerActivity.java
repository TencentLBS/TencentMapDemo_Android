package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.AbsMapActivity;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.AlphaAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.Animation;
import com.tencent.tencentmap.mapsdk.maps.model.AnimationListener;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class SimpleMarkerActivity extends AbsMapActivity {

    private TencentMap mTencentMap;
    private Marker mSimpleMarker;
    private Marker mCustomMarker;

    private boolean mMarkerAdded;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap pTencentMap) {
        super.onCreate(savedInstanceState, pTencentMap);
        mTencentMap = pTencentMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_marker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add).setVisible(!mMarkerAdded);
        menu.findItem(R.id.menu_delete).setVisible(mMarkerAdded);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_marker_simple:
                if (mSimpleMarker == null) {
                    LatLng position = new LatLng(40.011313,116.391907);
                    mSimpleMarker = mTencentMap.addMarker(new MarkerOptions(position));
                    mTencentMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMarkerAdded = true;
                }
                break;
            case R.id.menu_add_marker_custom:

                if (mCustomMarker == null) {
                    LatLng position = new LatLng(40.011313,116.391907);
                    BitmapDescriptor custom = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                    mCustomMarker = mTencentMap.addMarker(new MarkerOptions(position)
                            .icon(custom)
                            .alpha(0.7f)
                            .flat(true)
                            .clockwise(false)
                            .rotation(30)
                    );

                    Animation animation = new AlphaAnimation(0.7f, 0f);
                    animation.setDuration(2000);
                    animation.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart() {

                        }

                        @Override
                        public void onAnimationEnd() {
                            mCustomMarker.setAlpha(1);
                        }
                    });
                    mCustomMarker.setAnimation(animation);
                    mCustomMarker.startAnimation();
                    mTencentMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMarkerAdded = true;
                }
                break;
            case R.id.menu_delete:
                if (mSimpleMarker != null) {
                    mSimpleMarker.remove();
                    mSimpleMarker = null;
                }

                if (mCustomMarker != null) {
                    mCustomMarker.remove();
                    mCustomMarker = null;
                }

                mMarkerAdded = false;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

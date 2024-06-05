package com.tencent.map.vector.demo.utils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class SnapshotActivity extends SupportMapFragmentActivity {
    public MapView mapview = null;

    public TencentMap tencentMap = null;

    private ImageView imgView = null;

    public TencentMap.SnapshotReadyCallback snapshotReadyCallback = new TencentMap.SnapshotReadyCallback() {
        @Override
        public void onSnapshotReady(Bitmap snapshot) {
            imgView.setImageBitmap(snapshot);
            // Log.d("线程", "run: "+Thread.currentThread().getName());
        }
    };

    Handler handScreen = new Handler();

    final CameraPosition camerPosition_GUGONG = new CameraPosition.Builder()
            .target(new LatLng(39.91822, 116.397165)).zoom(14.5f).bearing(200).tilt(50).build();

    final CameraPosition camerPosition_YINKE = new CameraPosition.Builder()
            .target(new LatLng(31.226407, 121.48298)).zoom(17.5f).bearing(0).tilt(25).build();

    private Runnable runScreenShot = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            tencentMap.snapshot(snapshotReadyCallback, Bitmap.Config.ARGB_8888);
        }

    };

    // TencentMap.OnTrafficUpdateListener trafficUpdateListener = new
    // TencentMap.OnTrafficUpdateListener() {
    //
    // @Override
    // public void onTrafficUpdate(String city) {
    // Log.e("zxy", "onTrafficUpdate(): " + city);
    // // TODO Auto-generated method stub\
    // handScreen.postDelayed(runScreenShot, 2000);
    //
    // }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screenshot);
        mapview = (MapView) findViewById(R.id.map);

        tencentMap = mapview.getMap();

        // tencentMap.setOnTrafficUpdate(trafficUpdateListener);
        tencentMap.setTrafficEnabled(true);
        tencentMap.setIndoorEnabled(true);
        handScreen.postDelayed(runScreenShot, 2000);
        //runOnUiThread(runScreenShot);
        imgView = (ImageView) this.findViewById(R.id.imgview);
        imgView.setScaleType(ImageView.ScaleType.CENTER);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapview.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 0, "截屏坐标1");
        menu.add(0, 2, 0, "截屏坐标2");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:
                setCoord1();
                handScreen.postDelayed(runScreenShot, 2000);
                break;
            case 2:
                setCoor2();
                handScreen.postDelayed(runScreenShot, 2000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCoord1() {
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(camerPosition_GUGONG);
        tencentMap.moveCamera(update);
    }

    private void setCoor2() {
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(camerPosition_YINKE);
        tencentMap.moveCamera(update);
    }
}
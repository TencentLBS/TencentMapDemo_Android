package com.tencent.map.vector.demo.polygon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerCollisionItem;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Tile;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.TileOverlayOptions;
import com.tencent.tencentmap.mapsdk.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TileOverlayActivity extends AppCompatActivity {
    private MapView mapView;
    private TencentMap tencentMap;
    private boolean mMarkerAdded;
    private TileOverlay mTileOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_overlay);
        initView();
    }

    private void initView() {
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(39.917505, 116.397657),
                        15,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.markercol, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_open_collisionsmap).setVisible(!mMarkerAdded);
        menu.findItem(R.id.menu_close_collisionsmap).setVisible(mMarkerAdded);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_collisionsmap:
                mMarkerAdded = true;
                if (mTileOverlay == null) {
                    mTileOverlay = tencentMap.addTileOverlay(
                            new TileOverlayOptions().
                                    tileProvider(new LocalTileProvider()));
                }

                break;
            case R.id.menu_close_collisionsmap:
                mMarkerAdded = false;
                if (mTileOverlay != null) {
                    mTileOverlay.clearTileCache();
                    mTileOverlay.remove();
                    mTileOverlay = null;
                }
                break;
        }

        supportInvalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    class LocalTileProvider implements TileProvider {

        @Override
        public Tile getTile(int x, int y, int zoom) {
            LatLng latLng = new LatLng(39.917505, 116.397657);
            int iZ = 16;
            double n = Math.pow(2, iZ);
            int iX = (int) (((latLng.getLongitude() + 180) / 360) * n);
            int iY = (int) ((1 - (Math.log(Math.tan(Math.toRadians(latLng.getLatitude())) +
                    (1 / Math.cos(Math.toRadians(latLng.getLatitude())))) / Math.PI)) / 2 * n);
            if (iX == x && iY == y && iZ == zoom) {
                Log.e("tile", "zoom:" + zoom + " x:" + x + " y:" + y);
                return new Tile(256, 256, tileData());
            }
            return TileProvider.NO_TILE;
        }

        byte[] tileData() {
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                is = getApplicationContext().getAssets().open("gugong.jpg");
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                int count = 0;
                while ((count = is.read(byteBuffer)) != -1) {
                    baos.write(byteBuffer, 0, count);
                }
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
}
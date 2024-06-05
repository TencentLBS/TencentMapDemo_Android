package com.tencent.map.vector.demo.smooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.vector.utils.animation.MarkerTranslateAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.tencent.tencentmap.mapsdk.vector.utils.animation.MarkerTranslateAnimator.MarkerTranslateStatusListener.AnimationStatus.AnimationComplete;

public class SmoothMoveActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mapView;
    private final String mLine = "39.98409,116.30804,39.98409,116.3081,39.98409,116.3081,39.98397,116.30809,39.9823,116.30809,39.9811,116.30817,39.9811,116.30817,39.97918,116.308266,39.97918,116.308266,39.9791,116.30827,39.9791,116.30827,39.979008,116.3083,39.978756,116.3084,39.978386,116.3086,39.977867,116.30884,39.977547,116.308914,39.976845,116.308914,39.975826,116.308945,39.975826,116.308945,39.975666,116.30901,39.975716,116.310486,39.975716,116.310486,39.975754,116.31129,39.975754,116.31129,39.975784,116.31241,39.975822,116.31327,39.97581,116.31352,39.97588,116.31591,39.97588,116.31591,39.97591,116.31735,39.97591,116.31735,39.97593,116.31815,39.975967,116.31879,39.975986,116.32034,39.976055,116.32211,39.976086,116.323395,39.976105,116.32514,39.976173,116.32631,39.976254,116.32811,39.976265,116.3288,39.976345,116.33123,39.976357,116.33198,39.976418,116.33346,39.976418,116.33346,39.97653,116.333755,39.97653,116.333755,39.978157,116.333664,39.978157,116.333664,39.978195,116.33509,39.978195,116.33509,39.978226,116.33625,39.978226,116.33625,39.97823,116.33656,39.97823,116.33656,39.978256,116.33791,39.978256,116.33791,39.978016,116.33789,39.977047,116.33791,39.977047,116.33791,39.97706,116.33768,39.97706,116.33768,39.976967,116.33706,39.976967,116.33697";
    private TencentMap map;
    private Polyline mPolyline;
    private Marker mCarMarker;
    private LatLng[] mCarLatLngArray;
    private MarkerTranslateAnimator mAnimator;
    private Polyline polyline;
    private ArrayList<Float> skews;
    private ArrayList<Float> roates;
    private String parentPath;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smooth_move);
        mapView = findViewById(R.id.map);
        map = mapView.getMap();
        //解析路线
        init();
        Button btLin = findViewById(R.id.button_set);
        Button btStart = findViewById(R.id.satrt);
        Button btStop = findViewById(R.id.stop);
        btLin.setOnClickListener(this);
        btStart.setOnClickListener(this);
        btStop.setOnClickListener(this);
    }

    private void init() {
        String[] linePointsStr = mLine.split(",");
        mCarLatLngArray = new LatLng[linePointsStr.length / 4];
        skews = new ArrayList<>();
        roates = new ArrayList<>();

        for (int i = 0; i < mCarLatLngArray.length; i++) {
            double latitude = Double.parseDouble(linePointsStr[i * 4]);
            double longitude = Double.parseDouble(linePointsStr[i * 4 + 1]);
            //  float skew = Float.parseFloat(linePointsStr[i * 4 + 2]);
            // float roate = Float.parseFloat(linePointsStr[i * 4 + 3]);
            mCarLatLngArray[i] = new LatLng(latitude, longitude);
            // skews.add(skew);
            //  roates.add(roate);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
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
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_set:
                if (polyline == null && mCarMarker == null) {
                    //添加小车路线
                    polyline = map.addPolyline(new PolylineOptions().add(mCarLatLngArray).color(R.color.colorPrimaryDark));
                    LatLng carLatLng = mCarLatLngArray[0];
                    mCarMarker = map.addMarker(
                            new MarkerOptions(carLatLng)
                                    .anchor(0.5f, 0.5f)
                                    .title("即将到达终点")
                                    .icon(BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.taxi)))
                                    .flat(true)
                                    .clockwise(false));
                    //调整最佳视界
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().include(Arrays.asList(mCarLatLngArray)).build(), 50));
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (polyline != null) {
                                snapshot();
                            }

                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask, 800);
                }
                break;
            case R.id.satrt:
                if (mAnimator == null) {
                    //创建移动动画
                    mAnimator = new MarkerTranslateAnimator(mCarMarker, 100 * 1000, mCarLatLngArray, true, new MarkerTranslateAnimator.MarkerTranslateStatusListener() {
                        private CameraUpdate cameraSigma;

                        @Override
                        public void onInterpolatePoint(LatLng latLng, int i, AnimationStatus animationStatus) {
                            //路线擦出
                            polyline.setEraseable(true);
                            polyline.eraseTo(i, latLng);

                      /*  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBoundsWithMapCenter(LatLngBounds.builder().include(Arrays.asList(mCarLatLngArray)).build(), latLng, 10);
                        map.animateCamera(cameraUpdate);*/
                            if (mCarMarker.getRotation() < 90 && mCarMarker.getRotation() > 0) {
                                cameraSigma =
                                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                                latLng,
                                                18,
                                                60,
                                                mCarMarker.getRotation() + 180));
                            } else if (mCarMarker.getRotation() >= 90 && mCarMarker.getRotation() <= 180) {
                                cameraSigma =
                                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                                latLng,
                                                18,
                                                60,
                                                -mCarMarker.getRotation()));
                            } else {
                                cameraSigma =
                                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                                latLng,
                                                18,
                                                60,
                                                -mCarMarker.getRotation()));
                            }

                            //移动地图
                            map.moveCamera(cameraSigma);
                            map.enableMultipleInfowindow(true);
                            if (i == 41) {
                                mCarMarker.startAnimation();
                                mCarMarker.showInfoWindow();
                            }
                            if (animationStatus == AnimationComplete) {
                                mCarMarker.setTitle("到达目的地");
                            }
                        }
                    });


                    //开启动画
                    mAnimator.startAnimation();
                }
                break;
            case R.id.stop:
                if (mAnimator != null && polyline != null && mCarMarker != null) {
                    mAnimator.endAnimation();
                    //mAnimator.cancelAnimation();
                    polyline.remove();
                    polyline = null;
                    mCarMarker.remove();
                    mCarMarker = null;
                    mAnimator = null;
                }


                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void snapshot() {
        imageView = findViewById(R.id.iv);

        map.snapshot(new TencentMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
            }
        }, Bitmap.Config.ARGB_8888);
    }

    //设置图片
    private Bitmap getBitMap(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 100;
        int newHeight = 150;
        float widthScale = ((float) newWidth) / width;
        float heightScale = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }
}
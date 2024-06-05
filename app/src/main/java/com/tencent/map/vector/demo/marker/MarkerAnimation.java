package com.tencent.map.vector.demo.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.AbsMapActivity;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.AnimationListener;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.IAlphaAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.IAnimationSet;
import com.tencent.tencentmap.mapsdk.maps.model.IRotateAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.IScaleAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.ITranslateAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MarkerAnimation extends AbsMapActivity {

    private int rotate = 0;
    private int scale = 1;
    private int alpha = 2;
    private int translate = 3;
    private int set = 4;
    private boolean mHasAdded;
    private Marker marker;
    private IRotateAnimation rotateAnimation;
    private IScaleAnimation scaleAnimation;
    private IAlphaAnimation alphaAnimation;
    private ITranslateAnimation translateAnimation;
    private TencentMap tencentMap;
    private IAnimationSet animationSet;
    private LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap mTencentMap) {
        super.onCreate(savedInstanceState, mTencentMap);
        tencentMap = mTencentMap;
        tencentMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marker_animation, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add_marker).setVisible(!mHasAdded);
        menu.findItem(R.id.menu_delete_marker).setVisible(mHasAdded);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_marker_rotate:
                tencentMap.clear();
                marker = tencentMap.addMarker(setMarkerAnimation(rotate));
                mHasAdded = true;
                marker.setAnimation(rotateAnimation);
                marker.startAnimation();
                break;

            case R.id.menu_add_marker_scale:
                tencentMap.clear();
                marker = tencentMap.addMarker(setMarkerAnimation(scale));
                mHasAdded = true;
                marker.setAnimation(scaleAnimation);
                marker.startAnimation();
                scaleAnimation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        marker.startAnimation();
                    }
                });


                break;
            case R.id.menu_add_marker_alpha:
                tencentMap.clear();
                marker = tencentMap.addMarker(setMarkerAnimation(alpha));
                mHasAdded = true;
                marker.setAnimation(alphaAnimation);
                marker.startAnimation();
                break;
            case R.id.menu_add_marker_translate:
                tencentMap.clear();
                marker = tencentMap.addMarker(setMarkerAnimation(translate));
                mHasAdded = true;
                Point screenLocation = tencentMap.getProjection().toScreenLocation(latLng);

                marker.setFixingPoint(screenLocation.x, screenLocation.y);

                marker.setAnimation(translateAnimation);
                marker.startAnimation();

                translateAnimation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        marker.setFixingPointEnable(false);

                    }
                });
                break;
            case R.id.menu_add_marker_set:
                tencentMap.clear();
                marker = tencentMap.addMarker(setMarkerAnimation(set));
                mHasAdded = true;
                marker.setAnimation(animationSet);
                marker.startAnimation();
                break;

            case R.id.menu_delete_marker:
                if (marker != null) {
                    marker.remove();
                    marker = null;
                    mHasAdded = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private MarkerOptions setMarkerAnimation(int type) {
        latLng = new LatLng(39.984108, 116.307557);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.marker));
        MarkerOptions position = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        switch (type) {
            case 0:
                //旋转动画
                rotateAnimation = tencentMap.getMapContext().createRotateAnimation(0, 360, 0, 0, 0);
                rotateAnimation.setDuration(2000);
                break;
            case 1:
                //缩放动画
                scaleAnimation = tencentMap.getMapContext().createScaleAnimation(0.0f, 1f, 0.0f, 1f);
                scaleAnimation.setDuration(2000);
                scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

            case 2:
                //渐变
                alphaAnimation = tencentMap.getMapContext().createAlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(2000);
                alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

                break;
            case 3:
                //平移
                Point point = tencentMap.getProjection().toScreenLocation(latLng);
                point.y -= dip2px(this, 250);
                LatLng target = tencentMap.getProjection().fromScreenLocation(point);
                translateAnimation = tencentMap.getMapContext().createTranslateAnimation(target);
                translateAnimation.setInterpolator(new Interpolator() {
                    @Override
                    public float getInterpolation(float v) {
                        if (v <= 0.5f) {
                            return (float) (0.5f - 2 * (0.5 - v) * (0.5 - v));
                        } else {
                            return (float) (0.5f - Math.sqrt((v - 0.5f) * (1.5f - v)));
                        }
                    }
                });
                translateAnimation.setDuration(2000);
                break;
            case 4:
                //组合动画
                animationSet = tencentMap.getMapContext().createAnimationSet(true);
                if (rotateAnimation != null || scaleAnimation != null || alphaAnimation != null) {
                    animationSet.addAnimation(rotateAnimation);
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.addAnimation(alphaAnimation);
                }

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return position;
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 1.5f);
    }

    private Bitmap getBitMap(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 100;
        int newHeight = 100;
        float widthScale = ((float) newWidth) / width;
        float heightScale = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

}

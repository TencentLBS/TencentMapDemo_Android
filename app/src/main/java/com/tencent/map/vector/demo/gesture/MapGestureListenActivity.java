package com.tencent.map.vector.demo.gesture;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.TencentMapGestureListener;

public class MapGestureListenActivity extends SupportMapFragmentActivity implements TencentMapGestureListener {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = findViewById(R.id.tv_info);
        textView.setVisibility(View.VISIBLE);
        tencentMap.setTencentMapGestureListener(this);
    }

    @Override
    public boolean onDoubleTap(float v, float v1) {
        textView.setText("单指双击");
        return true;
    }

    @Override
    public boolean onSingleTap(float v, float v1) {
        textView.setText("单指单击");
        return true;
    }

    @Override
    public boolean onFling(float v, float v1) {
        textView.setText("单指惯性滑动");
        return true;
    }

    @Override
    public boolean onScroll(float v, float v1) {
        textView.setText("单指滑动");
        return true;
    }

    @Override
    public boolean onLongPress(float v, float v1) {
        textView.setText("长按");
        return true;
    }

    @Override
    public boolean onDown(float v, float v1) {
        textView.setText("单指按下");
        return true;
    }

    @Override
    public boolean onUp(float v, float v1) {
        textView.setText("单指抬起");
        return true;
    }

    @Override
    public boolean onTwoFingerMoveAgainst(TwoFingerMoveAgainstStatus twoFingerMoveAgainstStatus, CameraPosition cameraPosition) {
        textView.setText("双指捏合");
        return false;
    }

    @Override
    public void onMapStable() {
        textView.setText("地图稳定");
        return;
    }

    @Override
    public void onMapStableBy(CameraPosition.Trigger trigger) {
        textView.setText("地图稳定 by " + trigger.name());
        return;
    }
}

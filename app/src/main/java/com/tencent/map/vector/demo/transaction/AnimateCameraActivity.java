package com.tencent.map.vector.demo.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class AnimateCameraActivity extends SupportMapFragmentActivity implements View.OnClickListener {

    private Button btn;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn = findViewById(R.id.btn_bottom);
        btn.setVisibility(View.VISIBLE);
        btn.setText("以动画的方式改变地图视图");
        btn.setOnClickListener(this);

        flag = true;
    }

    /**
     * 改变地图视图
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(flag){
            CameraUpdate cameraSigma =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(39.977290,116.337000), //新的中心点坐标
                            19,  //新的缩放级别
                            40f, //俯仰角 0~45° (垂直地图时为0)
                            45f)); //偏航角 0~360° (正北方为0)
            //移动地图
            tencentMap.animateCamera(cameraSigma);
            flag = !flag;
        }else {
            CameraUpdate cameraSigma =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(39.877290,116.437000), //新的中心点坐标
                            18,  //新的缩放级别
                            0f, //俯仰角 0~45° (垂直地图时为0)
                            0f)); //偏航角 0~360° (正北方为0)
            //移动地图
            tencentMap.animateCamera(cameraSigma);
            flag = !flag;
        }

    }

}

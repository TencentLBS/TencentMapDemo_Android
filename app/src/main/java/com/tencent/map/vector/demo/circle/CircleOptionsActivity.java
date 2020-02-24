package com.tencent.map.vector.demo.circle;

import android.os.Bundle;

import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class CircleOptionsActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Circle circle = tencentMap.addCircle(circleOptions);
        Circle newCircle = tencentMap.addCircle(newCircleOptions);
        newCircle.setCenter(new LatLng(39.98408, 116.30701)); //重新设置圆心
        newCircle.setOptions(newCircleOptions); //重新设置圆形属性
    }

    /**
     * 圆形选项
     */
    private CircleOptions circleOptions = new CircleOptions() //圆形选项
            .center(new LatLng(39.9849, 116.3977))  //圆心位置
            .clickable(true) //支持点击
            .fillColor((128 << 24) + (100 << 16) + (50 << 8) + 120) //设置填充颜色RGBA
            .radius(110d) //设置半径
            .visible(true) //设置是否可见
            .zIndex(1) //设置堆叠顺序，越大优先级越高
            .strokeWidth(1) //描边宽度
            .strokeColor((128 << 24) + (128 << 16) + (128 << 8) + 128); //描边颜色


    private CircleOptions newCircleOptions = new CircleOptions().center(new LatLng(39.121, 116.454)).radius(90d).fillColor(0xff00ff00);
}

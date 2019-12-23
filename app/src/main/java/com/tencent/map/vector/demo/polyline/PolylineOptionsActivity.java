package com.tencent.map.vector.demo.polyline;

import android.os.Bundle;

import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class PolylineOptionsActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Polyline polyline = tencentMap.addPolyline(polylineOptions);
        Polyline newPolyline = tencentMap.addPolyline(newPolylineOptions);
        polyline.pattern(patternLine(patterns)); //设置虚线样式
    }

    private int[] patterns = {10, 15, 20};
    private List<Integer> patternLine(int [] patterns){
        List<Integer> patternLine = new ArrayList<>();
        if (patterns == null)
            return patternLine;
        for (int i = 0; i < patterns.length; i++)
            patternLine.add(patterns[i]);
        return patternLine;
    }


    private List<LatLng> transferArrayToList(LatLng[] latLngs){ //坐标数组与List转换
        List<LatLng> list = new ArrayList<>();
        if (latLngs == null)
            return list;
        for (int i = 0; i < latLngs.length; i++)
            list.add(latLngs[i]);

        return list;

    }

    private LatLng[] latLngs = {
            new LatLng(39.984864, 116.305756),
            new LatLng(39.983618, 116.305848),
            new LatLng(39.982347, 116.305966),
            new LatLng(39.982412, 116.308111),
            new LatLng(39.984122, 116.308224),
            new LatLng(39.984955, 116.308099),
            new LatLng(39.984864, 116.305756)
    };

    private LatLng[] newLatLngs = {
            new LatLng(39.984864, 116.305756),
            new LatLng(39.983618, 116.305848),
            new LatLng(39.982347, 116.305966),
            new LatLng(39.982412, 116.308111),
            new LatLng(39.984122, 116.308224),
            new LatLng(39.984955, 116.308099),
            new LatLng(39.984864, 116.305756)
    };
    private PolylineOptions polylineOptions = new PolylineOptions().
            addAll(transferArrayToList(latLngs)). //添加顶点
            alpha(0.5f). //设置透明度
            arrow(true). //导航用 接口
            arrowSpacing(110). // 设置方向箭头间距
            arrowTexture(BitmapDescriptorFactory.fromAsset("color_arrow_texture.png")). // 设置箭头纹理
            borderColor(0xaa323456). //设置描边颜色
            borderWidth(1f). //设置描边宽度
            clickable(true). //可点击
            color(PolylineOptions.Colors.LIGHT_BLUE). //线的颜色
            colorTexture(BitmapDescriptorFactory.fromAsset("color_texture.png")). //颜色纹理
            lineCap(true). //路线是否显示半圆端点
            lineType(PolylineOptions.LineType.LINE_TYPE_MULTICOLORLINE). //设置线的类型
            visible(true). //设置折线可见性
            width(10f). //设置线的宽度
            zIndex(0); //堆叠顺序，越大优先级越高


    private PolylineOptions newPolylineOptions = new PolylineOptions().add(newLatLngs).
            lineType(PolylineOptions.LineType.LINE_TYPE_DOTTEDLINE).
            visible(true).
            pattern(patternLine(patterns)).
            zIndex(1);
}

package com.tencent.map.vector.demo.polygon;

import android.os.Bundle;

import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class PolylonOptionsActivity extends SupportMapFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Polygon polygon = tencentMap.addPolygon(polygonOptions);
        polygon.setPoints(transferArrayToList(latLngs)); //Options中设置顶点
        Polygon newPolygon = tencentMap.addPolygon(newPolygonOptions);
        newPolygon.setOptions(newPolygonOptions); //重新设置Options
        newPolygon.setClickable(true); //设置可点击
        newPolygon.setFillColor(0xaa324354);
        newPolygon.setPoints(transferArrayToList(newLatLngs)); //设置坐标
        newPolygon.setTag(tencentMap.addMarker(new MarkerOptions().position(new LatLng(39.984866, 116.305759)).title("设置标注").draggable(true)));


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


    private PolygonOptions polygonOptions = new PolygonOptions().
            add(latLngs). // 添加图形坐标点
            fillColor(0x56643313). //填充颜色
            strokeColor(0xfa349012). //描边颜色
            strokeWidth(1). //描边宽度
            visible(true). //是否可见
            zIndex(0). //堆叠顺序，越大优先级越高
            clickable(true); // 是否可点击

    private PolygonOptions newPolygonOptions = new PolygonOptions().
            addAll(transferArrayToList(newLatLngs)); //设置批量添加顶点
}

package com.example.tencentmap.tencentmapdemo.polyline;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.tencentmap.tencentmapdemo.R;
import com.example.tencentmap.tencentmapdemo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DrawLineActivity extends SupportMapFragmentActivity implements RadioGroup.OnCheckedChangeListener{
    private int typeSimple = 0;
    private int typeImage = 1;
    private int typeColor = 2;
    private int typeArrow = 3;

    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioGroup = findViewById(R.id.lay_line);
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(this);
        tencentMap.addPolyline(setLineStyle(typeSimple));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.btn_line_simple:
                tencentMap.clear();
                tencentMap.addPolyline(setLineStyle(typeSimple));
                break;
            case R.id.btn_line_image:
                tencentMap.clear();
                tencentMap.addPolyline(setLineStyle(typeImage));
                break;
            case R.id.btn_line_color:
                tencentMap.clear();
                Polyline polyline = tencentMap.addPolyline(setLineStyle(typeColor));
                int[] color = {0,1,2,3,4};
                int[] index = {0,1,2,3,4,5};
                polyline.setColors(color,index);
                break;
            case R.id.btn_line_arrow:
                tencentMap.clear();
                tencentMap.addPolyline(setLineStyle((typeArrow)));
        }
    }

    private PolylineOptions setLineStyle(int type){
        PolylineOptions polylineOptions = new PolylineOptions().addAll(getLatlons()).lineCap(true);
        switch (type){
            case 0:
                //设置折线颜色、宽度
                polylineOptions.color(0xff00ff00). width(5f);
                break;
            case 1:
                polylineOptions.lineType(PolylineOptions.LineType.LINE_TYPE_IMAGEINARYLINE);
                polylineOptions.width(10);
                List<Integer> list = new ArrayList<>();
                list.add(35);
                list.add(20);
                polylineOptions.pattern(list);
                break;
            case 2:
                //线路颜色值纹理图片里的颜色索引
                polylineOptions.colorType(PolylineOptions.ColorType.LINE_COLOR_TEXTURE).color(PolylineOptions.Colors.GREEN);
                polylineOptions.colorTexture(BitmapDescriptorFactory.fromAsset("color_texture.png"));
                break;
            case 3:
                polylineOptions.arrow(true);
                polylineOptions.arrowSpacing(30);
                polylineOptions.arrowTexture(BitmapDescriptorFactory.fromAsset("color_arrow_texture.png"));

        }
        return polylineOptions;
    }

    private List<LatLng> getLatlons(){
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(39.984864,116.305756));
        latLngs.add(new LatLng(39.983618,116.305848));
        latLngs.add(new LatLng(39.982347,116.305966));
        latLngs.add(new LatLng(39.982412,116.308111));
        latLngs.add(new LatLng(39.984122,116.308224));
        latLngs.add(new LatLng(39.984955,116.308099));
        return latLngs;
    }
}

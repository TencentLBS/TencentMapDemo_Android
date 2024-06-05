package com.tencent.map.vector.demo.polyline;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class LineTextActivity extends SupportMapFragmentActivity {
    private Polyline polyline;
    private static List<LatLng> mPoints = new ArrayList<>();
    private Switch switch_style;
    private Switch switch_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        tencentMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        switch_style.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (polyline == null) {
                    return;
                }

                PolylineOptions.Text text = polyline.getText();
                removeText();
                if (checked) {
                    //设置显示优先级，可选项有HIGH或NORMAL
                    text.setPriority(PolylineOptions.TextPriority.HIGH);
                    //设置字体大小
                    text.setTextSize(10);
                    //设置填充颜色
                    text.setStrokeColor(Color.WHITE);
                    //设置文字颜色
                    text.setTextColor(Color.BLACK);
                } else {
                    //设置显示优先级，可选项有HIGH或NORMAL
                    text.setPriority(PolylineOptions.TextPriority.NORMAL);
                    //设置字体大小
                    text.setTextSize(8);
                    //设置填充颜色
                    text.setStrokeColor(Color.WHITE);
                    //设置文字颜色
                    text.setTextColor(Color.GRAY);

                }

                polyline.setText(text);
            }
        });

        switch_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    polyline = createLineWithText();
                } else {
                    removeText();
                    polyline.remove();
                    polyline = null;
                }
            }
        });
    }


    private void initView() {
        //文字
        switch_style = (Switch) findViewById(R.id.switch_style);
        switch_style.setVisibility(View.VISIBLE);
        switch_off = (Switch) findViewById(R.id.switch_off);
        switch_off.setVisibility(View.VISIBLE);
    }

    public Polyline addLine() {
        Polyline polyline = tencentMap.addPolyline(new PolylineOptions().addAll(mPoints).color(0x22ff0000));

        return polyline;
    }


    //生成坐标点路径
    private List<LatLng> generateLatLngs() {
        if (mPoints.size() != 0)
            mPoints = new ArrayList<>();
        //苏州街
        mPoints.add(new LatLng(39.982382, 116.305883));
        //北四环西路辅路
        mPoints.add(new LatLng(39.984914, 116.305690));
        //彩和坊路
        mPoints.add(new LatLng(39.985045, 116.308136));
        mPoints.add(new LatLng(39.983570, 116.308088));
        mPoints.add(new LatLng(39.980063, 116.308297));
        return mPoints;
    }

    private PolylineOptions.Text generateText() {
        List<PolylineOptions.SegmentText> segmentTexts = new ArrayList<>();
        //参数分别表示坐标路径数组起点index，终点index，动态路名文字
        segmentTexts.add(new PolylineOptions.SegmentText(0, 1, "苏州街"));
        segmentTexts.add(new PolylineOptions.SegmentText(1, 2, "北四环西路辅路"));
        segmentTexts.add(new PolylineOptions.SegmentText(2, 4, "彩和坊路"));

        return new PolylineOptions.Text.Builder(segmentTexts).build();
    }


    private Polyline createLineWithText() {
        Polyline polyline = tencentMap.addPolyline(new PolylineOptions().addAll(generateLatLngs()).text(generateText()));
        PolylineOptions.Text text = polyline.getText();
        return polyline;
    }


    private void removeText() {
        polyline.setText(null);
    }

}

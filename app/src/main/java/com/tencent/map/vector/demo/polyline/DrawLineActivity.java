package com.tencent.map.vector.demo.polyline;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.AbsMapActivity;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DrawLineActivity extends AbsMapActivity {
    private int typeSimple = 0;
    private int typeImage = 1;
    private int typeColor = 2;
    private int typeArrow = 3;
    private int typeGradient = 4;

    private TencentMap mTencentMap;
    private boolean mHasAdded;
    private boolean mHasEnableText;
    private Polyline polyline;
    private PolylineOptions.Text mPolylineText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap tencentMap) {
        super.onCreate(savedInstanceState, tencentMap);
        mTencentMap = tencentMap;
        tencentMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.polyline, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add).setVisible(!mHasAdded);
        menu.findItem(R.id.menu_text).setTitle(mHasEnableText ? "关闭路名" : "开启路名").setVisible(mHasAdded);
        menu.findItem(R.id.menu_delete).setVisible(mHasAdded);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_line_simple:
                mTencentMap.clear();
                polyline = mTencentMap.addPolyline(setLineStyle(typeSimple));
                mHasAdded = true;
                break;

            case R.id.menu_add_line_dash:
                mTencentMap.clear();
                polyline = mTencentMap.addPolyline(setLineStyle(typeImage));
                mHasAdded = true;
                break;
            case R.id.menu_add_line_segment:
                mTencentMap.clear();
                polyline = mTencentMap.addPolyline(setLineStyle(typeColor));
                int[] color = {0, 1, 2, 3, 4};
                int[] index = {0, 1, 2, 3, 4, 5};
                polyline.setColors(color, index);
                mHasAdded = true;
                break;
            case R.id.menu_add_line_texture:
                mTencentMap.clear();
                polyline = mTencentMap.addPolyline(setLineStyle((typeArrow)));
                mHasAdded = true;
                break;
            case R.id.menu_add_line_gradient:
                mTencentMap.clear();
                polyline = mTencentMap.addPolyline(setLineStyle((typeGradient)));
                int[] indexes = {0, 1, 2, 3, 4};
// 设置每段索引之间的颜色，这个颜色同样支持纹理颜色，即 PolylineOptions.Colors 中定义的 [0, 10] 值
                int[] colors = {
                        0xff00ff00, // 线上点 [0, 1] 之间为绿色
                        0xffffff00, // 线上点 [1, 2] 之间为黄色
                        0xffff0000, // 线上点 [2, 3] 之间为红色
                        0xff131313, // 线上点 [3, 4] 之间为黄色
                        0xff1033f6  // 线上点 [4, 最后一个点] 之间为绿色
                };
                polyline.setColors(colors, indexes);
                mHasAdded = true;
                break;
            case R.id.menu_text:
                mHasEnableText = !mHasEnableText;
                if (mHasEnableText) {
                    if (mPolylineText == null) {
                        mPolylineText = generateText();
                        //设置显示优先级，可选项有HIGH或NORMAL
                        mPolylineText.setPriority(PolylineOptions.TextPriority.HIGH);
                        //设置字体大小
                        mPolylineText.setTextSize(10);
                        //设置填充颜色
                        mPolylineText.setStrokeColor(Color.WHITE);
                        //设置文字颜色
                        mPolylineText.setTextColor(Color.BLACK);
                    }

                    polyline.setText(mPolylineText);
                } else {
                    polyline.setText(null);
                }

                break;

            case R.id.menu_delete:
                if (polyline != null) {
                    polyline.remove();
                    polyline = null;
                    mHasAdded = false;
                    mHasEnableText = false;

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private PolylineOptions setLineStyle(int type) {
        PolylineOptions polylineOptions = new PolylineOptions().addAll(getLatlons()).lineCap(true);
        switch (type) {
            case 0:
                //设置折线颜色、宽度
                polylineOptions
                        .color(0xff00ff00)
                        .width(5f);
                break;
            case 1:
                List<Integer> list = new ArrayList<>();
                list.add(35);
                list.add(20);
                polylineOptions
                        .lineType(PolylineOptions.LineType.LINE_TYPE_IMAGEINARYLINE)
                        .width(10)
                        .pattern(list);
                break;
            case 2:
                //线路颜色值纹理图片里的颜色索引
                polylineOptions
                        .colorType(PolylineOptions.ColorType.LINE_COLOR_TEXTURE)
                        .color(PolylineOptions.Colors.GREEN)
                        .colorTexture(BitmapDescriptorFactory.fromAsset("color_texture.png"));
                break;
            case 3:
                polylineOptions
                        .arrow(true)
                        .arrowSpacing(30)
                        .arrowTexture(BitmapDescriptorFactory.fromAsset("color_arrow_texture.png"));
                break;
            case 4:
                polylineOptions
                        .gradient(true)
                        .lineType(PolylineOptions.LineType.LINE_TYPE_MULTICOLORLINE)
                        .width(20);
                break;

        }
        return polylineOptions;
    }

    private PolylineOptions.Text generateText() {
        List<PolylineOptions.SegmentText> segmentTexts = new ArrayList<>();
        //参数分别表示坐标路径数组起点index，终点index，动态路名文字
        segmentTexts.add(new PolylineOptions.SegmentText(0, 1, "苏州街"));
        segmentTexts.add(new PolylineOptions.SegmentText(1, 2, "北四环西路辅路"));
        segmentTexts.add(new PolylineOptions.SegmentText(2, 4, "彩和坊路"));
        return new PolylineOptions.Text.Builder(segmentTexts).build();
    }

    private List<LatLng> getLatlons() {
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(39.984864, 116.305756));
        latLngs.add(new LatLng(39.983618, 116.305848));
        latLngs.add(new LatLng(39.982347, 116.305966));
        latLngs.add(new LatLng(39.982412, 116.308111));
        latLngs.add(new LatLng(39.984122, 116.308224));
        latLngs.add(new LatLng(39.984955, 116.308099));
        return latLngs;
    }
}

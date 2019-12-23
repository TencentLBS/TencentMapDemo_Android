package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MarkerInfoWindowActivity extends MarkerActivity implements TencentMap.OnInfoWindowClickListener {

    private LatLng latlng_disanji = new LatLng(39.984253,116.307439);

    /**
     * Marker配置
     */
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aSwitch.setVisibility(View.GONE);

        tencentMap.setOnInfoWindowClickListener(this);
        //设置信息窗适配器
        tencentMap.setInfoWindowAdapter(infoWindowAdapter);
        marker = tencentMap.addMarker(new MarkerOptions()
                //标注的位置
                .position(latlng_disanji)
                //标注的InfoWindow的标题
                .title("第三极大厦")
                //标注的InfoWindow的内容
                .snippet("地址: 北京市北四环西路66号")
                //标注的锚点，取值为[0.0 ~ 1.0]
                .anchor(0.5f, 0.5f)
                //设置自定义Marker图标
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        );
    }

    /**
     * 自定义信息窗
     */
    private TencentMap.InfoWindowAdapter infoWindowAdapter = new TencentMap.InfoWindowAdapter() {

        TextView tvTitle;

        //返回View为信息窗自定义样式，返回null时为默认信息窗样式
        @Override
        public View getInfoWindow(final Marker arg0) {
            // TODO Auto-generated method stub
            if (arg0.equals(marker)) {
                LinearLayout custInfowindow = (LinearLayout) View.inflate(
                        getApplicationContext(), R.layout.custom_infowindow, null);
                tvTitle = (TextView)custInfowindow.findViewById(R.id.tv_title);
                //设置自定义信息窗的内容
                tvTitle.setText("custom_infowindow:");
                tvTitle.append("\n" + arg0.getTitle());
                tvTitle.append("\n" + arg0.getSnippet());
                return custInfowindow;
            }
            return null;
        }

        //返回View为信息窗内容自定义样式，返回null时为默认信息窗样式
        @Override
        public View getInfoContents(Marker arg0) {
            // TODO Auto-generated method stub
            return null;
        }
    };
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e("marker:","infoWindow click");
        Toast.makeText(getApplicationContext(),"您点击了信息窗", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClickLocation(int i, int i1, int i2, int i3) {

    }

}

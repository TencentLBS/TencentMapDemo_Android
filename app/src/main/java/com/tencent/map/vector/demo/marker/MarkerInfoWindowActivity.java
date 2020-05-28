package com.tencent.map.vector.demo.marker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.AbsMapActivity;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MarkerInfoWindowActivity extends AbsMapActivity {


    private static final int TYPE_BITMAP = 0;
    private static final int TYPE_VIEW = 1;

    private static final int STYLE_NO_FRAME = 1;
    private static final int STYLE_FRAME = 2;

    private Marker mMarker;
    private Marker mAnotherMarker;
    private TencentMap mTencentMap;
    private int mInfoWindowAdapterStyle;
    private int mInfoWindowType = TYPE_BITMAP;
    private LinearLayout mCustomInfoWindowView;

    /**
     * 自定义信息窗
     */
    private TencentMap.InfoWindowAdapter mInfoWindowAdapter = new TencentMap.InfoWindowAdapter() {

        TextView tvTitle;

        //返回View为信息窗自定义样式，返回null时为默认信息窗样式
        @Override
        public View getInfoWindow(final Marker marker) {
            if (mInfoWindowAdapterStyle == 1 && marker.equals(mMarker)) {
                return createCustomInfoView(marker);
            }
            return null;
        }

        //返回View为信息窗内容自定义样式，返回null时为默认信息窗样式
        @Override
        public View getInfoContents(Marker marker) {
            if (mInfoWindowAdapterStyle == 2 && marker.equals(mMarker)) {
                return createCustomInfoView(marker);
            }
            return null;
        }

        private View createCustomInfoView(Marker marker) {
            mCustomInfoWindowView = (LinearLayout) View.inflate(
                    getApplicationContext(), R.layout.custom_infowindow, null);
            tvTitle = mCustomInfoWindowView.findViewById(R.id.tv_title);
            //设置自定义信息窗的内容
            tvTitle.setText("我是自定义信息窗口:");
            tvTitle.append("\n" + marker.getTitle());
            tvTitle.append("\n" + marker.getSnippet());
            return mCustomInfoWindowView;
        }
    };
    private boolean mMultEnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap pTencentMap) {
        super.onCreate(savedInstanceState, pTencentMap);
        mTencentMap = pTencentMap;
        mMarker = createMarker();
        pTencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                invalidateOptionsMenu();
                return false;
            }
        });
        pTencentMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "信息窗被点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInfoWindowClickLocation(int i, int i1, int i2, int i3) {
                Toast.makeText(getApplicationContext(),
                        "尺寸：" + i + "x" + i1 + " 位置：" + i2 + "," + i3, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Marker createMarker() {
        if (mTencentMap == null) {
            return null;
        }
        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
        }
        LatLng position = new LatLng(39.908710, 116.397499);
        MarkerOptions options = new MarkerOptions(position);
        options.infoWindowEnable(true);//默认为true
        options.title("天安门")//标注的InfoWindow的标题
                .snippet("地址: 北京市东城区东长安街")//标注的InfoWindow的内容
                .anchor(0.5f, 1)
                .viewInfoWindow(mInfoWindowType == TYPE_VIEW)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));//设置自定义Marker图标
        mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        return mTencentMap.addMarker(options);
    }

    private Marker createMultMarker() {
        if (mTencentMap == null) {
            return null;
        }
        if (mAnotherMarker != null) {
            mAnotherMarker.remove();
            mAnotherMarker = null;
        }
        LatLng position = new LatLng(39.902500,116.397750);
        MarkerOptions options = new MarkerOptions(position);
        options.infoWindowEnable(true);//默认为true
        options.title("毛主席纪念堂")//标注的InfoWindow的标题
                .snippet("地址: 北京市东城区前门东大街11号")//标注的InfoWindow的内容
                .anchor(0.5f, 1)
                .viewInfoWindow(mInfoWindowType == TYPE_VIEW)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));//设置自定义Marker图标
        return mTencentMap.addMarker(options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infowindow, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String typeName = null;
        if (mInfoWindowType == TYPE_VIEW) {
            typeName = "View类型";
        } else if (mInfoWindowType == TYPE_BITMAP) {
            typeName = "Bitmap类型";
        }
        if (typeName != null) {
            menu.findItem(R.id.menu_type).setTitle(typeName)
                    .setVisible(!mMarker.isInfoWindowShown());
        }
        menu.findItem(R.id.menu_mult).setTitle(mMultEnable ? "开启多窗口" : "关闭多窗口")
                .setVisible(!mMarker.isInfoWindowShown());
        menu.findItem(R.id.menu_add).setVisible(!mMarker.isInfoWindowShown());
        menu.findItem(R.id.menu_play).setVisible(mMarker.isInfoWindowShown());
        menu.findItem(R.id.menu_delete).setVisible(mMarker.isInfoWindowShown());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mult_enable:
                mMultEnable = true;
                initMarkers();
                hideInfoWindow();
                break;
            case R.id.menu_mult_close:
                mMultEnable = false;
                if (mAnotherMarker != null) {
                    mAnotherMarker.remove();
                    mAnotherMarker = null;
                }

                hideInfoWindow();
                break;
            case R.id.menu_type_bitmap:
                mInfoWindowType = TYPE_BITMAP;
                initMarkers();
                hideInfoWindow();
                break;
            case R.id.menu_type_view:
                mInfoWindowType = TYPE_VIEW;
                initMarkers();
                hideInfoWindow();
                break;
            case R.id.menu_add_infowindow_default:
                showInfoWindow();
                break;
            case R.id.menu_add_infowindow_custom_1:
                mInfoWindowAdapterStyle = STYLE_NO_FRAME;
                mTencentMap.setInfoWindowAdapter(mInfoWindowAdapter);
                showInfoWindow();
                break;
            case R.id.menu_add_infowindow_custom_2:
                mInfoWindowAdapterStyle = STYLE_FRAME;
                mTencentMap.setInfoWindowAdapter(mInfoWindowAdapter);
                showInfoWindow();
                break;
            case R.id.menu_play:
                animationPlay();
                break;
            case R.id.menu_delete:
                hideInfoWindow();
                break;
        }

        mTencentMap.enableMultipleInfowindow(mMultEnable);
        return super.onOptionsItemSelected(item);
    }

    private void initMarkers() {
        mMarker = createMarker();
        if (mMultEnable) {
            mAnotherMarker = createMultMarker();
        }
    }

    private void showInfoWindow() {
        if (mMarker != null) {
            mMarker.showInfoWindow();
        }

        if (mAnotherMarker != null && mMultEnable) {
            mAnotherMarker.showInfoWindow();
        }
    }

    private void hideInfoWindow() {
        if (mMarker != null) {
            mMarker.hideInfoWindow();
        }

        if (mAnotherMarker != null && mMultEnable) {
            mAnotherMarker.hideInfoWindow();
        }
        mTencentMap.setInfoWindowAdapter(null);
    }

    private void animationPlay() {

        if (mInfoWindowType == TYPE_VIEW) {
            //TODO :动画
        }
    }
}

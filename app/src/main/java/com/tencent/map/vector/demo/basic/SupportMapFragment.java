package com.tencent.map.vector.demo.basic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tencent.tencentmap.mapsdk.maps.MapFragment;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;

/**
 * 含有 {@link MapView} 的Fragment, 由于 API Level要求在 12 以下的应用中。
 * <p>如果你的应用要求在 API Level 12以上，用 {@link MapFragment} 代替。 </p>
 * 关于Fragment,参见 {@link <a href="http://developer.android.com/reference/android/support/v4/app/Fragment.html"> Android Fragment</a>}
 */
public class SupportMapFragment extends Fragment {
    private TencentMap mTencentMap;

    protected MapView mapV = null;

    private boolean isOnTop = false;

    /**
     * 创建一个新的SupportMapFragment
     *
     * @param mContex 要创建的Context， 参见 {@link <a href="http://developer.android.com/reference/android/content/Context.html"> Android Context</a>}
     * @return
     */
    public static SupportMapFragment newInstance(Context mContex) {
        SupportMapFragment instance = new SupportMapFragment();
        instance.initMap(mContex);
        return instance;
    }

    public SupportMapFragment() {
    }

    protected void initMap(Context context) {
        mapV = onCreateMapView(context, null);
    }

    /**
     * 获取当前Fragment的 {@link MapView} 关联的 {@link TencentMap}对象。
     *
     * @return
     */
    public TencentMap getMap() {
        if (mTencentMap == null) {
            mTencentMap = mapV.getMap();
        }
        return mTencentMap;
    }

    public View onCreateView(LayoutInflater layoutinflater,
                             ViewGroup viewgroup, Bundle bundle) {
        if (mapV == null) {
            mapV = onCreateMapView(this.getActivity().getBaseContext(), null);
        }
        mapV.setOnTop(isOnTop);
        return mapV;
    }

    protected MapView onCreateMapView(Context context, TencentMapOptions options) {
        return new MapView(context, options);
    }

    public void onResume() {
        mapV.onResume();
        super.onResume();
    }

    public void onPause() {
        mapV.onPause();
        super.onPause();
    }

    public void onDestroyView() {
        mapV.onDestroy();
        super.onDestroyView();
    }

    public void onDestroy() {
        mapV.onDestroy();
        super.onDestroy();
        mapV = null;
    }

    @Override
    public void onStart() {
        mapV.onStart();
        super.onStart();
    }

    @Override
    public void onStop() {
        mapV.onStop();
        super.onStop();
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }

    /**
     * 参见 {@link MapView#setOnTop(boolean)}
     *
     * @param isOnTop
     */
    public void setOnTop(boolean isOnTop) {
        this.isOnTop = isOnTop;
    }
}

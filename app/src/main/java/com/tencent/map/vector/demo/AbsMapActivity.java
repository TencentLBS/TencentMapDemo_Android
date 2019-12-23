package com.tencent.map.vector.demo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.Menu;

import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

public abstract class AbsMapActivity extends AbsActivity {

    private TencentMap mTencentMap;

    protected int getLayoutId() {
        return R.layout.activity_map_container;
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) manager.findFragmentById(R.id.fragment_map);
        if (fragment != null) {
            mTencentMap = fragment.getMap();
        }

        onCreate(savedInstanceState, mTencentMap);
    }

    protected boolean checkMapInvalid() {
        return mTencentMap == null || mTencentMap.isDestroyed();
    }

    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap pTencentMap) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        supportInvalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }
}

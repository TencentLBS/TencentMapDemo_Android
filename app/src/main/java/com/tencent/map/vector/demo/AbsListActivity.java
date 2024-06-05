package com.tencent.map.vector.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;

public abstract class AbsListActivity extends AbsActivity {

    protected int getLayoutId() {
        return R.layout.activity_recycler_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        TencentMapInitializer.setAgreePrivacy(true);
        TencentLocationManager.setUserAgreePrivacy(true);
        final String PRIVACY_URL = "https://privacy.qq.com/document/preview/a10a8634f237464da9a95f4f07e73e40";
        findViewById(R.id.tv_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL)));
            }
        });
    }
}

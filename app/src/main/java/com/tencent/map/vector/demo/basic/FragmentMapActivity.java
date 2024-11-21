package com.tencent.map.vector.demo.basic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationConfig;

public class FragmentMapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        final Button button = findViewById(R.id.btn_change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map_frag, new MyMapFragment())
                        .commitNow();
            }
        });

    }

    public static class MyMapFragment extends SupportMapFragment {

        private LocationSource.OnLocationChangedListener mLocationChangedListener;

        LocationSource myLocationSource = new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                Log.d("MyMapFragment", "LocationSource activate " + MyMapFragment.this);
                mLocationChangedListener = onLocationChangedListener;
            }

            @Override
            public void deactivate() {
                Log.d("MyMapFragment", "LocationSource deactivate " + MyMapFragment.this);
                mLocationChangedListener = null;
            }
        };

        TencentMap.OnMapLoadedCallback thisMapLoadedCallback = new TencentMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d("MyMapFragment", "onMapLoaded: " + MyMapFragment.this);
                getMap().removeOnMapLoadedCallback(this);
            }
        };

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Log.d("MyMapFragment", "onViewCreated: " + MyMapFragment.this);
            getMap().addOnMapLoadedCallback(thisMapLoadedCallback);
            getMap().getUiSettings().setScaleViewFadeEnable(false);
            getMap().setMyLocationConfig(MyLocationConfig.newBuilder()
                    .setLocationSource(myLocationSource).build());
        }

        @Override
        public void onDestroyView() {
            //保证外部设置的变量在销毁时主动置空，否则可能会有泄露
            mLocationChangedListener = null;
            myLocationSource = null;
            Log.d("MyMapFragment", "onDestroyView: " + MyMapFragment.this);
            super.onDestroyView();
        }
    }
}

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.map.vector.demo.basic;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import androidx.fragment.app.FragmentActivity;

import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

/**
 * Demonstrates the different base layers of a map.
 */
@SuppressLint("NewApi")
public class MapStyleDemoActivity extends FragmentActivity implements TencentMap.OnCameraChangeListener {
    private MapView mMapView;
    private TextView mTextView;
    private TencentMap mTencentMap;
    private Switch mSwitch;
    private Spinner mSpinner;

    private String[] styles = new String[]{"style1", "style2", "style3", "style4", "style5",
            "normal", "traffic_navi", "traffic_navi_night", "satellite", "night", "navi", "night", "eagle_day", "eagle_night"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_style);
        mMapView = findViewById(R.id.map_view);
        mTextView = findViewById(R.id.tv_level);
        mSwitch=findViewById(R.id.switch_open);
        mTencentMap = mMapView.getMap();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.901268, 116.403854), 11f, 0f, 0f));
        mTencentMap.moveCamera(cameraUpdate);
        //mTencentMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.901268, 116.403854))); //移动地图
        mTencentMap.setOnCameraChangeListener(this);
        mSpinner = findViewById(R.id.sp_style);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, styles);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 5) {
                    mTencentMap.setMapStyle(position + 1);
                }
                switch (position) {
                    case 5:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_NORMAL);
                        break;
                    case 6:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_TRAFFIC_NAVI);
                        break;
                    case 7:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_TRAFFIC_NIGHT);
                        break;
                    case 8:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_SATELLITE);
                        break;
                    case 9:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_NIGHT);
                        break;
                    case 10:
                        mTencentMap.setMapStyle(TencentMap.MAP_TYPE_NAVI);
                        break;
                    case 11:
                        mTencentMap.setMapStyle(13 + 1000);
                        break;
                    case 12:
                        mTencentMap.setMapStyle(14 + 1000);
                        break;
                    case 13:
                        mTencentMap.setMapStyle(15 + 1000);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadioGroup rg = findViewById(R.id.rg_map_type);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_normal_type:
                        if (mTencentMap != null) {
                            mTencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
                        }
                        break;
                    case R.id.rb_dark_type:
                        if (mTencentMap != null) {
                            mTencentMap.setMapType(TencentMap.MAP_TYPE_DARK);
                        }
                        break;
                    case R.id.rb_sagellite_type:
                        if (mTencentMap != null) {
                            mTencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
                        }
                        break;
                }
            }
        });
        mTencentMap.getUiSettings().setCompassEnabled(true);
        mTencentMap.getUiSettings().setMyLocationButtonEnabled(true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PorterDuff.Mode buttonTintMode = compoundButton.getButtonTintMode();
                if(b){
                    mTencentMap.setBuildingEnable(true);
                }else{
                    mTencentMap.setBuildingEnable(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.i("TAG","地图滑动了");
        mTextView.setText("当前缩放级别为：" + cameraPosition.zoom);
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {
        Log.i("TAG","地图滑动了");
        mTextView.setText("当前缩放级别为：" + cameraPosition.zoom);
    }
}

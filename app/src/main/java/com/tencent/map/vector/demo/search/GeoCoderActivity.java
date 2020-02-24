package com.tencent.map.vector.demo.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.httpresponse.Poi;
import com.tencent.lbssearch.object.param.Address2GeoParam;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Address2GeoResultObject;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class GeoCoderActivity extends SupportMapFragmentActivity {

    private EditText etGeocoder;
    private Button btnGeocoder;
    private EditText etRegeocoder;
    private Button btnRegeocoder;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView(){
        etGeocoder = (EditText) findViewById(R.id.et_geocoder);
        btnGeocoder = (Button) findViewById(R.id.btn_geocoder);
        etRegeocoder = (EditText) findViewById(R.id.et_regeocoder);
        btnRegeocoder = (Button) findViewById(R.id.btn_regeocoder);
        etGeocoder.setVisibility(View.VISIBLE);
        btnGeocoder.setVisibility(View.VISIBLE);
        etRegeocoder.setVisibility(View.VISIBLE);
        btnRegeocoder.setVisibility(View.VISIBLE);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvResult.setVisibility(View.VISIBLE);
        btnGeocoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geocoder();
            }
        });

        btnRegeocoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reGeocoder();
            }
        });
    }
    /**
     *地理编码
     */
    protected void geocoder() {
        TencentSearch tencentSearch = new TencentSearch(this);
        String address = etGeocoder.getText().toString();
        Address2GeoParam address2GeoParam =
                new Address2GeoParam(address).region("北京");
        tencentSearch.address2geo(address2GeoParam, new HttpResponseListener<BaseObject>() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    return;
                }
                Address2GeoResultObject obj = (Address2GeoResultObject)arg1;
                StringBuilder sb = new StringBuilder();
                sb.append("地址解析");
                if (obj.result.latLng != null) {
                    sb.append("\n坐标：" + obj.result.latLng.toString());
                } else {
                    sb.append("\n无坐标");
                }
                printResult(sb.toString());
                tencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(obj.result.latLng,15f, 0, 0)));
                tencentMap.addMarker(new MarkerOptions()
                        .position(obj.result.latLng));
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                printResult(arg1);
            }
        });
    }

    /**
     * 逆地理编码
     */
    protected void reGeocoder() {
        String str = etRegeocoder.getText().toString().trim();
        LatLng latLng = str2Coordinate(this, str);
        if (latLng == null) {
            return;
        }
        TencentSearch tencentSearch = new TencentSearch(this);
        //还可以传入其他坐标系的坐标，不过需要用coord_type()指明所用类型
        //这里设置返回周边poi列表，可以在一定程度上满足用户获取指定坐标周边poi的需求
        Geo2AddressParam geo2AddressParam = new Geo2AddressParam(latLng).getPoi(true)
                .setPoiOptions(new Geo2AddressParam.PoiOptions()
                        .setRadius(1000).setCategorys("面包")
                        .setPolicy(Geo2AddressParam.PoiOptions.POLICY_O2O));
        tencentSearch.geo2address(geo2AddressParam, new HttpResponseListener<BaseObject>() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    return;
                }
                Geo2AddressResultObject obj = (Geo2AddressResultObject)arg1;
                StringBuilder sb = new StringBuilder();
                sb.append("逆地址解析");
                sb.append("\n地址：" + obj.result.address);
                sb.append("\npois:");
                for (Poi poi : obj.result.pois) {
                    sb.append("\n\t" + poi.title);
                    tencentMap.addMarker(new MarkerOptions()
                            .position(poi.latLng)  //标注的位置
                            .title(poi.title)     //标注的InfoWindow的标题
                            .snippet(poi.address) //标注的InfoWindow的内容
                    );
                }
                //printResult(sb.toString());
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                printResult(arg1);
            }
        });
    }


    /**
     * 由字符串获取坐标
     * @param context
     * @param str
     * @return
     */
    public static LatLng str2Coordinate(Context context, String str) {
        if (!str.contains(",")) {
            Toast.makeText(context, "经纬度用\",\"分割", Toast.LENGTH_SHORT).show();
            return null;
        }
        String[] strs = str.split(",");
        double lat = 0;
        double lng = 0;
        try {
            lat = Double.parseDouble(strs[0]);
            lng = Double.parseDouble(strs[1]);
        } catch (NumberFormatException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return new LatLng(lat, lng);
    }
    protected void printResult(final String result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                tvResult.setText(result);
            }
        });
    }
}

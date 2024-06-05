package com.tencent.map.vector.demo.location;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.google.gson.Gson;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationLayerActivity extends SupportMapFragmentActivity implements EasyPermissions.PermissionCallbacks, LocationSource, TencentLocationListener, TencentMap.OnMapLongClickListener, RadioGroup.OnCheckedChangeListener {

    private OnLocationChangedListener locationChangedListener;

    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private MyLocationStyle locationStyle;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //定位需要申请的权限
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            Log.i("location", "已获取权限");
        } else {
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perms);
        }
        //设置显示定位的图标
        mapUiSettings.setMyLocationButtonEnabled(true);
        tencentMap.setOnMapLongClickListener(this);

        radioGroup = findViewById(R.id.location_type);
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(this);

        //建立定位
        initLocation();
        //SDK版本4.3.5新增内置定位标点击回调监听
        tencentMap.setMyLocationClickListener(new TencentMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClicked(LatLng latLng) {
                Toast.makeText(LocationLayerActivity.this, "内置定位标点击回调", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * 定位的一些初始化设置
     */
    private void initLocation() {
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        locationManager = TencentLocationManager.getInstance(this);
        //设置坐标系
        locationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        //创建定位请求
        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest.setInterval(3000);

        //地图上设置定位数据源
        tencentMap.setLocationSource(this);
        //设置当前位置可见
        tencentMap.setMyLocationEnabled(true);
        //设置定位图标样式
        setLocMarkerStyle();
        tencentMap.setMyLocationStyle(locationStyle);
    }

    /**
     * 设置定位图标样式
     */
    private void setLocMarkerStyle() {
        locationStyle = new MyLocationStyle();
        //创建图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.location_icon));
        locationStyle.icon(bitmapDescriptor);
        //设置定位圆形区域的边框宽度
        locationStyle.strokeWidth(3);
        //设置圆区域的颜色
        locationStyle.fillColor(R.color.style);
    }

    private Bitmap getBitMap(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 55;
        int newHeight = 55;
        float widthScale = ((float) newWidth) / width;
        float heightScale = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    /**
     * 实现位置监听
     *
     * @param tencentLocation
     * @param i
     * @param s
     */
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {

        if (i == TencentLocation.ERROR_OK && locationChangedListener != null) {
            Location location = new Location(tencentLocation.getProvider());
            //设置经纬度以及精度
            location.setLatitude(tencentLocation.getLatitude());
            location.setLongitude(tencentLocation.getLongitude());
            location.setAccuracy(tencentLocation.getAccuracy());
            locationChangedListener.onLocationChanged(location);

            //显示回调的实时位置信息
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //打印tencentLocation的json字符串
//                    Toast.makeText(getApplicationContext(), new Gson().toJson(location), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        //GPS, WiFi, Radio 等状态发生变化
        Log.v("State changed", s + "===" + s1);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;

        int err = locationManager.requestLocationUpdates(locationRequest, this, Looper.myLooper());
        switch (err) {
            case 1:
                Toast.makeText(this, "设备缺少使用腾讯定位服务需要的基本条件", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "manifest 中配置的 key 不正确", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "自动加载libtencentloc.so失败", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void deactivate() {
        locationManager.removeUpdates(this);
        locationManager = null;
        locationRequest = null;
        locationChangedListener = null;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e("location quest: ", "success");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e("location quest: ", "failed");
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Location location = new Location("LongPressLocationProvider");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        location.setAccuracy(20);
        locationChangedListener.onLocationChanged(location);
        Log.i("long click", new Gson().toJson(latLng));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            //连续定位，但不会移动到地图中心点，并且会跟随设备移动
            case R.id.btn_follow_no_center:

                initLocation();
                locationStyle = locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
                tencentMap.setMyLocationStyle(locationStyle);
                break;
            //连续定位，且将视角移动到地图中心，定位点依照设备方向旋转，并且会跟随设备移动,默认是此种类型
            case R.id.btn_location_rotate:

                initLocation();
                locationStyle = locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
                tencentMap.setMyLocationStyle(locationStyle);
                break;
            //连续定位，但不会移动到地图中心点，定位点依照设备方向旋转，并且跟随设备移动
            case R.id.btn_location_rotate_no_center:

                initLocation();
                locationStyle = locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
                tencentMap.setMyLocationStyle(locationStyle);
                break;
            //连续定位，但不会移动到地图中心点，地图依照设备方向旋转，并且会跟随设备移动
            case R.id.btn_map_rotate_no_center:

                initLocation();
                locationStyle = locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);
                tencentMap.setMyLocationStyle(locationStyle);
                break;
        }
    }
}

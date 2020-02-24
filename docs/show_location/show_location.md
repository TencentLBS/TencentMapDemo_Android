### 显示当前定位

腾讯地图 SDK 并不包含定位功能，只提供展示定位的组件，方便用户在获得到定位信息后在地图上展示出来。用户可以接入 [腾讯定位 SDK](https://lbs.qq.com/geo/index.html) 获得腾讯提供的定位能力。

定位图标的基本设置以及说明如下：

```java
/**
 * 有关定位的一些初始化设置
 */
private void initLocation(){
    // 用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
    TencentLocationManager locationManager = TencentLocationManager.getInstance(this);
    // 设置坐标系
    locationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_WGS84);
    // 创建定位请求
    TencentLocationRequest locationRequest = TencentLocationRequest.create();
    // 设置定位周期（位置监听器回调周期）为10s
    locationRequest.setInterval(10000);
    // 地图上设置定位数据源
    tencentMap.setLocationSource(this);
    // 设置当前位置可见
    tencentMap.setMyLocationEnabled(true);
    // 设置定位图标样式
    setLocMarkerStyle();
}

/**
 * 设置定位图标样式
 */
private void setLocMarkerStyle(){
    locationStyle = new MyLocationStyle();
    // 创建图标
    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.navi_marker_location));
    locationStyle.icon(bitmapDescriptor);
    // 设置定位圆形区域的边框宽度
    locationStyle.strokeWidth(3);
    // 设置圆区域的颜色
    locationStyle.fillColor(R.color.style);
    tencentMap.setMyLocationStyle(locationStyle);
}
```

开启/移除定位事件监听器：

```java
@Override
public void activate(OnLocationChangedListener onLocationChangedListener) {
    locationChangedListener = onLocationChangedListener;

    int err = locationManager.requestLocationUpdates(locationRequest, this, Looper.myLooper());
    switch (err) {
        case 1:
            Toast.makeText(this,"设备缺少使用腾讯定位服务需要的基本条件",Toast.LENGTH_SHORT).show();
            break;
        case 2:
            Toast.makeText(this,"manifest 中配置的 key 不正确",Toast.LENGTH_SHORT).show();
            break;
        case 3:
            Toast.makeText(this,"自动加载libtencentloc.so失败",Toast.LENGTH_SHORT).show();
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
    locationChangedListener=null;
}
```

对当前定位状态的监听实现：

```java
/**
 * 实现位置改变的监听
 * @param tencentLocation
 * @param i
 * @param s
 */
@Override
public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {

    if(i == TencentLocation.ERROR_OK && locationChangedListener != null) {
        Location location = new Location(tencentLocation.getProvider());
        // 设置经纬度以及精度
        location.setLatitude(tencentLocation.getLatitude());
        location.setLongitude(tencentLocation.getLongitude());
        location.setAccuracy(tencentLocation.getAccuracy());
        locationChangedListener.onLocationChanged(location);

        // 显示回调的实时位置信息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 打印tencentLocation的json字符串
                Toast.makeText(getApplicationContext(), new Gson().toJson(location), Toast.LENGTH_LONG).show();
            }
        });
    }
}

/**
 * GPS, WIFI, Radio等状态发生变化的监听
 * @param s
 * @param i
 * @param s1
 */
@Override
public void onStatusUpdate(String s, int i, String s1) {
    // GPS, WiFi, Radio 等状态发生变化
    Log.v("State changed", s +"===" + s1);
}
```

具体关于定位的样例Demo可以参考TencentMapDemo项目中LocationLayerActivity.java文件。
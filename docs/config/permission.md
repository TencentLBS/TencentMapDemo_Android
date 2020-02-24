### 权限配置

地图SDK需要使用网络，访问硬件存储等系统权限，在AndroidManifest.xml文件里，添加如下权限：

```
<!--腾讯地图 SDK 要求的权限(开始)-->
<!--访问网络获取地图服务-->
<uses-permission android:name="android.permission.INTERNET"/>
<!--检查网络可用性-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<!-- 访问WiFi状态 -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!--需要外部存储写权限用于保存地图缓存-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<!--获取 device id 辨别设备-->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<!--腾讯地图 SDK 要求的权限(结束)-->
```
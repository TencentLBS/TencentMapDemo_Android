## 坐标转换

屏幕坐标与经纬度之间的转换：

```java
// 经纬度转换为像素点
Projection projection = tencentMap.getProjection();
Point screen = projection.toScreenLocation(latLng);
Toast.makeText(getApplicationContext(), ("屏幕坐标：" + new Gson().toJson(screen)), Toast.LENGTH_LONG).show();

// 像素点转换为经纬度
LatLng transferLatLng = projection.fromScreenLocation(screen);
Toast.makeText(getApplicationContext(), ("经纬度坐标：" + new Gson().toJson(transferLatLng)), Toast.LENGTH_LONG).show();    
```

当前屏幕地图的视野范围：

```java
// 获取当前屏幕地图的视野范围
Projection projection = tencentMap.getProjection();
VisibleRegion region = projection.getVisibleRegion();
Toast.makeText(getApplicationContext(), ("当前地图的视野范围：" + new Gson().toJson(region)), Toast.LENGTH_LONG).show();
```
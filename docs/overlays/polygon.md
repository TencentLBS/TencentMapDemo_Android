### 多边形

多边形是由 Polygon 类定义的一组在地图上的封闭线段组成的图形，它由一组 LatLng 点按顺序连接而成的封闭图形。

多边形的实例化需要一个 PolygonOptions 对象，该对象可以设置多边形相关选项，包括顶点、描边宽度和颜色、填充颜色、堆叠顺序、折线是否可见、多边形是否可见、多边形是否支持点击、文本的显示、颜色、大小、字体等。

添加折线返回的是一个 Polygon 对象，Polygon 是在地图上画多边形的类，可以移除`remove()`多边形、设置多边形顶点、描边的宽度和颜色、多边形的填充色、层级关系、可见性、可点击性，也可以通过`setOptions(PolygonOptions opts)`设置一组属性。

下面的代码展示了添加多边形、设置多边形属性、移除多边形的方法：

```java
//添加多边形
LatLng[] latLngs = {
        new LatLng(39.873911,116.379548),
        new LatLng(39.985538,116.448212),
        new LatLng(39.946595,116.387788),
        new LatLng(39.980277,116.305390)};
Polygon polygon = tencentMap.addPolygon(new PolygonOptions().
        //连接封闭图形的点
        add(latLngs).
        //填充颜色为红色
        fillColor(0xffff0000).
        //边线颜色为黑色
        strokeColor(0xff000000).
        //边线宽度15像素
        strokeWidth(15));

// 将地图视野移动到多边形所在区域，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.873911, 116.379548), 10));

// 设置多边形描边的宽度
polygon.setStrokeWidth(5);

// 移除多边形
polygon.remove();
```

绘制的多边形效果如下图所示：

![多边形](/overlays/images/polygon.jpg)
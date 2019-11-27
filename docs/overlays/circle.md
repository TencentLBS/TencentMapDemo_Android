### 圆

圆形是由 Circle 类定义的封闭曲线，在腾讯地图构造一个圆形需要确定它的圆心和半径。

圆的实例化需要一个 CircleOptions 对象，该对象是创建圆的参数类，可以设置圆心坐标、半径、描边的宽度和颜色、圆的填充颜色、层级、可见性、可点击性等属性。

添加圆返回的是一个 Circle 对象，该队形是在地图上画圆的类，可以通过它修改圆的属性。

添加圆、修改属性、移除圆的具体示例代码如下：

```java
//添加圆
LatLng latLng = new LatLng(39.984059,116.307771);
circle = tencentMap.addCircle(new CircleOptions().
    // 圆心
    center(latLng).
    // 半径
    radius(1000d).
    // 圆的填充色为蓝色
    fillColor(0xff0000ff).
    // 描边的颜色为黑色
    strokeColor(0xff000000).
    // 描边宽度
    strokeWidth(15).
    // 可点击性
    clickable(false).
    // 可见性
    visible(true).
    // 层级
    zIndex(2));

// 将地图视野移动到多边形所在区域，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.984059,116.307771), 10));

// 设置圆的填充色为红色
circle.setFillColor(0xFFFF0000);

// 移除圆
circle.remove();
```

绘制的圆效果如下图所示：

![圆形](/overlays/images/circle.jpg)
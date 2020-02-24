### 地图视野
----

地图视野操作主要包括地图的缩放、移动、旋转、倾斜、改变地图中心点等操作，以动画的方式把地图变换到指定的状态。CameraUpdateFactory提供13种方法执行地图视野操作。

可以通过 TencentMap 的 `animateCamera()` 方法以动画的方式把地图变换到指定的状态，也可以通过TencentMap的 `moveCamera()` 方法直接把地图变换到指定的状态； TencentMap 的 `stopAnimation()` 可以停止地图所有正在进行的动画。具体如下：

```java
/**
 * 以动画的方式把地图变换到指定的状态
 *
 * 参数：
 * cameraupdate：状态对象
 * duration：动画执行时间（单位：ms）
 * cancelablecallback：动画状态监听器
 */
tencentMap.animateCamera(CameraUpdate cameraupdate);

tencentMap.animateCamera(CameraUpdate cameraupdate, CancelableCallback cancelablecallback);

tencentMap.animateCamera(CameraUpdate cameraupdate, long duration,CancelableCallback cancelablecallback);

// 直接把地图变换到指定的状态
tencentMap.moveCamera(CameraUpdate cameraupdate);

// 停止地图所有正在进行的动画
tencentMap.stopAnimation();
```

#### 改变地图视野
----

由CameraPosition生成地图状态对象，直接改变地图视野。

__注意：腾讯地图SDK使用的是国测局坐标系（火星坐标，GCJ-02）。__

直接设置地图视野到指定状态的示例代码如下：

```java
CameraUpdate cameraSigma =
    CameraUpdateFactory.newCameraPosition(new CameraPosition(
        new LatLng(39.977290,116.337000), // 中心点坐标，地图目标经纬度
        19,    // 目标缩放级别
        45f,   // 目标倾斜角[0.0 ~ 45.0] (垂直地图时为0)
        45f)); // 目标旋转角 0~360° (正北方为0)

tencentMap.moveCamera(cameraSigma); // 移动地图
```

效果如下图所示：
![sigma](/show_map/images/camera_position.jpg)

#### 设置地图的锚点
----

设置方法为：

```java
/**
 * 设置camera变换的中心点
 *
 * xratio 水平方向位置
 * yratio 垂直方向位置
 */
tencentMap.setCameraCenterProportion(float xratio, float yratio);
```

或者:

```java
/**
 * 设置camera变换的中心点
 *
 * xratio 水平方向位置
 * yratio 垂直方向位置
 * moveMap 改变中心点的同时是否将原有中心点移到新的中心点
 */
tencentMap.setCameraCenterProportion(float xratio, float yratio, boolean moveMap);
```

#### 修改地图中心点
----

CameraUpdateFactory中提供`scrollBy()`方法平移地图，`newLatLng()`方法改变地图中心点、移动地图，如下所示：

```java
/**
 * 将地图向左下移动xPixel和yPexil像素
 * 
 * xPixel: 向左移动的像素
 * yPixel: 向下移动的像素
 */
tencentMap.animateCamera(CameraUpdateFactory.scrollBy(float xPixel, float yPixel));

/**
 * 把地图移动指定的经纬度到屏幕中心
 * 
 * latlng: 为目标经纬度
 */
tencentMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng latlng));

/**
 * 把地图以latlng为中心，以zoom为缩放级别，移到屏幕中心
 * 
 * latlng: 目标经纬度
 * zoom: 新的指定级别，取值范围为 [3.0 ~ 19.0]
 */
tencentMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng latlng, float zoom));
```

把地图移动到指定的经纬度范围，具体应用参见“地图标注”部分：

```java
/**
 * 把地图移到到以指定的经纬度范围的，并且在这个范围四周加上padding像素的填充
 *
 * latLngBounds: 经纬度范围
 * padding: 四周填充的像素
 */
tencentMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds latlngbounds, int padding));

/**
 * 把地图移动到指定经纬度范围，以适应当前屏幕的显示，并且可以为上下左右分别设置填充的像素
 *
 * latlngbounds: 经纬度范围
 * leftPadding: 左侧填充像素
 * rightPadding: 右侧填充像素
 * topPadding: 右侧填充像素
 * bottomPadding: 顶部填充像素、底部填充像素
 */
tencentMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(LatLngBounds latlngbounds, int leftPadding, int rightPadding, int topPadding, int bottomPadding));

/**
 * 把地图移动到指定元素所在范围，以适应当前屏幕的显示，并且可以为上下左右分别设置填充的像素.
 *
 * latLngBounds: 经纬度范围（指定西南和东北的坐标点）
 * padding: 四周填充的像素
 * leftPadding: 左侧填充像素
 * rightPadding: 右侧填充像素
 * topPadding: 顶部填充像素
 * bottomPadding: 底部填充像素
 * elements: 要显示在底图上的元素
 */
tencentMap.animateCamera(CameraUpdateFactory.newElementBoundsRect(List<IMapElement> elements, int leftPadding, int rightPadding, int topPadding, int bottomPadding));
```

#### 修改地图缩放级别
----

CameraUpdateFactory中提供`zoomIn()`、`zoomOut()`、`zoomTo()`、`zoomBy()`等方法可以改变地图的缩放级别。

设置方法为：

```java
// 把地图放大一级
tencentMap.animateCamera(CameraUpdateFactory.zoomIn()); 

// 把地图缩小一级
tencentMap.animateCamera(CameraUpdateFactory.zoomOut()); 

// 把地图缩放到目标级别zoomLevel，取值范围为 [4.0 ~ 20.0]
tencentMap.animateCamera(CameraUpdateFactory.zoomTo(float zoomLevel));

// 在当前的缩放级别下，把缩放级别变化zoomLevelDelta，如果超出了范围，则停止在范围内
tencentMap.animateCamera(CameraUpdateFactory.zoomBy(float zoomLevelDelta)); 

/**
 * 在当前的缩放级别下，把缩放级别变化zoomLevelDelta，如果超出了范围，则停止在范围内；
 *
 * zoomLevelDelta: 要变化的级别数值
 * point: 当前地图所处的地图点，移动后屏幕处于中心点
 */
tencentMap.animateCamera(CameraUpdateFactory.zoomBy(float zoomLevelDelta, Point point));
```

#### 限制地图的缩放级别范围
----

```java
// 设置地图的最大缩放级别  （3~19）
tencentMap.setMaxZoomLevel(int zoomLevel); 

// 设置地图的最小缩放级别
tencentMap.setMinZoomLevel(int zoomLevel); 
```

#### 旋转、倾斜地图
----

设置方法为：

```java
// 将地图顺时针旋转rotate度，并且向后倾斜skew度
tencentMap.animateCamera(CameraUpdateFactory.rotateTo(float rotate, float skew));
```

#### 地图视野变化监听
----

```java
// 设置地图视野变化的监听接口
tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
        @Override public void onCameraChange(CameraPosition cameraPosition) {

        }

        @Override public void onCameraChangeFinished(CameraPosition cameraPosition) {

        }
    }
);
```
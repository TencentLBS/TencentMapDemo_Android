### 地图 UI 设置
----

腾讯地图提供了UiSettings类以方便开发者对地图手势及SDK提供的控件的控制，以定制自己想要的视图效果。UiSettings类的实例化也是通过TencentMap来获取：

```java
// 获取地图界面操作对象
UiSettings mUiSettings = tencentMap.getUiSettings();
```
#### 控件设置
----

__缩放按钮__

可以控制地图的缩放级别，每次点击改变1个级别，开启后此控件显示在右下角，设置方式及效果图如下所示：

```java
// 设置是否显示缩放按钮，true表示显示，false表示不显示
mUiSettings.setZoomControlsEnabled(boolean flag); 
// 获取当前是否显示缩放控件
mUiSettings.isZoomControlsEnabled();
```

![缩放控件](/show_map/images/zoom.jpg)

__指南针__

可以指示地图的南北方向，默认的视图状态下不显示，只有在地图的偏航角或俯仰角不为0时才会显示，并且该控件的默认点击事件会将地图视图的俯仰角和偏航角动画到0的位置。此控件显示在左上角，设置方式及效果图如下所示：

```java
// 设置是否显示罗盘
mUiSettings.setCompassEnabled(boolean flag);
// 获取当前是否开启罗盘控件
mUiSettings.isCompassEnabled();
// 设置是否显示罗盘
mUiSettings.setCompassEnabled(true);
// 地图罗盘点击事件
tencentMap.setOnCompassClickedListener(new TencentMap.OnCompassClickedListener() {
    @Override
    public void onCompassClicked() {

    }
}
);
```
![指南针](/show_map/images/compass.jpg)

罗盘的填边设置请参考“接口文档”。

__定位控件__

当通过 `TencentMap.setLocationSource(locationSource)` 设置好地图的定位源后，点击此按钮可以在地图上标注一个蓝点指示用户的当前位置。定位控件显示在右下角，设置方式和效果图如下所示：

```java
// 设置是否显示定位按钮
mUiSettings.setMyLocationButtonEnabled(boolean flag); 
// 获取当前是否显示定位控件
mUiSettings.isMyLocationButtonEnabled();
```
![定位按钮](/show_map/images/location.jpg)

__比例尺控件__

默认显示在左下角，也可以通过参数进行设置，设置方式和效果图如下所示：

```java
// 设置当前是否显示比例尺
mUiSettings.setScaleViewEnabled(true);
// 获取当前是否显示比例尺
mUiSettings.isScaleViewEnabled();
// 设置比例尺的显示位置
/**
    * 设置比例尺的显示位置
    *
    *  @param position 地图比例尺显示位置：
    *   {@link TencentMapOptions#SCALEVIEW_POSITION_BOTTOM_CENTER}
    *   {@link TencentMapOptions#SCALEVIEW_POSITION_BOTTOM_LEFT}
    *   {@link TencentMapOptions#SCALEVIEW_POSITION_BOTTOM_RIGHT}
    *   {@link TencentMapOptions#SCALEVIEW_POSITION_TOP_CENTER}
    *   {@link TencentMapOptions#SCALEVIEW_POSITION_TOP_LEFT}
    *   {@link TencentMapOptions#SCALEVIEW_POSITION_TOP_RIGHT}
    */
UiSettings.setScaleViewPosition(int position);
```
![比例尺](/show_map/images/scale.jpg)

__地图Logo__

腾讯地图logo默认显示在右下角，改变Logo位置的示例代码如下：

```java
/**
* 设置Logo的显示位置
*  @param logoAnchor 地图logo显示位置：
*    {@link TencentMapOptions#LOGO_POSITION_BOTTOM_CENTER}
*    {@link TencentMapOptions#LOGO_POSITION_BOTTOM_LEFT}
*    {@link TencentMapOptions#LOGO_POSITION_BOTTOM_RIGHT}
*    {@link TencentMapOptions#LOGO_POSITION_TOP_CENTER}
*    {@link TencentMapOptions#LOGO_POSITION_TOP_LEFT}
*    {@link TencentMapOptions#LOGO_POSITION_TOP_RIGHT}
*/
UiSettings.setLogoPosition(int logoAnchor);
```
![Logo](/show_map/images/logo.jpg)

注意：使用腾讯地图SDK要求显示logo，如上图所示。如有特殊需求可通过 `setLogoPositionWithMargin()` 方法设置偏移，示例代码如下：

```java
/**
 * 设置Logo的显示位置，带偏移量，单位像素
 *
 * position: 地图logo显示位置，选项同上
 * top:      position为TOP时，该值生效，不需要偏移请传0或负数
 * bottom:   position为BOTTOM时，该值生效，不需要偏移请传0或负数
 * left:     position为LEFT时，该值生效，不需要偏移请传0或负数
 * right:    position为RIGHT时，该值生效，不需要偏移请传0或负数
 * 
 */
UiSettings.setLogoPositionWithMargin(int position, int top, int bottom, int left, int right); 
```

或：

```java
/**
* 根据地图四个Anchor 模式（左上、左下、右下、右上）以及相应的Margins来设置Logo位置
*
* @param logoAnchor
*    {@link TencentMapOptions#LOGO_POSITION_BOTTOM_LEFT}
*    {@link TencentMapOptions#LOGO_POSITION_BOTTOM_RIGHT}
*    {@link TencentMapOptions#LOGO_POSITION_TOP_LEFT}
*    {@link TencentMapOptions#LOGO_POSITION_TOP_RIGHT}
* @param marginParams
*    若logoAnchor为 LOGO_POSITION_BOTTOM_LEFT,则Logo的 bottomMargin 为 marginParams[0], leftMargin 为 ma  rginParams[1]
*    若logoAnchor为 LOGO_POSITION_BOTTOM_RIGHT,则Logo的 bottomMargin 为 marginParams[0], rightMargin 为 ma  rginParams[1]
*    若logoAnchor为 LOGO_POSITION_TOP_RIGHT,则Logo的 topMargin 为 marginParams[0], rightMargin 为 marginParams[1]
*    若logoAnchor为 LOGO_POSITION_TOP_LEFT ，则Logo 的 topMargin 为 marginParams[0], leftMargin 为 marginParams[1]
*/
UiSettings.setLogoPosition(int logoAnchor , int[] marginParams); 
```

### 手势设置与监听
----

__拖动手势__

可以用手指拖动地图四处滚动（平移）或用手指滑动地图（动画效果），也可以禁用或开启平移（滑动）手势。

```java
// 设置是否开启地图滚动手势
mUiSettings.setScrollGesturesEnabled(boolean flag);
// 获取当前是否支持滚动手势
mUiSettings.isScrollGesturesEnabled();
```

__缩放手势__

缩放手势可改变地图的缩放级别：双击地图可以使缩放级别增加1 \(放大\)；两个手指捏/拉伸也可以控制缩放级别。

禁用缩放手势不会影响用户使用地图上的缩放控制按钮。

```java
// 设置是否开启地图缩放手势
mUiSettings.setZoomGesturesEnabled(boolean flag);
// 获取当前是否支持缩放手势
mUiSettings.isZoomGesturesEnabled(); 
```

__倾斜手势__

通过两个手指的移动，控制地图的倾斜角。

```java
// 设置是否开启地图倾斜手势
mUiSettings.setTiltGesturesEnabled(boolean flag);
// 获取当前是否支持倾斜手势
mUiSettings.isTiltGesturesEnabled();
```

__旋转手势__

通过两个手指控制，旋转3D地图。

```java
// 设置是否开启地图旋转手势
mUiSettings.setRotateGesturesEnabled(boolean flag);
// 获取当前是否支持旋转手势
mUiSettings.isRotateGesturesEnabled();
```

__所有手势__

包括滚动手势、缩放手势、倾斜手势、旋转手势，可同时设置手势的开启或禁止。

```java
// 设置是否开启地图所有手势
mUiSettings.setAllGesturesEnabled(boolean flag);
```

__手势监听__

手势监听器的事件会通知到所有监听器。

```java
//设置腾讯地图手势监听接口
tencentMap.setTencentMapGestureListener(new TencentMapGestureListener() {
        //单指双击
        @Override
        public boolean onDoubleTap(float v, float v1) {
            return false;
        }
    
        //单指单击
        @Override
        public boolean onSingleTap(float v, float v1) {
            return false;
        }

        //单指惯性滑动
        @Override
        public boolean onFling(float v, float v1) {
            return false;
        }

        //单指滑动
        @Override
        public boolean onScroll(float v, float v1) {
            return false;
        }

        //长按
        @Override
        public boolean onLongPress(float v, float v1) {
            return false;
        }

        //单指按下
        @Override
        public boolean onDown(float v, float v1) {
            return false;
        }

        //单指抬起
        @Override
        public boolean onUp(float v, float v1) {
            return false;
        }

        //地图稳定下来
        @Override
        public void onMapStable() {

        }
    }
);
    
//添加一个腾讯地图手势监听接口
tencenMap.addTencentMapGestureListener(TencentMapGestureListener listner); 

//移除一个腾讯地图手势监听接口
tencenMap.removeTencentMapGestureListener(TencentMapGestureListener listner);
```
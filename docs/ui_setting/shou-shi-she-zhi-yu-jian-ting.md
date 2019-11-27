## 手势设置与监听

* 滚动手势

可以用手指拖动地图四处滚动（平移）或用手指滑动地图（动画效果），也可以禁用或开启平移（滑动）手势。

```java
mUiSettings.setScrollGesturesEnabled(boolean flag); // 设置是否开启地图滚动手势

mUiSettings.isScrollGesturesEnabled(); // 获取当前是否支持滚动手势
```

* 缩放手势

缩放手势可改变地图的缩放级别：双击地图可以使缩放级别增加1 \(放大\)；两个手指捏/拉伸也可以控制缩放级别。

禁用缩放手势不会影响用户使用地图上的缩放控制按钮。

```java
mUiSettings.setZoomGesturesEnabled(boolean flag); // 设置是否开启地图缩放手势

mUiSettings.isZoomGesturesEnabled(); // 获取当前是否支持缩放手势
```

* 倾斜手势

通过两个手指的移动，控制地图的倾斜角。

```java
mUiSettings.setTiltGesturesEnabled(boolean flag); // 设置是否开启地图倾斜手势

mUiSettings.isTiltGesturesEnabled(); // 获取当前是否支持倾斜手势
```

* 旋转手势

通过两个手指控制，旋转3D地图。

```java
mUiSettings.setRotateGesturesEnabled(boolean flag); // 设置是否开启地图旋转手势

mUiSettings.isRotateGesturesEnabled(); // 获取当前是否支持旋转手势
```

* 所有手势

包括滚动手势、缩放手势、倾斜手势、旋转手势，可同时设置手势的开启或禁止。

```java
mUiSettings.setAllGesturesEnabled(boolean flag); // 设置是否开启地图所有手势
```

* 手势监听

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




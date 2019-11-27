### 点标注

点标注可用来标注地图上的位置，如用户位置、店铺位置。地图SDK提供的点标注功能包括两部分：标注点位置的 Marker 和显示位置信息的 InfoWindow ，同时提供了 Marker 的点击、拖拽事件监听和 InfoWindow 的点击监听。

#### 添加 Marker 及 InfoWindow
----

添加Marker可以在地图上标注指定的点，MarkerOption是设置Marker参数的类。添加Marker的代码如下：

```java
LatLng latlng_disanji = new LatLng(39.984253,116.307439);
final Marker marker = tencentMap.addMarker(new MarkerOptions()
            // 标注的位置
            .position(latlng_disanji)
            // 标注的InfoWindow的标题
            .title("第三极大厦")
            //标注的InfoWindow的内容
            .snippet("地址: 北京市北四环西路66号")
            // 标注的锚点，取值为[0.0 ~ 1.0]
            .anchor(0.5f, 0.5f)
    );

// 显示信息窗，也可以通过点击Marker图标设置显示或隐藏
marker.showInfoWindow();

// 把地图移动到指定经纬度
tencentMap.moveCamera(CameraUpdateFactory.newLatLng(latlng_disanji));
```

以上代码绘制的 Marker 及 InfoWindow 效果图如下：

![第三极](/overlays/images/default_marker.jpg)

Marker 常用属性如下：

| 属性 | 说明 |
| :--- | :--- |
| position | 标注的位置 |
| title | 标注的InfoWindow的标题，如果设置了InfoWindowAdapter则失效 |
| snippet | 标注的InfoWindow的内容 |
| icon | 设置标注的样式 |
| anchor | 标注的锚点 |
| draggable | 标注是否可以被拖动 |
| visible | 标注是否可见 |
| rotation | 标注的旋转角度 |
| alpha | 标注的透明度 |
| zIndex | 标注的层级关系 |
| indoorInfo | 为Marker添加室内图信息（buildingId，floorName\), 当添加室内图信息后，Marker将会随着绑定的室内图状态改变而展示和隐藏 |
| infoWindowEnable | 标注是否可以弹出InfoWindow |
| flat | 是不是3D标注，3D标注会随着地图倾斜面倾斜 |
| displayLevel | 显示级别：0 不参与避让，1-65535 参与避让且1的级别最高 |
| autoOverturnInfoWindow | 该marker对应的气泡是否实现自动翻转 |
| avoidAnnocation | 是否避让底图文字 |
| clockwise | 旋转角度是否沿顺时针方向 |
| infoWindowAnchor | 设置infowindow anchor point |
| fastLoad | 是否允许 marker 快速加载 |

#### 自定义Marker及InfoWindow
----

用户可以在地图的指定位置添加自定义的 Marker，并设置自定义信息窗。InfoWindow 信息窗是显示当前标注信息的控件，SDK 提供的 InfoWindow 只能显示 Marker 的两条信息：title 和 snippet，如果用户对信息窗的样式或者显示方式有其他要求，可以自定义信息窗。

首先在 layout 中创建 custom\_infowindow.XML 文件定义自己的信息窗样式

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/infowindow_background"> <!--背景资源可以自己设置-->

<!--用于展示自定义信息窗的内容-->
<TextView
    android:id="@+id/tv_title"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="custom_infowindow:"
    android:layout_gravity="center_horizontal"
    android:textColor="#ff000000"
    android:textSize="15sp"/>

</LinearLayout>
```

然后创建一个新Marker并展示自定义InfoWindow（以MainActivity为例）：

```java
// 添加Marker并自定义图标
LatLng latlng_disanji = new LatLng(39.984253,116.307439);
final Marker marker = tencentMap.addMarker(new MarkerOptions()
        // 标注的位置
        .position(latlng_disanji)
        // 标注的InfoWindow的标题
        .title("第三极大厦")
        // 标注的InfoWindow的内容
        .snippet("地址: 北京市北四环西路66号")
        // 标注的锚点，取值为[0.0 ~ 1.0]
        .anchor(0.5f, 0.5f)
        // 设置自定义Marker图标
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
);

// 显示信息窗
marker.showInfoWindow();

// 把地图移动到指定经纬度
tencentMap.moveCamera(CameraUpdateFactory.newLatLng(latlng_disanji));

// 自定义信息窗
TencentMap.InfoWindowAdapter infoWindowAdapter = new TencentMap.InfoWindowAdapter() {

    TextView tvTitle;
 
    // 返回View为信息窗自定义样式，返回null时为默认信息窗样式
    @Override
    public View getInfoWindow(final Marker arg0) {
        // TODO Auto-generated method stub
        if (arg0.equals(marker)) {
            LinearLayout custInfowindow = (LinearLayout) View.inflate(
                    MainActivity.this, R.layout.custom_infowindow, null);
            tvTitle = (TextView)custInfowindow.findViewById(R.id.tv_title);
            // 设置自定义信息窗的内容
            tvTitle.setText("custom_infowindow:");
            tvTitle.append("\n" + arg0.getTitle());
            tvTitle.append("\n" + arg0.getSnippet());
            return custInfowindow;
        }
        return null;
    }

    // 返回View为信息窗内容自定义样式，返回null时为默认信息窗样式
    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }
};

// 设置信息窗适配器
tencentMap.setInfoWindowAdapter(infoWindowAdapter);
```

以上代码设置的自定义Marker和InfoWindow的效果如下图所示：

![第三极](/overlays/images/custom_marker.jpg)

也可以对已经添加的Marker设置自定义图标，代码如下：

```java
// 设置marker的显示样式，参数为纹理样式
marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)); 
```

另外，InfoWindow也提供一些设置操作，如下：

```java
// 点击地图其他区域时，infowindow是否需要隐藏
tencentMap.setOnTapMapViewInfoWindowHidden(boolean hidden); 

// 设置地图是否允许多InfoWindow模式，默认是false(只允许显示一个InfoWindow)。注意：此方法需要在addMarker之前调用。	
tencentMap.enableMultipleInfowindow(boolean isEnable); 
```

<span style="color:red">Tips: <br/>矢量地图 SDK 使用 OpenGL 技术渲染底图及用户添加到地图上的各种覆盖物，所以用户通过 BitmapDescriptorFactory.fromView 构造的 Marker 以及在 InfoWindowAdapter 回调中传入地图的 View 最终都会在地图展示这个 marker/infowindow 时将 view 转化为图片渲染到地图上。因此不会有 Andorid 提供的 View 的事件响应、动画等功能</span>

#### 标注的事件
----

设置Marker和InfoWindow的监听事件如下所示：

```java
// Marker点击事件
tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    }
);

// Marker拖拽事件
tencentMap.setOnMarkerDragListener(new TencentMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
            
        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {

        }
    }
);

// InfoWindow点击事件
tencentMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {

        }

        @Override
        public void onInfoWindowClickLocation(int i, int i1, int i2, int i3) {

        }
    }
);
```

#### 移除标注
----

从地图上移除Marker标注的示例代码如下：

```java
marker.remove();
```
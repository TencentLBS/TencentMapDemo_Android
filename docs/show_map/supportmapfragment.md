### SupportMapFragment

SDK 还提供了 SupportMapFragment 这个类来加载地图，这个类继承自 SupportFragment 内部封装了 MapView 帮助用户管理了 MapView 的生命周期，推荐大家使用。这里简单说明一下 SupportMapFragment 的使用方法。

__1、在activity\_main.xml中添加如下代码：__

```xml
<fragment
    android:id="@+id/map_frag"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
class="com.tencent.tencentmap.mapsdk.maps.SupportMapFragment"/>
```

__2、在需要加载地图的Acticity中实例化地图对象，该Acitvity需要继承FragmentAcitivity来管理SupportMapFragment。以MainActicity为例，示例代码如下：__

```java
public class MainActivity extends FragmentActivity {

    private TencentMap tencentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.map_frag);
        tencentMap = mapFragment.getMap();
    }
}
```

运行工程就可以在您的APP中看到腾讯地图了，效果如下图所示：

![show map](/show_map/images/mapView.png)


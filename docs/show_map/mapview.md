### MapView

一个用于展示地图的 View，它负责从服务端获取地图数据，负责捕捉屏幕触控手势事件。使用这个类必须按照它的生命周期进行操控，你必须参照以下方法 `onCreate(Bundle)`、 `onStart()`、 `onResume()`、 `onPause()`、 `onStop()`、 `onDestroy()` 等声明周期函数。

用 MapView 加载地图的方法就像 Android 提供的其他 View 一样，具体的使用步骤如下：

__1、对需要加载地图的Acticity相应的\*.xml文件中添加代码，以MainActicity为例，在activity\_main.xml中添加如下代码：__

```xml
<com.tencent.tencentmap.mapsdk.maps.MapView
    android:id="@+id/map"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

__2、在需要加载地图的Acticity中添加如下代码，其中包括地图对象的实例化，由于SDK并没有提供用于管理地图生命周期的Activity，因此需要用户继承Activity后管理地图的生命周期，防止内存泄露。以MainActicity为例，示例代码如下：__

```java
public class MainActivity extends Activity {

    private MapView mapView;
    private TencentMap tencentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView)findViewById(R.id.map_view);
        tencentMap = mapView.getMap();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        mapView.onRestart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
    }
}
```
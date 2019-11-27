### 设置地图类型

腾讯地图SDK提供5种类型的基本地图，具体如下：

| 地图样式 \(styleType\) | 说明 |
| :--- | :--- |
| MAP\_TYPE\_NONE | 空白地图 |
| MAP\_TYPE\_NORMAL | 普通地图 |
| MAP\_TYPE\_SATELLITE | 卫星图 |
| MAP\_TYPE\_NAVI | 导航模式 |
| MAP\_TYPE\_NIGHT | 夜间模式 |

#### 1、普通地图与实时路况：

普通地图的信息包括路网信息、建筑物及重要的自然景观；腾讯地图还提供了实时路况图层，可以为提供实时交通数据的城市展示实时交通状况，设置显示普通地图和实时路况的示代码如下：

```java
tencentMap.setMapStyle(TencentMap.MAP_TYPE_NORMAL); // 显示普通地图
tencentMap.setTrafficEnabled(true); // 参数为true表示开启实时路况，参数为false表示关闭
```

#### 2、卫星图：

卫星地图有两种设置方式，代码如下：

```java
tencentMap.setSatelliteEnabled(true); // 开启卫星图
或
tencentMap.setMapStyle(TencentMap.MAP_TYPE_SATELLITE); // 直接设置卫星地图
```

实时路况和卫星图的效果如下所示：

![实时路况](/show_map/images/traffic.png)
![卫星图](/show_map/images/satellite.png)
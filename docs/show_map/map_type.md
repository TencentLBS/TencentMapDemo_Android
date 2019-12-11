### 设置地图类型

腾讯地图SDK提供3种类型的基本地图，具体如下：

| 地图类型 \(mapType\) | 说明 |
| :--- | :--- |
| MAP\_TYPE\_NORMAL | 普通地图，只有在这个类型下，TencentMap.setMapStyle(int styleType) 才能设置生效 |
| MAP\_TYPE\_SATELLITE | 卫星图 |
| MAP\_TYPE\_DARK | 暗色模式 |

#### 1、普通地图与实时路况：

普通地图的信息包括路网信息、建筑物及重要的自然景观；腾讯地图还提供了实时路况图层，可以为提供实时交通数据的城市展示实时交通状况，设置显示普通地图和实时路况的示代码如下：

```java
// 显示普通地图
tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
// 参数为true表示开启实时路况，参数为false表示关闭
tencentMap.setTrafficEnabled(true);
```

#### 2、卫星图：

卫星地图有两种设置方式，代码如下：

```java
// 直接设置卫星地图
tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
```

实时路况和卫星图的效果如下所示：

![实时路况](/show_map/images/traffic.png)
![卫星图](/show_map/images/satellite.png)
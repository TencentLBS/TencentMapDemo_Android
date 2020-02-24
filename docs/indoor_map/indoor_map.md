### 室内图

腾讯地图 SDK 支持室内图功能，包括室内图的展示、带有室内属性的 marker、polyline 等地图元素室内图功能为付费功能，用户可以参考[腾讯地图室内图官网](https://lbs.qq.com/lbsindoor/home/index.html)了解详情。

#### 展示室内图
----

1. 已经申请key的用户需要确认是否已申请室内图权限，如果没有权限，请联系[腾讯地图商务](https://lbs.qq.com/contractus.html)并确保已经将带有室内图权限的 key 填入 Manifest

    ```xml
    <meta-data
        android:name="TencentMapSDK"
        android:value="有室内图权限的 key"/>
    ```

2. 室内图默认不展示，需要手动调用开关

    ```java
    tencentMap.setIndoorEnabled(true);
    ```

    以欧美汇为例，如下图所示：
    ![](/indoor_map/images/indoor.png)
    当室内图打开时，默认会展示室内图楼层控件，用户也可以按照自己的需求展示/隐藏室内图楼层控件：

    ```java
    uisettings.setIndoorLevelPickerEnabled(false);
    ```

    ![](/indoor_map/images/indoor_without_controller.png)

3. 设置展示室内图的建筑的楼层

| 方法 | 说明 |
| :--- | :--- |
| TencentMap.setIndoorFloor\(String buildingId, String floorName\) | 将buildingId对应的室内图建筑物设置到指定楼层展示。此接口一般用于初始化地图时使用，当用户已知地图初始化视野中含有室内图并知道当前室内图 buildingId 及 floorName 时可以调用此接口初始化地图的展示状态。需注意的是，此接口只会对一个建筑生效，如果多次设置只有最后一次的设置会生效。 |
| Tencentmap.setIndoorFloor\(int floorId\) | 设置当前激活状态室内图Building的选中的楼层 |

目前 buildingId 和 floorName 可以从室内图的相关回调获取

#### 室内图回调
----

用户可以通过腾讯地图 sdk 提供的室内图变化回调获取当前展示的室内图的相关信息，包括当前激活的建筑物，建筑物的详细信息，以及当前不再激活的建筑物。当前屏幕内有可能有多个室内图，但仅会有一栋处于屏幕中心区域的建筑物处于“激活”态。

| 方法 | 说明 |
| :--- | :--- |
| TencentMap.setOnIndoorStateChangeListener\(OnIndoorStateChangeListener listener\) | 设置室内图状态回调 |
| OnIndoorStateChangeListener.onIndoorBuildingFocused\(\) | 室内图场景激活回调，当前视野已经显示出室内图 |
| OnIndoorStateChangeListener.onIndoorLevelActivated\(IndoorBuilding building\) | 室内图楼层状态激活和变化回调 |
| OnIndoorStateChangeListener.onIndoorBuildingDeactivated\(\) | 当前室内图处于无效状态 |

如果需要修改当前激活的建筑楼层可以通过以下接口设置：

| 方法 | 说明 |
| :--- | :--- |
| TencentMap.setIndoorFloor\(int floorId\) | 设置当前激活的室内图楼层 |



#### 带室内属性的地图元素
----

腾讯地图提供了带室内楼层属性的元素，目前包括 marker、polyline。在增加室内属性以后，该marker或polyline仅在该建筑物的该楼层展示，以 marker 为例说明设置室内属性的方法和展示效果：

```java
tencentMap.setIndoorEnabled(true);
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoo(new LatLng(39.865105,116.378345), 18));
tencentMap.addMarker(new MarkerOptions(new LatLn(39.979708, 116.313486))
        // *******需要更换为当前建筑的builidingid
        .indoorInfo(new IndoorInfo("**********", "F3")));
```
![](/indoor_map/images/marker.png)
![](/indoor_map/images/no_marker.png)

如图所示，添加的 marker 只在欧美汇的 F3 层展示

#### 获取室内 poi 的属性
----

腾讯地图的室内图 poi 带有其所属的室内图信息，获取 poi 室内属性（包括poi的名称以及poi的楼层信息等）的方法：

```java
tencentMap.setOnMapPoiClickListener(newTencentMap.OnMapPoiClickListener() {
    @Override
    public void onClicked(MapPoi poi) {
        if (poi instanceof IndoorMapPoi) {
            IndoorMapPoi indoorMapPoi = (IndoorMapPoi) poi;
            Log.e("", "building id:" + indoorMapPoi.buildingId
                    + ", building name:" + indoorMapPoi.buildingName
                    + ", floor name:" + indoorMapPoi.floorName);
        }
    }
});
```

#### 自定义楼层控件
----

开发者可以通过室内图状态的回调信息定制自己的楼层控件，定制前，先隐藏默认的楼层控件，然后基于地图回调的室内数据，使用原生view自行研发楼层控件。

自定义的步骤：

1. 隐藏默认楼层控件

   ```java
   uisettings.setIndoorLevelPickerEnabled(false);
   ```

2. 通过OnIndoorStateChangeListener回调中获取的信息，来实现建筑物的状态展示，和楼层信息的实时切换。

   ```java
   Tencentmap:
   /**
    * 室内图回调
    * com.tencent.tencentmap.mapsdk.maps.TencentMap#setOnIndoorStateChangeListener(OnIndoorStateChangeListener listener)}
    */
   public TestOnIndoorStateChangeListener implements OnIndoorStateChangeListener{
       /**
        * 室内图场景激活回调，当前视野已经显示出室内图
        * @return
        */
       boolean onIndoorBuildingFocused(){
       	//TODO：在此处显示开发者自定义的楼层控件
       }
       /**
        * 室内图楼层状态激活和变化回调
        * @param building，室内图对象
        * @return
        */
       boolean onIndoorLevelActivated(IndoorBuilding building){
       	//TODO：在此处获取到当前激活的建筑物的楼层列表，如（B4，B3... F1，F2，F3）以及当前激活的楼层,根据以上信息填充楼层控件的数据

       }
       /**
        * 当前室内图处于无效状态
        */
       boolean onIndoorBuildingDeactivated(){
       	//TODO：隐藏自定义的楼层控件
       }
   }
   ```
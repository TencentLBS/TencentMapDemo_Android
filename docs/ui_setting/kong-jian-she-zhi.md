## 控件设置

* 缩放按钮

可以控制地图的缩放级别，每次点击改变1个级别，开启后此控件显示在右下角，设置方式及效果图如下所示：

```java
mUiSettings.setZoomControlsEnabled(boolean flag); // 设置是否显示缩放按钮，true表示显示，false表示不显示

mUiSettings.isZoomControlsEnabled(); // 获取当前是否显示缩放控件
```

[![](https://camo.githubusercontent.com/7225424a3eb53ee493b7f8c9718b993f4cc561df/68747470733a2f2f692e696d6775722e636f6d2f696a57397358432e6a7067)](https://camo.githubusercontent.com/7225424a3eb53ee493b7f8c9718b993f4cc561df/68747470733a2f2f692e696d6775722e636f6d2f696a57397358432e6a7067)

* 指南针

可以指示地图的南北方向，默认的视图状态下不显示，只有在地图的偏航角或俯仰角不为0时才会显示，并且该控件的默认点击事件会将地图视图的俯仰角和偏航角动画到0的位置。此控件显示在左上角，设置方式及效果图如下所示：

```java
mUiSettings.setCompassEnabled(boolean flag); // 设置是否显示罗盘

mUiSettings.isCompassEnabled(); // 获取当前是否开启罗盘控件

//地图罗盘点击事件
mUiSettings.setCompassEnabled(true); // 设置是否显示罗盘
    tencentMap.setOnCompassClickedListener(new TencentMap.OnCompassClickedListener() {
        @Override
        public void onCompassClicked() {

        }
    }
);
```

[![](https://camo.githubusercontent.com/33779961427700511e4e24988c90fba87a8d14f0/68747470733a2f2f692e696d6775722e636f6d2f6662436f5a6c312e6a7067)](https://camo.githubusercontent.com/33779961427700511e4e24988c90fba87a8d14f0/68747470733a2f2f692e696d6775722e636f6d2f6662436f5a6c312e6a7067)

罗盘的填边设置请参考“接口文档”。

* 定位控件

当通过`TencentMap.setLocationSource(locationSource)`设置好地图的定位源后，点击此按钮可以在地图上标注一个蓝点指示用户的当前位置。定位控件显示在右下角，设置方式和效果图如下所示：

```java
mUiSettings.setMyLocationButtonEnabled(boolean flag); // 设置是否显示定位按钮

mUiSettings.isMyLocationButtonEnabled(); // 获取当前是否显示定位控件
```

[![](https://camo.githubusercontent.com/08b47d10896e82c820b381f4e74e96a7a2705c4f/68747470733a2f2f692e696d6775722e636f6d2f7350574b3276472e6a7067)](https://camo.githubusercontent.com/08b47d10896e82c820b381f4e74e96a7a2705c4f/68747470733a2f2f692e696d6775722e636f6d2f7350574b3276472e6a7067)

* 比例尺控件

默认显示在左下角，也可以通过参数进行设置，设置方式和效果图如下所示：

```
mUiSettings.setScaleViewEnabled(true); // 设置当前是否显示比例尺

mUiSettings.isScaleViewEnabled(); // 获取当前是否显示比例尺

mUiSettings.setScaleViewPosition(int position); // 设置比例尺的显示位置

参数position：
TencentMapOptions.SCALEVIEW_POSITION_BOTTOM_CENTER
TencentMapOptions.SCALEVIEW_POSITION_BOTTOM_LEFT
TencentMapOptions.SCALEVIEW_POSITION_BOTTOM_RIGHT
TencentMapOptions.SCALEVIEW_POSITION_TOP_CENTER
TencentMapOptions.SCALEVIEW_POSITION_TOP_LEFT
TencentMapOptions.SCALEVIEW_POSITION_TOP_RIGHT
```

[![](https://camo.githubusercontent.com/e4ab71eaedfd266b5a8ee24fca7b5eb2371b1160/68747470733a2f2f692e696d6775722e636f6d2f426f54326d514b2e6a7067)](https://camo.githubusercontent.com/e4ab71eaedfd266b5a8ee24fca7b5eb2371b1160/68747470733a2f2f692e696d6775722e636f6d2f426f54326d514b2e6a7067)

* 地图Logo

腾讯地图logo默认显示在右下角，改变Logo位置的示例代码如下：

```
mUiSettings.setLogoPosition(int logoAnchor); // 设置Logo的显示位置

参数logoAnchor：
TencentMapOptions.LOGO_POSITION_BOTTOM_CENTER
TencentMapOptions.LOGO_POSITION_BOTTOM_LEFT
TencentMapOptions.LOGO_POSITION_BOTTOM_RIGHT
TencentMapOptions.LOGO_POSITION_TOP_CENTER
TencentMapOptions.LOGO_POSITION_TOP_LEFT
TencentMapOptions.LOGO_POSITION_TOP_RIGHT
```

[![](https://camo.githubusercontent.com/1573eaeb9a3fafe8323f2709e62dfd78fd617cdc/68747470733a2f2f692e696d6775722e636f6d2f7449756a4d67662e6a7067)](https://camo.githubusercontent.com/1573eaeb9a3fafe8323f2709e62dfd78fd617cdc/68747470733a2f2f692e696d6775722e636f6d2f7449756a4d67662e6a7067)

注意：使用腾讯地图SDK要求显示logo，如上图所示。如有特殊需求可通过`setLogoPositionWithMargin()`方法设置偏移，示例代码如下：

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
mUiSettings.setLogoPositionWithMargin(int position, int top, int bottom, int left, int right); 

```

或：

```java
// 根据地图四个Anchor 模式（左上、左下、右下、右上）以及相应的Margins来设置Logo位置
mUiSettings.setLogoPosition(int logoAnchor , int[] marginParams); 

说明：
1、若logoAnchor为 TencentMapOptions.LOGO_POSITION_BOTTOM_LEFT,则Logo的 bottomMargin 为 marginParams[0], leftMargin 为 marginParams[1]
2、若logoAnchor为 TencentMapOptions.LOGO_POSITION_BOTTOM_RIGHT,则Logo的 bottomMargin 为 marginParams[0], rightMargin 为 marginParams[1]
3、若logoAnchor为 TencentMapOptions.LOGO_POSITION_TOP_RIGHT,则Logo的 topMargin 为 marginParams[0], rightMargin 为 marginParams[1]
4、若logoAnchor为 TencentMapOptions.LOGO_POSITION_TOP_LEFT ，则Logo 的 topMargin 为 marginParams[0], leftMargin 为 marginParams[1]
```




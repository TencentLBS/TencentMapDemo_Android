### 折线

线由一组经纬度点按照一定的顺序连接而成，在地图上绘制折线由Polyline类定义实现。

折线的实例化需要一个PolylienOptions对象，并向其中添加坐标点，添加坐标点的方式可以是每次添加一个点，也可以直接添加一个坐标点集合；PolylineOptions类负责折线的属性操作，如折线的颜色、宽度、折叠顺序、可见性、透明度、虚线样式设置、分段颜色设置、自定义纹理设置等，另外可以通过Polyline对象设置或修改折线的属性。

#### 绘制折线
----

添加折线的同时可以设置线的颜色、宽度等属性信息，示例代码如下：

```java
//经纬度
List<LatLng> latLngs = new ArrayList<LatLng>();
latLngs.add(new LatLng(39.999391,116.135972));
latLngs.add(new LatLng(39.898323,116.057694));
latLngs.add(new LatLng(39.900430,116.265061));
latLngs.add(new LatLng(39.955192,116.140092));

//绘制折线
Polyline polyline = tencentMap.addPolyline(new PolylineOptions().
         addAll(latLngs).
         // 折线的颜色为绿色
         color(0xff00ff00).
         // 折线宽度为5像素
         width(5f));

// 将地图视野移动到折线所在区域(指定西南坐标和东北坐标)，设置四周填充的像素
LatLngBounds latLngBounds = new LatLngBounds(new LatLng(39.800000,116.000000), new LatLng(40.000000,116.300000));
tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
```

上面绘制的折线有四个顶点，折线颜色为绿色，折线宽度为5像素，效果如下图所示：

![绿线](/overlays/images/green_line.jpg)

#### 绘制虚线
----

可以通过PolylienOptions对象的属性直接绘制虚线：

```java
// 绘制虚线
List<Integer> pattern = new ArrayList<>();
pattern.add(60);
pattern.add(30);
Polyline polyline = tencentMap.addPolyline(new PolylineOptions().
         addAll(latLngs).
         color(0xff00ff00).
         width(5f).
         // 设置虚线
         pattern(pattern));

// 将地图视野移动到折线所在区域，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.999391, 116.135972), 10));
```

也可以在普通折线polyline的基础上设置虚线的样式：

```java
// 设置虚线
List<Integer> pattern = new ArrayList<>();
pattern.add(60);
pattern.add(30);
polyline.pattern(pattern);  
```

参数pattern的元素数量必须是偶数个，每对元素分别表示虚线中实线区域的长度，以及空白区域的长度（单位px\)。绘制的虚线效果如下图所示：

![虚线](/overlays/images/dash_line.jpg)

#### 设置自定义纹理
----

对普通折线设置自定义纹理的代码和效果如下：

```java
Polyline polyline = tencentMap.addPolyline(new PolylineOptions().addAll(latLngs));
// 参数为线纹理的文件名，文件要放在assets目录下。
polyline.setColorTexture("color_texture_hello");

// 参数为线颜色纹理，图片要放在assets目录下。
polyline.setColorTexture("color_texture2.png"); 
```
![红纹理线](/overlays/images/red_texture_line.jpg)
![灰纹理线](/overlays/images/grey_texture_line.jpg)

设置线路绘制类型为点，并设置自定义纹理的代码和效果如下：

```java
// 设置线路绘制类型为点，自定义点的纹理
Polyline polyline = tencentMap.addPolyline(new PolylineOptions()
         .addAll(latLngs)
         // 线路绘制类型
         .lineType(PolylineOptions.LineType.LINE_TYPE_DOTTEDLINE)
);
// 设置点的自定义纹理，图片要放在assets目录下。
polyline.setColorTexture("color_point_texture.png");

// 将地图视野移动到折线所在区域，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.999391, 116.135972), 10));
```

![灰纹理线](/overlays/images/dot_line.jpg)

#### 设置线的分段颜色
----

可以通过PolylienOptions对象的属性直接设置分段颜色，示例代码（也包含了其他属性的设置）如下：

```java
PolylineOptions options = new PolylineOptions();
// 设置透明度
options.alpha(1.0f);
// 设置经纬度
options.setLatLngs(latLngs);
// 设置路线线宽
options.width(12);
// 设置路线是否显示半圆端点
options.lineCap(false);
int[] indexes = {0, latLngs.size()/3, latLngs.size()*2/3};
int[] colors = {PolylineOptions.Colors.DARK_BLUE, PolylineOptions.Colors.GREEN, PolylineOptions.Colors.YELLOW};
// 设置线的分段颜色
options.colors(colors, indexes);
polyline = tencentMap.addPolyline(options);

// 将地图视野移动到折线所在区域，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.999391, 116.135972), 10));
```

也可以在普通折线的基础上设置线的分段颜色：

```java
int[] indexes = {0, polyline.getPoints().size()/3, polyline.getPoints().size()*2/3};
int[] colors = {PolylineOptions.Colors.DARK_BLUE, PolylineOptions.Colors.GREEN, PolylineOptions.Colors.YELLOW};
polyline.setColors(colors, indexes); 

// 注意：当使用 setColorTexture(String) 设置了自定义纹理，使用纹理线时，指定使用纹理的第几像素行来绘制线
```

参数 colors 为颜色数组，参数indexs为颜色数组对应的顶点索引数组。设置线的分段颜色效果如下图所示：

![彩虹蚯蚓](/overlays/images/color_segment_line.jpg)

#### 添加折线动画

可以为折线添加动画，腾讯地图SDK目前支持两种动画类型，AlphaAnimation和EmergeAnimation。AlphaAnimation 支持从一个Alpha（透明度）到另一个Alpha的渐变，取值范围为\[0.0 ~ 1.0\]；

EmergeAnimation 从一点开始然后向两端扩展，当然也可以设置为起点或是终点，如果设置的点不在线上，则从线与这个点最近的那个点开始。

```java
Button button = (Button)findViewById(R.id.start_alpha_animation) ;
button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 添加折线动画
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1);
            alphaAnimation.setDuration(1500);
            alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaAnimation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    // AlphaAnimation动画结束后，显示EmergeAnimation动画
                    EmergeAnimation emergeAnimation = new EmergeAnimation(polyline.getPoints()
                            .get(polyline.getPoints().size()/2));
                    emergeAnimation.setDuration(2500);
                    emergeAnimation.setInterpolator(new DecelerateInterpolator());
                    polyline.startAnimation(emergeAnimation);
                }
            });
            if (polyline != null){
                // AlphaAnimation动画
                polyline.startAnimation(alphaAnimation);
            }
        }
    }
);
```

#### 移除折线
----

从地图移除折线：

```java
polyline.remove();
```

更多Polyline属性的设置、修改、获取操作请参考“接口文档”。

#### 添加折线文字 可以在折线上动态添加、修改文字，可以实现路名的动态显示（详见动态路名Demo）
----

```java
// 设置具体折线每条线段上要添加的文字
public PolylineOptions.Text createText() {
        List<PolylineOptions.SegmentText> segmentTexts = new ArrayList<>();
        // SegmentText的三个参数分别为起点在数组中的下标，终点在数组中的下标，以及要显示的文字
        segmentTexts.add(new PolylineOptions.SegmentText(0, 1, "苏州街"));
        segmentTexts.add(new PolylineOptions.SegmentText(1, 2, "北四环西路辅路"));
        segmentTexts.add(new PolylineOptions.SegmentText(2, 4, "彩和坊路"));
        return new PolylineOptions.Text.Builder(segmentTexts).build();
}

Polyline polyline = mTencentmap.addPolyline(
                new PolylineOptions()
                        .addAll(mPoints)
                        .color(0x22ff0000)).text(createText());
```

* 获取折线文字的样式

```java
PolylineOptions.Text text = mPolyline.getText();
```

* 修改文字样式

```java
// 设置显示优先级，可选项有HIGH或NORMAL
text.setPriority(PolylineOptions.TextPriority.HIGH);
// 设置字体大小
text.setTextSize(10);
// 设置填充颜色
text.setStrokeColor(Color.WHITE);
// 设置文字颜色
text.setTextColor(Color.BLACK);

// 重新修改折线文字样式
mPolyline.setText(text);
```

* 移除折线文字

```java
mPolyline.setText(null);
```

### 旧版热力图

以特殊高亮的形式显示访客热衷的地理区域和访客所在的地理区域的图示。开发者可以使用这一功能，将自己的数据展示在地图上给用户直观的展示效果。

添加热力图需要一个HeatOverlayOptions对象，该对象包含添加一个热力图层的参数，包括热力图节点、半径、配色方案等属性。

HeatDataNode是热力图节点，包括热点位置和热度值（HeatOverlay会根据传入的全部节点的热度值范围计算最终的颜色表现）。

添加热力图返回的是一个HeatOverlay对象，代表当前地图的热力图层。

#### 添加热力图
----

热力图需要的数据是经纬度和热力值，下面以随机模拟的数据为例，添加热力图的示例代码如下：

```java
// 添加热力图节点，每个节点包括经纬度、热力值。
ArrayList<HeatDataNode> nodes = new ArrayList<HeatDataNode>();
double lat = 39.961629;
double lng = 116.355343;
double value = 1; 
Random r = new Random();
// 添加随机的经纬度点及热力值
for (int i = 0; i < 100; i++) {
    double a = r.nextInt(10) * 1E-4;
    double b = r.nextInt(10) * 1E-4;
    double c = r.nextInt(100);
    LatLng latLng = new LatLng(lat + a, lng + b);
    nodes.add(new HeatDataNode(latLng, value + c));
}

//设置热力图参数	
HeatOverlayOptions heatOverlayOptions = new HeatOverlayOptions();
//热力图节点
heatOverlayOptions.nodes(nodes)
        .onHeatMapReadyListener(new OnHeatMapReadyListener() {
            @Override
            public void onHeatMapReady() {
                HeatMapActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "热力图数据初始化完毕",
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }
        })
        // 半径，单位是像素，这个数值越大运算量越大，默认值为18，建议设置在18-30之间
        .radius(18)
        // 配色方案
    	.colorMapper(new ColorMapper());

//在当前地图上添加热力图
HeatOverlay heatOverlay = tencentMap.addHeatOverlay(heatOverlayOptions);

//地图视野移动到热力图显示的位置，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.961629,116.355343), 18)); 
```

自定义配色方案的示例代码如下：

```java
class ColorMapper implements IColorMapper {
    @Override
    public int colorForValue(double value) {
        int alpha, red, blue, green;
        if (value > 1) {
            value = 1;
        }
        value = Math.sqrt(value);
        float a = 20000;
        red = 255;
        green = 119;
        blue = 3;
        if (value > 0.7) {
            green = 78;
            blue = 1;
        }

        if (value > 0.6) {
            alpha = (int) (a * Math.pow((value - 0.7), 3) + 240);
        } else if (value > 0.4) {
            alpha = (int) (a * Math.pow((value - 0.5), 3) + 200);
        } else if (value > 0.2) {
            alpha = (int) (a * Math.pow((value - 0.3), 3) + 160);
        } else {
            alpha = (int) (700 * value);
        }
        if (alpha > 255) {
            alpha = 255;
        }

        return Color.argb(alpha, red, green, blue);
    }
}
```

添加热力图的效果如下图所示：
![老版热力图](/tile_overlay/images/old_heatmap.jpg)

#### 热力图数据更新
----

每个地图仅支持一层热力图，当前热力图数据的更新通过已添加的，示例代码如下：

```java
// 更新热力图节点
ArrayList<HeatDataNode> nodes2 = new ArrayList<HeatDataNode>();
double lat2 = 39.961629;
double lng2 = 116.355343;
double value2 = 1;
Random r2 = new Random();
for (int i = 0; i < 100; i++) {
     double a = r2.nextInt(10) * 1E-4;
     double b = r2.nextInt(10) * 1E-4;
     double c = r2.nextInt(100);
     LatLng latLng = new LatLng(lat2 + a, lng2 + b);
     nodes2.add(new HeatDataNode(latLng, value2 + c));
}

//更新热力图
heatOverlay.updateData(nodes2);
```



#### 移除热力图
----

移除热力图的示例代码如下：

```java
heatOverlay.remove();
```
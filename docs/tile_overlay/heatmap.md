### 热力图

以特殊高亮的形式显示访客热衷的地理区域和访客所在的地理区域的图示。开发者可以使用这一功能，将自己的数据展示在地图上给用户直观的展示效果。

添加热力图需要 HeatMapTileProvider 对象。通过该对象，可以设置一个热力图层的参数，包括热力图节点、半径、配色方案等属性。 
WeightedLatLng 是热力图节点，包括经纬度和热度值。

#### 添加热力图
----

热力图需要的数据是经纬度和热力值，下面以随机模拟的数据为例，添加热力图的示例代码如下：

```java
// 默认卷积半径
private static final int ALT_HEATMAP_RADIUS = HeatMapTileProvider.DEFAULT_RADIUS;
// 默认透明度
private static final double ALT_HEATMAP_OPACITY = HeatMapTileProvider.DEFAULT_OPACITY;
// 默认渐变控制器
public static final Gradient ALT_HEATMAP_GRADIENT = HeatMapTileProvider.DEFAULT_GRADIENT;

// 热力图顶点
ArrayList<WeightedLatLng> nodes = new ArrayList<>();

// 以下示例是从data2k文件中读取提前模拟好的顶点数据
br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("data2k")));
String line;
while ((line = br.readLine()) != null) {
    String[] lines = line.split("\t");
    if (lines.length == 3) {
    	double value = Double.parseDouble(lines[2]);
        LatLng latLng = new LatLng((Double.parseDouble(lines[1])), (Double.parseDouble(lines[0])));
        nodes.add(new WeightedLatLng(latLng, value));
    }
}
// 设置热力图参数
HeatMapTileProvider mProvider = new HeatMapTileProvider.Builder()
                    //热力图节点
                    .weightedData(nodes)
                    //设置渐变控制器
                    .gradient(ALT_HEATMAP_GRADIENT)
                    //设置透明色
                    .opacity(ALT_HEATMAP_OPACITY)
                    //设置卷积半径
                    .radius(ALT_HEATMAP_RADIUS)
                    // 设置热力图准备完成回调
                    .readyListener(new HeatMapTileProvider.OnHeatMapReadyListener() {
                        @Override
                        public void onHeatMapReady() {
                            // 热力图准备完成后热力图瓦片缓存刷新
                            mHeatmapTileOverlay.clearTileCache();
                            mHeatmapTileOverlay.reload();
                        }
                    })
                    // 创建mProvider
                    .build();

// 在当前地图上添加热力图
mHeatmapTileOverlay = tencentMap.addTileOverlay(
    new TileOverlayOptions().tileProvider(mProvider));

// 地图视野移动到热力图显示的位置，指定了经纬度和缩放级别
tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
    new LatLng(39.961629,116.355343), 18));
```

添加热力图后的效果如下：
![热力图](/tile_overlay/images/heatmap.jpg)

```java
// 渐变色数组
private static final int[] CUSTOM_GRADIENT_COLORS = {
            Color.argb(0,0, 225, 225),
            Color.rgb(102, 125, 200),
            Color.rgb(255, 0, 0)
}; 
// 渐变色边界
private static final float[] CUSTOM_GRADIENT_START_POINTS = {
            0.0f, 
            0.2f, 
            1f
};

// 自定义渐变算法（参数包括渐变色数组，渐变色边界）
public static final Gradient CUSTOM_HEATMAP_GRADIENT = new Gradient(CUSTOM_GRADIENT_COLORS, CUSTOM_GRADIENT_START_POINTS);

// 添加自定义瓦片生成器
mProvider.setHeatTileGenerator(new HeatMapTileProvider.HeatTileGenerator() {
            @Override
                    public double[] generateKernel(int radius) {
                        //生成扩散矩阵,radius为半径
                        double[] kernel = new double[radius * 2 + 1];
                        for (int i = -radius; i <= radius; i++) {
                            kernel[i + radius] = Math.exp(-i * i / (2 * (radius / 2f) * (radius / 2f)));
                        }
                        return kernel;
                    }

                    @Override
                    public int[] generateColorMap(double opacity) {
                        //生成自定义渐变颜色，opacity为透明值
                        return CUSTOM_HEATMAP_GRADIENT.generateColorMap(opacity);
                    }
});
//热力图瓦片缓存刷新
mHeatmapTileOverlay.clearTileCache();
mHeatmapTileOverlay.reload();
```

自定义热力图效果如下：
![自定义热力图](/tile_overlay/images/custom_heatmap.jpg)

#### 移除热力图
----

移除热力图的示例代码如下：

```java
mHeatmapTileOverlay.clearTileCache();
```




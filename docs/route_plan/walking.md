### 路线规划

腾讯地图 SDK 提供了步行、驾车、公交三种路线规划接口，使用方法大致相同，用户可参照步行路线规划的接口使用方法获取驾车和公交路线规划。

#### 步行路线规划
----

1. 设置起点终点坐标：

    ```java
    // 起点坐标
    private LatLng fromPoint = new LatLng(39.843, 116.343);
    // 终点坐标
    private LatLng toPoint = new LatLng(39.232,116.323);
    ```

2. 构造对应路线规划的参数，此处为步行路线规划为例构造参数：

    ```java
    RoutePlanningParam param = new WalkingParam();     // 创建路线规划参数
    param = param.from(fromPoint).to(toPoint);         // 写入起终点
    TencentSearch tencentSearch = new TencentSearch(getApplicationContext()); // 构造搜索类的实例
    ```

3. 获取步行路线规划，可在回调中对返回结果WalkingResultObject进行解析获取对应路线：

    ```java
    tencentSearch.getRoutePlan(param, new HttpResponseListener() {

        /**
        * 获取回调
        * @param i
        * @param o
        */
        @Override
        public void onSuccess(int i, Object o) {


            String json = new Gson().toJson(o);
            Toast.makeText(getApplicationContext(), json,Toast.LENGTH_LONG).show();

            //根据返回结果解析

            WalkingResultObject walkingResultObject = new Gson().fromJson(json,WalkingResultObject.class);

            if (walkingResultObject == null || walkingResultObject.result == null || walkingResultObject.result.routes==null)
                return;

            // 显示其中一条步行路线
            for (WalkingResultObject.Route route : walkingResultObject.result.routes){
                List<LatLng> points = route.polyline;
                tencentMap.addPolyline(new PolylineOptions().addAll(points));
                break;
            }
        }

        @Override
        public void onFailure(int i, String s, Throwable throwable) {
            Toast.makeText(getApplicationContext(), s + new Gson().toJson(throwable),Toast.LENGTH_LONG).show();
        }
    });
    ```
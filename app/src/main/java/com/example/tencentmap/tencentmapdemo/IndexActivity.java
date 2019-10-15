package com.example.tencentmap.tencentmapdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tencentmap.tencentmapdemo.basic.HandDrawActivity;
import com.example.tencentmap.tencentmapdemo.basic.IndoorMapActivity;
import com.example.tencentmap.tencentmapdemo.camera.MapAnchorZoomActivity;
import com.example.tencentmap.tencentmapdemo.camera.MapBoundActivity;
import com.example.tencentmap.tencentmapdemo.circle.DrawCircleActivity;
import com.example.tencentmap.tencentmapdemo.gesture.GestureSettingsActivity;
import com.example.tencentmap.tencentmapdemo.gesture.MapListenActivity;
import com.example.tencentmap.tencentmapdemo.gesture.MapLongClickActivity;
import com.example.tencentmap.tencentmapdemo.location.LocationPointActivity;
import com.example.tencentmap.tencentmapdemo.basic.OverseaMapActivity;
import com.example.tencentmap.tencentmapdemo.basic.SetMapStyleActivity;
import com.example.tencentmap.tencentmapdemo.basic.SetMapTypeActivity;
import com.example.tencentmap.tencentmapdemo.basic.UiSettingsActivity;
import com.example.tencentmap.tencentmapdemo.location.LocationLayerActivity;
import com.example.tencentmap.tencentmapdemo.basic.MapViewActivity;
import com.example.tencentmap.tencentmapdemo.basic.TrafficMapActivity;
import com.example.tencentmap.tencentmapdemo.camera.MapCameraCenterActivity;
import com.example.tencentmap.tencentmapdemo.heatoverlay.DrawHeatOverlayActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerAnimation;
import com.example.tencentmap.tencentmapdemo.marker.MarkerClickActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerClusterActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerDragActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerInfoWindowActivity;
import com.example.tencentmap.tencentmapdemo.poi.PoiClickActivity;
import com.example.tencentmap.tencentmapdemo.polygon.DrawPolygonActivity;
import com.example.tencentmap.tencentmapdemo.polyline.DrawLineActivity;
import com.example.tencentmap.tencentmapdemo.polyline.LineTextActivity;
import com.example.tencentmap.tencentmapdemo.search.DistrictActivity;
import com.example.tencentmap.tencentmapdemo.search.DrivingRouteActivity;
import com.example.tencentmap.tencentmapdemo.search.GeoCoderActivity;
import com.example.tencentmap.tencentmapdemo.search.SearchBasicActivity;
import com.example.tencentmap.tencentmapdemo.search.WalkingRouteActivity;
import com.example.tencentmap.tencentmapdemo.transaction.AnimateCameraActivity;
import com.example.tencentmap.tencentmapdemo.transaction.MoveCameraActivity;
import com.example.tencentmap.tencentmapdemo.transaction.RotateMapActivity;
import com.example.tencentmap.tencentmapdemo.transaction.ScollMapActivity;
import com.example.tencentmap.tencentmapdemo.transaction.ZoomCalulateActivity;
import com.example.tencentmap.tencentmapdemo.transaction.ZoomMapActivity;
import com.example.tencentmap.tencentmapdemo.utils.BitMapActivity;
import com.example.tencentmap.tencentmapdemo.utils.CoordinateActivity;
import com.example.tencentmap.tencentmapdemo.utils.SnapshotActivity;

import java.util.ArrayList;

public class IndexActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private ArrayAdapter<String> activityListAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        listView = findViewById(R.id.li_index);
        activityListAdapter = new ActivityListAdapter(this, R.layout.index_adapter, getActivityList());
        listView.setAdapter(activityListAdapter);
        listView.setOnItemClickListener(this);
    }

    /**
     * 获取activity列表
     * @return
     */
    private ArrayList<String> getActivityList(){
        ArrayList<String> activityList = new ArrayList<>();
        activityList.add("基础地图");//0
        activityList.add("地图对象");
        activityList.add("设置地图类型");
        activityList.add("个性化地图");
        activityList.add("地图定位");
        activityList.add("修改地图定位Icon");
        activityList.add("室内地图");
        activityList.add("海外地图");
        activityList.add("手绘图");



        activityList.add("地图交互");//9
        activityList.add("UI控件交互");
        activityList.add("手势交互");
        activityList.add("改变地图缩放级别");
        activityList.add("改变地图中心点");
        activityList.add("限制地图显示范围");
        activityList.add("地图截屏");
        activityList.add("POI点击");
        activityList.add("动画效果");
        activityList.add("指定锚点缩放");



        activityList.add("地图变换");//19
        activityList.add("缩放");
        activityList.add("平移");
        activityList.add("旋转");
        activityList.add("平移改变视图效果");
        activityList.add("地图视图改变监听事件");
        activityList.add("地图点击监听事件");
        activityList.add("地图长按监听事件");

        activityList.add("标注");//27
        activityList.add("标注点击事件");
        activityList.add("标注拖拽事件");
        activityList.add("标注信息窗点击事件");
        activityList.add("标注聚合");
        activityList.add("标注动画");

        activityList.add("图形绘制");//33
        activityList.add("热力图");
        activityList.add("折线");
        activityList.add("多边形");
        activityList.add("圆");
        activityList.add("Marker");


        activityList.add("导航");//39
        activityList.add("步行导航");
        activityList.add("驾车导航");

        activityList.add("检索"); //42
        activityList.add("基础检索");
        activityList.add("地址逆地址解析");
        activityList.add("行政区划");

        activityList.add("特色");//46
        activityList.add("动态路名");

        activityList.add("工具类");//48
        activityList.add("BitMap");
        activityList.add("坐标转换");
        return activityList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){

            //基础地图
            case 1:
                Intent mapViewIntent = new Intent(IndexActivity.this, MapViewActivity.class);
                startActivity(mapViewIntent);
                break;
            case 2:
                Intent mapTypeIntent = new Intent(IndexActivity.this, SetMapTypeActivity.class);
                startActivity(mapTypeIntent);
                break;
            case 3:
                Intent mapStyleIntent = new Intent(IndexActivity.this, SetMapStyleActivity.class);
                startActivity(mapStyleIntent);
                break;
            case 4:
                Intent locationIntent = new Intent(IndexActivity.this, LocationLayerActivity.class);
                startActivity(locationIntent);
                break;
            case 5:
                Intent locationPointIntent = new Intent(IndexActivity.this, LocationPointActivity.class);
                startActivity(locationPointIntent);
                break;
            case 6:
                Intent indoorMapIntent = new Intent(IndexActivity.this, IndoorMapActivity.class);
                startActivity(indoorMapIntent);
                break;
            case 7:
                Intent overseaMapIntent = new Intent(IndexActivity.this, OverseaMapActivity.class);
                startActivity(overseaMapIntent);
                break;
            case 8:
                Intent handDrawMapIntent = new Intent(IndexActivity.this, HandDrawActivity.class);
                startActivity(handDrawMapIntent);
                break;

                //地图控件
            case 10:
                Intent uiSettingIntent = new Intent(IndexActivity.this, UiSettingsActivity.class);
                startActivity(uiSettingIntent);
                break;
            case 11:
                Intent gestureSettingIntent = new Intent(IndexActivity.this, GestureSettingsActivity.class);
                startActivity(gestureSettingIntent);
                break;
            case 12:
                Intent zoomIntent = new Intent(IndexActivity.this, ZoomMapActivity.class);
                startActivity(zoomIntent);
                break;

            case 13:
                Intent mapCameraIntent = new Intent(IndexActivity.this, MapCameraCenterActivity.class);
                startActivity(mapCameraIntent);
                break;
            case 14:
                Intent boundIntent = new Intent(IndexActivity.this, MapBoundActivity.class);
                startActivity(boundIntent);
                break;
            case 15:
                Intent snapshotIntent = new Intent(IndexActivity.this, SnapshotActivity.class);
                startActivity(snapshotIntent);
                break;
            case 16:
                Intent poiClickIntent = new Intent(IndexActivity.this, PoiClickActivity.class);
                startActivity(poiClickIntent);
                break;
            case 17:
                Intent animateMapIntent = new Intent(IndexActivity.this, AnimateCameraActivity.class);
                startActivity(animateMapIntent);
                break;
            case 18:
                Intent mapAnchorIntent = new Intent(IndexActivity.this, MapAnchorZoomActivity.class);
                startActivity(mapAnchorIntent);
                break;

            //地图操作
            case 20:
                Intent zoomMapIntent = new Intent(IndexActivity.this, ZoomMapActivity.class);
                startActivity(zoomMapIntent);
                break;

            case 21:
                Intent scollIntent = new Intent(IndexActivity.this, ScollMapActivity.class);
                startActivity(scollIntent);
                break;
            case 22:
                Intent rotateIntent = new Intent(IndexActivity.this, RotateMapActivity.class);
                startActivity(rotateIntent);
                break;
            case 23:
                Intent calculateIntent = new Intent(IndexActivity.this, ZoomCalulateActivity.class);
                startActivity(calculateIntent);
                break;
            case 24:
                Intent moveMapIntent = new Intent(IndexActivity.this, MoveCameraActivity.class);
                startActivity(moveMapIntent);
                break;

            case 25:
                Intent mapClickIntent = new Intent(IndexActivity.this, MapListenActivity.class);
                startActivity(mapClickIntent);
                break;
            case 26:
                Intent mapLongClickIntent = new Intent(IndexActivity.this, MapLongClickActivity.class);
                startActivity(mapLongClickIntent);
                break;


            //Marker
            case 28:
                Intent markerClickIntent = new Intent(IndexActivity.this, MarkerClickActivity.class);
                startActivity(markerClickIntent);
                break;
            case 29:
                Intent markerDragIntent = new Intent(IndexActivity.this, MarkerDragActivity.class);
                startActivity(markerDragIntent);
                break;
            case 30:
                Intent markerInfoWindowClickIntent = new Intent(IndexActivity.this, MarkerInfoWindowActivity.class);
                startActivity(markerInfoWindowClickIntent);
                break;
            case 31:
                Intent markerClusterIntent = new Intent(IndexActivity.this, MarkerClusterActivity.class);
                startActivity(markerClusterIntent);
                break;
            case 32:
                Intent markerAnimationIntent = new Intent(IndexActivity.this, MarkerAnimation.class);
                startActivity(markerAnimationIntent);
                break;


            //绘制元素
            case 34:
                Intent heapMapIntent = new Intent(IndexActivity.this, DrawHeatOverlayActivity.class);
                startActivity(heapMapIntent);
                break;
            case 35:
                Intent lineIntent = new Intent(IndexActivity.this, DrawLineActivity.class);
                startActivity(lineIntent);
                break;
            case 36:
                Intent polygonIntent = new Intent(IndexActivity.this, DrawPolygonActivity.class);
                startActivity(polygonIntent);
                break;
            case 37:
                Intent circleIntent = new Intent(IndexActivity.this, DrawCircleActivity.class);
                startActivity(circleIntent);
                break;
            case 38:
                Intent markerIntent = new Intent(IndexActivity.this, MarkerActivity.class);
                startActivity(markerIntent);
                break;

            //导航
            case 40:
                Intent walkingRouteIntent = new Intent(IndexActivity.this, WalkingRouteActivity.class);
                startActivity(walkingRouteIntent);
                break;
            case 41:
                Intent drivingRouteIntent = new Intent(IndexActivity.this, DrivingRouteActivity.class);
                startActivity(drivingRouteIntent);
                break;

            //检索
            case 43:
                Intent searchBasicIntent = new Intent(IndexActivity.this, SearchBasicActivity.class);
                startActivity(searchBasicIntent);
                break;
            case 44:
                Intent geoCodeIntent = new Intent(IndexActivity.this, GeoCoderActivity.class);
                startActivity(geoCodeIntent);
                break;
            case 45:
                Intent districtIntent = new Intent(IndexActivity.this, DistrictActivity.class);
                startActivity(districtIntent);
                break;
            //特色
            case 47:
                Intent lineTextIntent = new Intent(IndexActivity.this, LineTextActivity.class);
                startActivity(lineTextIntent);
                break;
            //工具类
            case 49:
                Intent bitmapIntent = new Intent(IndexActivity.this, BitMapActivity.class);
                startActivity(bitmapIntent);
                break;
            case 50:
                Intent coorIntent = new Intent(IndexActivity.this, CoordinateActivity.class);
                startActivity(coorIntent);
                break;
        }
    }

    /**
     * 重写适配器
     */
    private class ActivityListAdapter extends ArrayAdapter<String>{

        private Context context;

        ActivityListAdapter(Context con, int resourceId, ArrayList<String> list){
            super(con, resourceId, list);
            context = con;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.index_adapter,null);
            }
            String name = getItem(position);
            TextView item = convertView.findViewById(R.id.tv_index);
            if(position == 0||position==9||position==19||position==27||position==33||position==39||position==42||position==46||position==48){
                item.setText(name);
            }else {
                item.setText("      "+name);
            }

            return convertView;
        }
    }
}

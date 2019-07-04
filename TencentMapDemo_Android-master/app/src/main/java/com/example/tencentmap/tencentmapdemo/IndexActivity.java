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

import com.example.tencentmap.tencentmapdemo.basic.LocationLayerActivity;
import com.example.tencentmap.tencentmapdemo.basic.MapViewActivity;
import com.example.tencentmap.tencentmapdemo.basic.SupportMapFragmentActivity;
import com.example.tencentmap.tencentmapdemo.basic.TrafficMapActivity;
import com.example.tencentmap.tencentmapdemo.circle.DrawCircleActivity;
import com.example.tencentmap.tencentmapdemo.gesture.MapCameraActivity;
import com.example.tencentmap.tencentmapdemo.gesture.MapListenActivity;
import com.example.tencentmap.tencentmapdemo.gesture.MapLongClickActivity;
import com.example.tencentmap.tencentmapdemo.heatoverlay.DrawHeatOverlayActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerAnimation;
import com.example.tencentmap.tencentmapdemo.marker.MarkerClickActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerClusterActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerDragActivity;
import com.example.tencentmap.tencentmapdemo.marker.MarkerInfoWindowActivity;
import com.example.tencentmap.tencentmapdemo.polygon.DrawPolygonActivity;
import com.example.tencentmap.tencentmapdemo.polyline.DrawLineActivity;
import com.example.tencentmap.tencentmapdemo.route.WalkingRouteActivity;
import com.example.tencentmap.tencentmapdemo.transaction.AnimateCameraActivity;
import com.example.tencentmap.tencentmapdemo.transaction.MoveCameraActivity;
import com.example.tencentmap.tencentmapdemo.transaction.RotateMapActivity;
import com.example.tencentmap.tencentmapdemo.transaction.ScollMapActivity;
import com.example.tencentmap.tencentmapdemo.transaction.ZoomCalulateActivity;
import com.example.tencentmap.tencentmapdemo.transaction.ZoomMapActivity;
import com.tencent.tencentmap.mapsdk.maps.model.IndoorMapPoi;

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
        activityList.add("地图对象");//0
        activityList.add("地图MapView");
        activityList.add("地图SupportMapFragment");
        activityList.add("视图变换");//3
        activityList.add("缩放");
        activityList.add("平移");
        activityList.add("旋转");
        activityList.add("根据点集缩放地图");
        activityList.add("平移改变视图效果");
        activityList.add("动画改变视图效果");

        activityList.add("事件监听");//10
        activityList.add("地图点击监听事件");
        activityList.add("地图长按监听事件");
        activityList.add("地图视图改变监听事件");

        activityList.add("图形绘制");//14
        activityList.add("折线");
        activityList.add("多边形");
        activityList.add("圆");

        activityList.add("标注");//18
        activityList.add("添加标注");
        activityList.add("标注点击事件");
        activityList.add("标注拖拽事件");
        activityList.add("标注信息窗点击事件");
        activityList.add("标注聚合");
        activityList.add("标注动画");

        activityList.add("地图图层");//25
        activityList.add("路况图");
        activityList.add("热力图");
        activityList.add("定位");
        activityList.add("步行规划");
        return activityList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 1:
                Intent mapViewIntent = new Intent(IndexActivity.this, MapViewActivity.class);
                startActivity(mapViewIntent);
                break;
            case 2:
                Intent supportMapIntent = new Intent(IndexActivity.this, SupportMapFragmentActivity.class);
                startActivity(supportMapIntent);
                break;
            case 4:
                Intent zoomIntent = new Intent(IndexActivity.this, ZoomMapActivity.class);
                startActivity(zoomIntent);
                break;
            case 5:
                Intent scollIntent = new Intent(IndexActivity.this, ScollMapActivity.class);
                startActivity(scollIntent);
                break;
            case 6:
                Intent rotateIntent = new Intent(IndexActivity.this, RotateMapActivity.class);
                startActivity(rotateIntent);
                break;
            case 7:
                Intent calculateIntent = new Intent(IndexActivity.this, ZoomCalulateActivity.class);
                startActivity(calculateIntent);
                break;
            case 8:
                Intent moveMapIntent = new Intent(IndexActivity.this, MoveCameraActivity.class);
                startActivity(moveMapIntent);
                break;
            case 9:
                Intent animateMapIntent = new Intent(IndexActivity.this, AnimateCameraActivity.class);
                startActivity(animateMapIntent);
                break;


            case 11:
                Intent mapClickIntent = new Intent(IndexActivity.this, MapListenActivity.class);
                startActivity(mapClickIntent);
                break;
            case 12:
                Intent mapLongClickIntent = new Intent(IndexActivity.this, MapLongClickActivity.class);
                startActivity(mapLongClickIntent);
                break;
            case 13:
                Intent mapCameraIntent = new Intent(IndexActivity.this, MapCameraActivity.class);
                startActivity(mapCameraIntent);
                break;
            case 15:
                Intent lineIntent = new Intent(IndexActivity.this, DrawLineActivity.class);
                startActivity(lineIntent);
                break;
            case 16:
                Intent polygonIntent = new Intent(IndexActivity.this, DrawPolygonActivity.class);
                startActivity(polygonIntent);
                break;
            case 17:
                Intent circleIntent = new Intent(IndexActivity.this, DrawCircleActivity.class);
                startActivity(circleIntent);
                break;
            case 19:
                Intent markerIntent = new Intent(IndexActivity.this, MarkerActivity.class);
                startActivity(markerIntent);
                break;
            case 20:
                Intent markerClickIntent = new Intent(IndexActivity.this, MarkerClickActivity.class);
                startActivity(markerClickIntent);
                break;
            case 21:
                Intent markerDragIntent = new Intent(IndexActivity.this, MarkerDragActivity.class);
                startActivity(markerDragIntent);
                break;
            case 22:
                Intent markerInfoWindowClickIntent = new Intent(IndexActivity.this, MarkerInfoWindowActivity.class);
                startActivity(markerInfoWindowClickIntent);
                break;
            case 23:
                Intent markerClusterIntent = new Intent(IndexActivity.this, MarkerClusterActivity.class);
                startActivity(markerClusterIntent);
                break;
            case 24:
                Intent markerAnimationIntent = new Intent(IndexActivity.this, MarkerAnimation.class);
                startActivity(markerAnimationIntent);
                break;
            case 26:
                Intent trafficIntent = new Intent(IndexActivity.this, TrafficMapActivity.class);
                startActivity(trafficIntent);
                break;
            case 27:
                Intent heapMapIntent = new Intent(IndexActivity.this, DrawHeatOverlayActivity.class);
                startActivity(heapMapIntent);
                break;
            case 28:
                Intent locationIntent = new Intent(IndexActivity.this, LocationLayerActivity.class);
                startActivity(locationIntent);
                break;
            case 29:
                Intent walkingRouteIntent = new Intent(IndexActivity.this, WalkingRouteActivity.class);
                startActivity(walkingRouteIntent);
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
            if(position == 0||position==3||position==10||position==18||position==25||position==14){
                item.setText(name);
            }else {
                item.setText("      "+name);
            }

            return convertView;
        }
    }
}

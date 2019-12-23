package com.tencent.map.vector.demo.marker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MarkerOptionsActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options.fastLoad(true); //设置是否快速加载
        options.icon(BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.marker))); //加载图标
        options.tag(tencentMap.addCircle(new CircleOptions().center(new LatLng(39.984059, 116.305756)))); //设置标签对象
        tencentMap.addMarker(options);
    }

    private LatLng latLng = new LatLng(39.984864, 116.305756);

    private Bitmap getBitMap(int resourceId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 100;
        int newHeight = 100;
        float widthScale = ((float)newWidth)/width;
        float heightScale = ((float)newHeight)/height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return bitmap;
    }


    private MarkerOptions options = new MarkerOptions(latLng). //坐标设置
            anchor(0.5f, 0.5f). //标注的锚点
            clockwise(true). //旋转角度顺时针
            draggable(true). //标注可被拖动
            flat(true). //设置3D标注
            infoWindowEnable(true). //弹出infowindow
            infoWindowAnchor(0.5f, 0.5f). //标注信息infowindow锚点
            rotation(90f). //旋转角度
            snippet("气泡infowindow内容"). //设置infowindow内容
            title("标题内容"). //infowindow标题内容
            visible(true). //标注是否可见
            zIndex(0); //堆叠顺序，越大优先级越高
}

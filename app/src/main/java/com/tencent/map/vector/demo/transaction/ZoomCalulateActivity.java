package com.tencent.map.vector.demo.transaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ZoomCalulateActivity extends SupportMapFragmentActivity {

    private Button btn;
    private int leftpadding = 50,rightpadding =50, toppadding = 50, bottompadding=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn = findViewById(R.id.btn_bottom);
        btn.setVisibility(View.VISIBLE);
        btn.setText("根据点集缩放地图");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(addMarker());
                tencentMap.moveCamera(cameraUpdate);
            }
        });
    }


    private CameraPosition addMarker(){
        ArrayList<LatLng> points = new ArrayList<>();
        points.add(new LatLng(39.984059,116.307621));
        points.add(new LatLng(39.984049,116.307631));
//        points.add(new LatLng(39.981527,116.308994));
//        points.add(new LatLng(39.984026,116.316419));
//        points.add(new LatLng(39.978501,116.311827));
        //根据markers计算缩放级别
        for(int i=0; i<points.size();i++){
            MarkerOptions markerOptions = new MarkerOptions().position(points.get(i)).icon(BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.marker)));
            tencentMap.addMarker(markerOptions);
        }


        CameraPosition cameraPosition = tencentMap.calculateZoomToSpanLevel(null,points,leftpadding,rightpadding,toppadding,bottompadding);
        return cameraPosition;
    }

    private Bitmap getBitMap(int resourceId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 50;
        int newHeight = 50;
        float widthScale = ((float)newWidth)/width;
        float heightScale = ((float)newHeight)/height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return bitmap;
    }
}

package com.tencent.map.vector.demo.gesture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MapLongClickActivity extends SupportMapFragmentActivity implements TencentMap.OnMapLongClickListener {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = findViewById(R.id.tv_info);
        textView.setVisibility(View.VISIBLE);
        tencentMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        String info = "经纬度："+latLng.latitude+","+latLng.longitude;
        textView.setText(info);
        setMarker(latLng);
    }

    /**
     * 长按添加标注
     */
    private void setMarker(LatLng latLng){

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.marker));
        MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        tencentMap.clear();
        tencentMap.addMarker(options);

    }


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


}

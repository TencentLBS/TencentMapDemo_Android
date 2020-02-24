package com.tencent.map.vector.demo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;


public class BitMapActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap = getBitMap(R.drawable.marker);
        BitmapDescriptorFactory.fromBitmap(bitmap);
        BitmapDescriptorFactory.fromAsset("color_texture.png");


    }

    /**
     * 获取resourse的bitmap
     * @param resourceId
     * @return
     */
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

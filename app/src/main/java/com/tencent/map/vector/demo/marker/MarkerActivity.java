package com.tencent.map.vector.demo.marker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MarkerActivity extends SupportMapFragmentActivity {
    public Marker marker;
    public Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aSwitch = findViewById(R.id.switch_map);
        aSwitch.setVisibility(View.VISIBLE);
        aSwitch.setText("添加标注");
        setMarker();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setMarker();
                }else {
                    if(marker!=null){
                        marker.remove();
                    }
                }
            }
        });

    }

    /**
     * 设置标注
     */
    private void setMarker(){
        LatLng latLng = new LatLng(39.984108,116.307557);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.marker));
        MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptor);

        //设置infowindow
        options.title("地址");
        options.snippet("中国技术交易大厦");
        marker = tencentMap.addMarker(options);
        marker.setInfoWindowEnable(true);
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

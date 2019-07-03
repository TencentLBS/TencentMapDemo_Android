package com.example.tencentmap.tencentmapdemo.heatoverlay;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tencentmap.tencentmapdemo.R;
import com.example.tencentmap.tencentmapdemo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.HeatDataNode;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlayOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;

public class DrawHeatOverlayActivity extends SupportMapFragmentActivity implements CompoundButton.OnCheckedChangeListener {


    private Switch heatControl;


    private HeatOverlay heatOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){

        heatControl = findViewById(R.id.switch_map);
        heatControl.setVisibility(View.VISIBLE);
        heatControl.setText("热力图开关");
        heatControl.setOnCheckedChangeListener(this);
        initHeatMap();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_map:
                if(isChecked){
                    //添加热力图
                    initHeatMap();
                }else {
                    //移除热力图
                    heatOverlay.remove();
                }
        }
    }

    private void initHeatMap(){
        //配置热力图参数
        HeatOverlayOptions heatOverlayOptions = new HeatOverlayOptions();
        heatOverlayOptions.nodes(getHeatDataNodes())
                .radius(18)// 半径，单位是像素，这个数值越大运算量越大，默认值为18，建议设置在18-30之间)
                .onHeatMapReadyListener(()->{
                    runOnUiThread(()-> {
                            Toast.makeText(DrawHeatOverlayActivity.this,
                                    "热力图数据准备完毕", Toast.LENGTH_SHORT).show();
                        });
                });
        heatOverlay = tencentMap.addHeatOverlay(heatOverlayOptions);

    }


    /**
     * 准备热力图数据
     * @return
     */
    private ArrayList<HeatDataNode> getHeatDataNodes(){
        //HeatDataNode 是热力图热点，包括热点位置和热度值（HeatOverlay会根据传入的全部节点的热度值范围计算最终的颜色表现）
        ArrayList<HeatDataNode> nodes = new ArrayList<>();
        nodes.add(new HeatDataNode(new LatLng(39.984108,116.307557), 86));
        nodes.add(new HeatDataNode(new LatLng(39.984208,116.307457), 44));
        nodes.add(new HeatDataNode(new LatLng(39.983208,116.307457), 64));
        nodes.add(new HeatDataNode(new LatLng(39.983208,116.317457), 54));
        nodes.add(new HeatDataNode(new LatLng(39.684308,116.317457), 34));
        nodes.add(new HeatDataNode(new LatLng(39.684108,116.307457), 24));
        nodes.add(new HeatDataNode(new LatLng(39.984408,116.327457), 44));
        nodes.add(new HeatDataNode(new LatLng(39.984508,116.307457), 54));
        nodes.add(new HeatDataNode(new LatLng(39.984208,116.307457), 14));
        nodes.add(new HeatDataNode(new LatLng(39.984298,116.307457), 64));
        nodes.add(new HeatDataNode(new LatLng(39.984208,116.327457), 74));
        nodes.add(new HeatDataNode(new LatLng(39.984108,116.317457), 84));
        nodes.add(new HeatDataNode(new LatLng(39.983208,116.297457), 65));
        nodes.add(new HeatDataNode(new LatLng(39.982208,116.307457), 54));
        nodes.add(new HeatDataNode(new LatLng(39.985208,116.297457), 69));
        return nodes;
    }

}

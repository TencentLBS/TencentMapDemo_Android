package com.example.tencentmap.tencentmapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class TrafficMapActivity extends SupportMapFragmentActivity {

    private Switch trafficControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){

        trafficControl = findViewById(R.id.switch_map);
        trafficControl.setVisibility(View.VISIBLE);
        trafficControl.setText("路况图开关");
        tencentMap.setTrafficEnabled(true);
        trafficControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //打开路况
                    tencentMap.setTrafficEnabled(true);
                }else {
                    //关闭路况
                    tencentMap.setTrafficEnabled(false);
                }
            }
        });
    }
}

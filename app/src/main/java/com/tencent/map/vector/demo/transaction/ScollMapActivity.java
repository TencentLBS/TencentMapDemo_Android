package com.tencent.map.vector.demo.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

public class ScollMapActivity extends SupportMapFragmentActivity implements View.OnClickListener {
    private static final int SCROLL_BY_PX = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = findViewById(R.id.ll);
        linearLayout.setVisibility(View.VISIBLE);
        Button scrollLeft = (Button) findViewById(R.id.scroll_left);
        scrollLeft.setOnClickListener(this);

        Button scrollRight = (Button) findViewById(R.id.scroll_right);
        scrollRight.setOnClickListener(this);

        Button scrollUp = (Button) findViewById(R.id.scroll_up);
        scrollUp.setOnClickListener(this);

        Button scrollDown = (Button) findViewById(R.id.scroll_down);
        scrollDown.setOnClickListener(this);

       /* btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.scrollBy(50f,50f);
                tencentMap.animateCamera(cameraUpdate);
            }
        });*/
    }


    // 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域

    private void changeCamera(CameraUpdate update, TencentMap.CancelableCallback callback) {
        boolean animated = ((CompoundButton) findViewById(R.id.animate))
                .isChecked();
        if (animated) {
            tencentMap.animateCamera(update, 1000, callback);
        } else {
            tencentMap.moveCamera(update);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击向左移动按钮响应事件，camera将向左边移动

            case R.id.scroll_left:
                changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_PX, 0), null);
                break;
            // 点击向右移动按钮响应事件，camera将向右边移动
            case R.id.scroll_right:
                changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_PX, 0), null);
                break;
            // 点击向上移动按钮响应事件，camera将向上边移动
            case R.id.scroll_up:
                changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_PX), null);
                break;
            // 点击向下移动按钮响应事件，camera将向下边移动
            case R.id.scroll_down:
                changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_PX), null);
                break;

            default:
                break;
        }
    }
}

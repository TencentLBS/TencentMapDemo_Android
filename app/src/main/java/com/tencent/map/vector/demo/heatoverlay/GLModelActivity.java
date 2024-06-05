package com.tencent.map.vector.demo.heatoverlay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlay;
import com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlayProvider;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.GeneralTranslateAnimator;
import com.tencent.tencentmap.mapsdk.maps.model.IAnimatorModel;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.io.File;
import java.util.Arrays;

import static com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlayProvider.CoordType.GeoGraphicType;
import static com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlayProvider.CoordType.PixelType;

public class GLModelActivity extends AppCompatActivity {

    /**
     * 和其他覆盖物相同三维模型图也可控制图层的Level和zIndex
     * 可通过 GLModelOverlayProvider的displayLevel和zIndex进行设置
     * displayLevel - 默认层级为POI之下 OverlayLevel.OverlayLevelAboveBuildings
     * 相同Level内的显示层级关系通过zIndex(int)来控制，zIndex越大越靠上显示。 Level优先级高于zIndex displayLevel必须为如下值之一，否则不生效
     */

    private MapView mapView;
    protected TencentMap tencentMap;
    private String mResourcePath;
    private GLModelOverlay vectorOverlay;
    private final String mLine = "39.98409,116.30804,39.98409,116.3081,39.98409,116.3081,39.98397,116.30809,39.9823,116.30809,39.9811,116.30817,39.9811,116.30817,39.97918,116.308266,39.97918,116.308266,39.9791,116.30827,39.9791,116.30827,39.979008,116.3083,39.978756,116.3084,39.978386,116.3086,39.977867,116.30884,39.977547,116.308914,39.976845,116.308914,39.975826,116.308945,39.975826,116.308945,39.975666,116.30901,39.975716,116.310486,39.975716,116.310486,39.975754,116.31129,39.975754,116.31129,39.975784,116.31241,39.975822,116.31327,39.97581,116.31352,39.97588,116.31591,39.97588,116.31591,39.97591,116.31735,39.97591,116.31735,39.97593,116.31815,39.975967,116.31879,39.975986,116.32034,39.976055,116.32211,39.976086,116.323395,39.976105,116.32514,39.976173,116.32631,39.976254,116.32811,39.976265,116.3288,39.976345,116.33123,39.976357,116.33198,39.976418,116.33346,39.976418,116.33346,39.97653,116.333755,39.97653,116.333755,39.978157,116.333664,39.978157,116.333664,39.978195,116.33509,39.978195,116.33509,39.978226,116.33625,39.978226,116.33625,39.97823,116.33656,39.97823,116.33656,39.978256,116.33791,39.978256,116.33791,39.978016,116.33789,39.977047,116.33791,39.977047,116.33791,39.97706,116.33768,39.97706,116.33768,39.976967,116.33706,39.976967,116.33697";
    private LatLng[] mCarLatLngArray;
    private GeneralTranslateAnimator animator;
    private Polyline polyline;
    private Spinner mSpinner;
    private String[] glmodelAnimator = new String[]{"开启平滑移动", "开启骨骼动画", "关闭骨骼动画"};
    private GLModelOverlayProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_l_model);
        mapView = findViewById(R.id.mapView);
        tencentMap = this.mapView.getMap();
        String[] linePointsStr = mLine.split(",");
        mCarLatLngArray = new LatLng[linePointsStr.length / 4];

        for (int i = 0; i < mCarLatLngArray.length; i++) {
            double latitude = Double.parseDouble(linePointsStr[i * 4]);
            double longitude = Double.parseDouble(linePointsStr[i * 4 + 1]);
            mCarLatLngArray[i] = new LatLng(latitude, longitude);
        }
        polyline = tencentMap.addPolyline(new PolylineOptions().add(mCarLatLngArray).color(R.color.colorAccent).arrow(true));
        tencentMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().include(Arrays.asList(mCarLatLngArray)).build(), 50));
        creatglmode();
        init();
    }

    private void creatglmode() {
        //gltf格式三维模型文件在assets内
        String file = "BrainStem.gltf";
        String SKELETON_PATH = "gltf-BrainStem";
        //获取外部存储
        mResourcePath = getExternalFilesDir(null).getAbsolutePath() + File.separator + SKELETON_PATH;
        provider = new GLModelOverlayProvider(
                mResourcePath + File.separator + file,
                new LatLng(39.98409, 116.30804)).coordType(PixelType)
                .pixelBounds(400, 1200).rotationX(90).rotationY(0).rotationZ(0).setClickEnable(true);
        vectorOverlay = tencentMap.addVectorOverlay(provider);
    }

    private void init() {
        mSpinner = findViewById(R.id.sp_glmode);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, glmodelAnimator);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //开启骨骼动画
                    case 0:
                        animator = new GeneralTranslateAnimator.Builder(vectorOverlay, 6000,
                                mCarLatLngArray).rotateEnabled(true).initRotate(180).modelType(GeneralTranslateAnimator.ModelType.MODEL_OVERLAY).build();
                        animator.startAnimation();
                        animator.addAnimatorEndListener(new IAnimatorModel.IAnimatorEndListener() {
                            @Override
                            public void onAnimatorEnd() {
                                Log.i("TransformActivity", "onAnimatorEnd");
                            }
                        });
                        break;
                    //停止骨骼动画
                    case 1:
                        vectorOverlay.playSkeletonAnimation(0, 1, true);
                        break;
                    //模型平移
                    case 2:
                        vectorOverlay.stopSkeletonAnimation();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * mapview的生命周期管理
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        vectorOverlay.remove();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }
}

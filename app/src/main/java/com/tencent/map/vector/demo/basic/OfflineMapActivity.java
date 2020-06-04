package com.tencent.map.vector.demo.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.sdk.comps.offlinemap.OfflineItem;
import com.tencent.map.sdk.comps.offlinemap.OfflineItemController;
import com.tencent.map.sdk.comps.offlinemap.OfflineMapComponent;
import com.tencent.map.sdk.comps.offlinemap.OfflineMapSyncedListener;
import com.tencent.map.sdk.comps.offlinemap.OfflineStatus;
import com.tencent.map.sdk.comps.offlinemap.OfflineStatusChangedListener;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;

/**
 * 离线地图下载、取消下载、删除
 */
public class OfflineMapActivity extends SupportMapFragmentActivity implements View.OnClickListener {
    private LinearLayout mLineraLayout;
    private MapView mapView;
    private Button mBtnOpen;
    private Button mBtnDownload;
    private Button mBtnStop;
    private Button mBtnDelete;
    private TextView mTvProgress;
    private OfflineItemController cityController;
    private TencentMap tencentMap;
    private OfflineMapComponent offlineMapComponent;
    private final int STATE_DOWNLOAD = 1;
    private final int STATE_COMPLETED = 2;
    private final int STATE_OPEN = 3;
    private final int STATE_CANCEL = 4;
    private String name;
    private int percentage;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_DOWNLOAD:
                    mBtnStop.setVisibility(View.VISIBLE);
                    mBtnDelete.setVisibility(View.GONE);
                    mBtnDownload.setVisibility(View.GONE);
                    mTvProgress.setVisibility(View.VISIBLE);
                    mTvProgress.setText(name + "下载进度：" + percentage + "%");
                    break;
                case STATE_COMPLETED:
                    Toast.makeText(OfflineMapActivity.this, name + "下载结束", Toast.LENGTH_LONG).show();
                    break;
                case STATE_OPEN:
                    mBtnDelete.setVisibility(View.VISIBLE);
                    mBtnStop.setVisibility(View.GONE);
                    mBtnDownload.setVisibility(View.GONE);
                    mTvProgress.setVisibility(View.GONE);
                    Toast.makeText(OfflineMapActivity.this, name+"打开离线", Toast.LENGTH_LONG).show();
                    break;
                case STATE_CANCEL:
                    mBtnStop.setVisibility(View.GONE);
                    mBtnDownload.setVisibility(View.VISIBLE);
                    mBtnDelete.setVisibility(View.GONE);
                    mTvProgress.setVisibility(View.GONE);
                    Toast.makeText(OfflineMapActivity.this, name + "停止下载", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
        mLineraLayout = findViewById(R.id.ll_offline);
        mTvProgress=findViewById(R.id.tv_progress);
        mBtnOpen=findViewById(R.id.btn_open_city);
        mBtnDownload = findViewById(R.id.btn_download_city);
        mBtnStop = findViewById(R.id.btn_stop_city);
        mBtnDelete = findViewById(R.id.btn_delete_city);
        mBtnDownload.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        TencentMapOptions options = new TencentMapOptions();
        //打开离线地图开关
        options.setOfflineMapEnable(true);
        mapView = new MapView(this, options);
        mapView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLineraLayout.addView(mapView);
        tencentMap = mapView.getMap();
        offlineMapComponent = tencentMap.getMapComponent(OfflineMapComponent.class);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_download_city:
                offlineMapComponent.syncLatestData(new OfflineMapSyncedListener() {
                    @Override
                    public void onSynced(boolean result) {
                        Log.i("TAG", "初始化同步结果：" + result);
                        if (result) {
                            downloadCity();
                        }
                    }
                });
                break;
            case R.id.btn_stop_city:
                stop();
                break;
            case R.id.btn_delete_city:
                delete();
                mBtnDownload.setVisibility(View.VISIBLE);
                mBtnStop.setVisibility(View.GONE);
                mBtnDelete.setVisibility(View.GONE);
                mTvProgress.setVisibility(View.GONE);
                break;
        }
    }
    private void downloadCity() {
        List<OfflineItem> offlineItems = offlineMapComponent.getOfflineItemList();
        for (final OfflineItem city : offlineItems) {
            if (city.getPinyin().equals("beijingshi")) {
                cityController = offlineMapComponent.getOfflineItemController(city,
                        new OfflineStatusChangedListener() {
                            @Override
                            public void onStatusChanged(OfflineItem item, OfflineStatus status) {
                                if (item == city) {
                                    name = item.getName();
                                    percentage = item.getPercentage();
                                    switch (status) {
                                        case OPEN: //开始离线
                                            Log.i("TAG", "打开离线");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mHandler.sendEmptyMessage(STATE_OPEN);
                                                }
                                            }).start();
                                            tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.897614, 116.383312), 16));
                                            break;
                                        case CLOSE: //关闭离线
                                            Log.i("TAG", "关闭离线");
                                            break;
                                        case COMPLETED: //完成下载
                                            Log.i("TAG", "下载结束");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mHandler.sendEmptyMessage(STATE_COMPLETED);
                                                }
                                            }).start();

                                            cityController.open();
                                            break;
                                        case CANCEL: //取消下载
                                            Log.i("TAG", "取消下载");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mHandler.sendEmptyMessage(STATE_CANCEL);
                                                }
                                            }).start();
                                            break;
                                        case DOWNLOADING: //下载中
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mHandler.sendEmptyMessage(STATE_DOWNLOAD);
                                                }
                                            }).start();
                                            Log.i("TAG", "下载中" + "下载进度" + item.getPercentage());
                                            break;
                                        case ERROR: //离线错误
                                            Log.i("TAG", "离线错误");
                                            break;
                                        case READY: //准备下载
                                            Log.i("TAG", "准备下载");
                                            break;
                                        case START: //开始下载
                                            Log.i("TAG", "开始下载");
                                            break;
                                    }
                                }
                            }
                        });

                if (cityController != null) {
                    boolean needDownload = cityController.checkInvalidate();
                    if (needDownload) {
                        Log.i("TAG", "有更新需下载");
                        cityController.startDownload();
                    } else {
                        Log.i("TAG", "无更新打开离线");
                        cityController.open();
                    }
                } else {
                    Log.e("TAG", "cityController 为空！");
                }

                break;
            }
        }
    }

    private void stop() {
        if (cityController != null) {
            cityController.stopDownload();
            Log.i("TAG", "停止下载");
            Toast.makeText(this, "停止下载", Toast.LENGTH_LONG).show();
        }
    }

    private void delete() {
        if (cityController != null) {
            cityController.removeCache();
            Log.i("TAG", "删除");
            Toast.makeText(this, "删除离线包", Toast.LENGTH_LONG).show();
        }
    }

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
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

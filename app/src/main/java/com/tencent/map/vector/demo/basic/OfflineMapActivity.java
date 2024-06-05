package com.tencent.map.vector.demo.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * 离线地图下载、取消下载、删除
 */
public class OfflineMapActivity extends AppCompatActivity {
    private static final String TAG = "OfflineMapActivity";
    private Button btnGoWork;
    private Button btnZoomIn;
    private Button btnZoomOut;
    private Button btnReboot;
    private Button btnSync;
    private Button btnRefresh;
    private MapView mapView;
    private RecyclerView offlineItemRecycleView;

    private TencentMap map;
    private OfflineMapComponent offlineComp;

    private OfflineAdapter offlineAdapter;

    private List<OfflineItem> offlineItems = new ArrayList<>();

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private ConnectivityManager connectivityManager;
    private TextView tvParmes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
        btnGoWork = findViewById(R.id.btn_go_work);
        btnZoomIn = findViewById(R.id.btn_zoom_in);
        btnZoomOut = findViewById(R.id.btn_zoom_out);
        btnReboot = findViewById(R.id.btn_reboot_map);
        btnSync = findViewById(R.id.btn_sync);
        btnRefresh = findViewById(R.id.btn_refresh);
        mapView = findViewById(R.id.map_view);
        tvParmes = findViewById(R.id.tv_parmes);
        offlineItemRecycleView = findViewById(R.id.offline_item_list);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        initMap();
        //重置地图
        btnReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    mapView.onPause();
                    mapView.onStop();
                    mapView.onDestroy();
                    map = null;
                    initMap();
                    mapView.onStart();
                    mapView.onResume();
                    tvParmes.setText("重置地图");
                }

            }
        });

        offlineAdapter = new OfflineAdapter(offlineItems);
        offlineItemRecycleView.setLayoutManager(new LinearLayoutManager(this));
        offlineItemRecycleView.setAdapter(offlineAdapter);
        offlineItemRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
    }

    private void initMap() {
        // 设置离线地图
        TencentMapOptions options = new TencentMapOptions();
        options.setOfflineMapEnable(true);

        map = mapView.getMap(options);

        map.addOnMapLoadedCallback(new TencentMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Toast.makeText(OfflineMapActivity.this, "地图加载完成", Toast.LENGTH_SHORT).show();
            }
        });
        //获取离线地图的城市列表
        offlineComp = map.getMapComponent(OfflineMapComponent.class);

        Log.d(TAG, "Offline enable:" + offlineComp.isOfflineMapEnable());

        // 回公司按钮
        btnGoWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(40.042893, 116.269673));
                latLngs.add(new LatLng(40.038951, 116.275241));
                LatLng center = new LatLng(40.040429, 116.273525);
                map.addMarker(new MarkerOptions(center)
                        .anchor(0.5f, 1));
                map.moveCamera(CameraUpdateFactory.newLatLngBoundsWithMapCenter(
                        new LatLngBounds.Builder()
                                .include(latLngs)
                                .build(),
                        center,
                        100));
            }
        });

        // 地图放大按钮
        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });

        // 地图缩小按钮
        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });

        // 同步离线地图信息按钮
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncData();
            }
        });

        // 刷新离线地图列表按钮
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offlineItemRecycleView.setVisibility(View.VISIBLE);
                refreshOfflineList();
            }
        });
        map.moveCamera(CameraUpdateFactory.zoomTo(5));  // 设置缩放级别,显示全国概略图.
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
        registerReceiver(networkChangeReceiver, intentFilter);

//        offlineItems.addAll(offlineComp.getOfflineItemList());
//        tryToOpenOffline();
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
    }

    private class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.OfflineViewHolder> {

        List<OfflineItem> offlineItems;

        OfflineAdapter(List<OfflineItem> offlineItems) {
            this.offlineItems = offlineItems;
        }

        @NonNull
        @Override
        public OfflineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offline
                    , parent, false);
            return new OfflineViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OfflineViewHolder holder, int position) {
            final OfflineItem offlineItem = offlineItems.get(position);

            final OfflineItemController cityController =
                    offlineComp.getOfflineItemController(offlineItem, statusChangedListener);
            if (cityController == null) {
                holder.tvTitle.setText("【controller is null】" + offlineItem.toString());
                return;
            }

            holder.tvTitle.setText(offlineItem.toString());

            holder.btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isClosed = cityController.close();
                    if (isClosed) {
                        //  toast("已关闭【" + offlineItem.getName() + "】的离线地图");
                        tvParmes.setText("已关闭【" + offlineItem.getName() + "】的离线地图");

                    }
                    Log.d(TAG, offlineItem.getName() + " close offline:" + isClosed);
                }
            });

            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadOffline(offlineItem);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isDeleted = cityController.removeCache();
                    if (isDeleted) {
                        // toast("缓存移除成功");
                        tvParmes.setText("缓存移除成功");
                    } else {
                        // toast("缓存移除失败");
                        tvParmes.setText("缓存移除失败");
                    }
                    Log.d(TAG, offlineItem.getName() + " remove cache:" + isDeleted);
                }
            });
        }

        @Override
        public int getItemCount() {
            return offlineItems.size();
        }

        class OfflineViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle;
            Button btnSwitch;
            Button btnDownload;
            Button btnDelete;

            public OfflineViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_offline_title);
                btnSwitch = itemView.findViewById(R.id.btn_switch_offline);
                btnDownload = itemView.findViewById(R.id.btn_download_offline);
                btnDelete = itemView.findViewById(R.id.btn_delete_offline);
            }
        }
    }

    OfflineStatusChangedListener statusChangedListener = new OfflineStatusChangedListener() {
        @Override
        public void onStatusChanged(OfflineItem offlineItem, OfflineStatus offlineStatus) {
            Log.d(TAG, offlineItem.getName() + " onStatusChanged: " + offlineStatus);
            final Message msg = new Message();
            msg.obj = offlineItem;
            switch (offlineStatus) {
                case OPEN: //开始离线
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.what = STATE_OPEN;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    break;
                case CLOSE: //关闭离线
                    break;
                case COMPLETED: //完成下载
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.what = STATE_COMPLETED;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    if (offlineItem.getPercentage() == 100 && offlineItem.getPinyin().equals("china2")) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(offlineItem,
                                statusChangedListener);
                        cityController.open();
                        map.moveCamera(CameraUpdateFactory.zoomTo(5));  // 设置缩放级别,显示全国概略图.
                    } else if (offlineItem.getPercentage() == 100 && offlineItem.getPinyin().equals("beijingshi")) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(offlineItem,
                                statusChangedListener);
                        cityController.open();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.909018, 116.397427), 16));
                    } else if (offlineItem.getPercentage() == 100 && offlineItem.getPinyin().equals("shanghaishi")) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(offlineItem,
                                statusChangedListener);
                        cityController.open();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.230378, 121.473658), 16));
                    } else if (offlineItem.getPercentage() == 100 && offlineItem.getPinyin().equals("tianjinshi")) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(offlineItem,
                                statusChangedListener);
                        cityController.open();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.085294, 117.201538), 16));
                    } else {
                        toast("可参考demo自行移动视野范围");
                    }
                    break;
                case CANCEL: //取消下载
                    toast("取消下载");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.what = STATE_CANCEL;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    break;
                case DOWNLOADING: //下载中
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.what = STATE_DOWNLOAD;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    break;
                case ERROR: //离线错误
                    toast("离线错误");
                    break;
                case READY: //准备下载
                    toast("准备下载");
                    break;
                case START: //开始下载
                    msg.what = STATE_START;
                    mHandler.sendMessage(msg);
                    break;
            }
        }
    };

    private void refreshOfflineList() {
        //获取离线地图列表
        List<OfflineItem> offlineItems = offlineComp.getOfflineItemList();
        Log.d(TAG, "Offline Items num:" + offlineItems.size());
//        for (OfflineItem item : offlineItems) {
//            Log.d(TAG,  item.toString());
//        }

        this.offlineItems.clear();
        this.offlineItems.addAll(offlineItems);
        offlineAdapter.notifyDataSetChanged();
        toast("刷新离线地图列表");

    }

    private void tryToOpenOffline() {
        // 无网情况主动开启离线地图
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager.getActiveNetwork() == null) {
                Log.d(TAG, "tryToOpenOffline: 无网络，尝试开启离线地图,item size：" + offlineItems.size());
                for (OfflineItem item : offlineItems) {
                    if (item.getPercentage() == 100 && item.getPinyin().equals("beijingshi")) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(item,
                                statusChangedListener);
                        if (cityController == null) {
                            Log.d(TAG, item.getName() + "try: controller is null");
                        } else {
                            Log.d(TAG, item.getName() + "try: openOffline: " + cityController.open());
                        }
                    }
                }
            }
        } else {
            toast("require API > M(23)");
        }
    }

    private void closeOffline() {
        List<OfflineItem> items = offlineComp.getOfflineItemList();
        Log.d(TAG, "closeOffline: ,item size：" + items.size());
        for (OfflineItem item : items) {
            if (item.getPercentage() == 100) {
                OfflineItemController cityController = offlineComp.getOfflineItemController(item,
                        statusChangedListener);
                if (cityController == null) {
                    Log.d(TAG, item.getName() + "close: controller is null");
                } else {
                    Log.d(TAG, item.getName() + "close: close Offline: " + cityController.close());
                }
            }
        }
    }


    private void syncData() {
        //同步最新数据
        offlineComp.syncLatestData(new OfflineMapSyncedListener() {
            @Override
            public void onSynced(boolean b) {
                Log.d(TAG, "onSynced: " + b);
                refreshOfflineList();
                // tryToOpenOffline();
            }
        });
    }

    private void downloadOffline(OfflineItem offlineItem) {
        //获取某一Item的离线地图数据
        OfflineItemController cityController = offlineComp.getOfflineItemController(offlineItem, statusChangedListener);
        if (cityController == null) {
            Toast.makeText(this, "cityController为NULL", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean needDownload = cityController.checkInvalidate();
        Log.d(TAG, "need download:" + cityController.checkInvalidate());
        if (needDownload) {
            //执行下载
            cityController.startDownload();
            tvParmes.setText("开始下载【" + offlineItem.getName() + "】的离线地图");
        } else {
            //跳过更新，打开离线
            boolean isOpened = cityController.open();
            if (isOpened) {
                tvParmes.setText("已开启【" + offlineItem.getName() + "】的离线地图");
            }
            Log.d(TAG, offlineItem.getName() + " open offline:" + isOpened);
        }
    }

    private final class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //获取离线那地图列表
            List<OfflineItem> items = offlineComp.getOfflineItemList();
            Log.d(TAG, "onReceive: 网络状态变化, offline item size:" + items.size());
            if (networkInfo != null) {
                Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
                for (OfflineItem item : items) {
                    if (item.getPercentage() == 100) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(item,
                                statusChangedListener);
                        if (cityController == null) {
                            Log.d(TAG, item.getName() + " controller is null");
                        } else {
                            Log.d(TAG, item.getName() + " closeOffline: " + cityController.close());
                        }
                    }
                }
            } else {
                // 加载所有已下载的离线地图
                items = offlineComp.getOfflineItemList();
                for (OfflineItem item : items) {
                    if (item.getPercentage() == 100) {
                        OfflineItemController cityController = offlineComp.getOfflineItemController(item,
                                statusChangedListener);
                        if (cityController == null) {
                            Log.d(TAG, item.getName() + " controller is null");
                        } else {
                            Log.d(TAG, item.getName() + " openOffline: " + cityController.open());
                        }
                    }
                }
            }
        }
    }

    private final int STATE_DOWNLOAD = 1;
    private final int STATE_COMPLETED = 2;
    private final int STATE_OPEN = 3;
    private final int STATE_CANCEL = 4;
    private final int STATE_START = 5;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            OfflineItem item = (OfflineItem) msg.obj;
            switch (msg.what) {
                case STATE_DOWNLOAD:
                    if (item.getPercentage() % 10 == 0) {
                        tvParmes.setText("正在下载【" + item.getName() + "】离线地图：" + item.getPercentage() + "%");
                        Log.d("getPercentage", item.getPercentage() + "");
                    }
                    break;
                case STATE_COMPLETED:
                    tvParmes.setText(item.getName() + "离线地图下载结束");
                    break;
                case STATE_OPEN:
                    tvParmes.setText(item.getName() + "离线地图已开启 ");
                    break;
                case STATE_CANCEL:
                    break;
                case STATE_START:
                    tvParmes.setText(item.getName() + "离线地图开始下载，点击刷新列表查看进度");
                    break;

            }
        }
    };

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
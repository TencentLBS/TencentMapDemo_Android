package com.tencent.map.vector.demo.basic;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.sdk.comps.offlinemap.OfflineCity;
import com.tencent.map.sdk.comps.offlinemap.OfflineItem;
import com.tencent.map.sdk.comps.offlinemap.OfflineItemController;
import com.tencent.map.sdk.comps.offlinemap.OfflineMapComponent;
import com.tencent.map.sdk.comps.offlinemap.OfflineMapSyncedListener;
import com.tencent.map.sdk.comps.offlinemap.OfflineProvince;
import com.tencent.map.sdk.comps.offlinemap.OfflineStatus;
import com.tencent.map.sdk.comps.offlinemap.OfflineStatusChangedListener;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 离线地图下载管理页
 */
public class DownloadManageActivity extends Activity implements MyAdapter1.OnItemClickListener, MyAdapter2.OnItemClickListener, OfflineStatusChangedListener {
    private MapView mapView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private MyAdapter1 myAdapter1;
    private MyAdapter2 myAdapter2;
    private OfflineMapComponent OfflineMapComponent;
    private TencentMap tencentMap;
    private List<OfflineItem> offlineItemList;
    private List<OfflineCity> cityList = new ArrayList<>();
    private OfflineItemController cityController;
    private List<OfflineCity> offlineCities = new ArrayList<>();
    private OfflineItem currentOfflineItem; //当前下载城市
    private int currentProgress;
    private String completeName;
    private final int STATE_OPEN = 1;
    private final int STATE_CLOSE = 2;
    private final int STATE_COMPLETED = 3;
    private final int STATE_CANCEL = 4;
    private final int STATE_DOWNLOADING = 5;
    private final int STATE_ERROR = 6;
    private final int STATE_READY = 7;
    private final int STATE_START = 8;
    private OfflineStatus offlineStatus;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_COMPLETED:
                    Toast.makeText(DownloadManageActivity.this, completeName + "下载结束", Toast.LENGTH_LONG).show();
                    break;
                case STATE_DOWNLOADING:
                    Toast.makeText(DownloadManageActivity.this, completeName + "下载中" + currentProgress + "%", Toast.LENGTH_LONG).show();
                    break;
                case STATE_OPEN:
                    Toast.makeText(DownloadManageActivity.this, completeName + "无更新开启离线", Toast.LENGTH_LONG).show();
                    break;
                case STATE_CLOSE:
                    Toast.makeText(DownloadManageActivity.this, completeName + "关闭离线", Toast.LENGTH_LONG).show();
                    break;
                case STATE_READY:
                    Toast.makeText(DownloadManageActivity.this, completeName + "准备下载", Toast.LENGTH_LONG).show();
                    break;
                case STATE_START:
                    Toast.makeText(DownloadManageActivity.this, completeName + "开启下载", Toast.LENGTH_LONG).show();
                    break;
                case STATE_ERROR:
                    Toast.makeText(DownloadManageActivity.this, completeName + "离线错误", Toast.LENGTH_LONG).show();
                    break;
                case STATE_CANCEL:
                    Toast.makeText(DownloadManageActivity.this, completeName + "取消下载", Toast.LENGTH_LONG).show();
                    cancel();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manage);
        recyclerView1 = findViewById(R.id.rv_beijing);
        recyclerView2 = findViewById(R.id.rv_guangdongsheng);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        TencentMapOptions options = new TencentMapOptions();
        //设置离线地图开关打开，默认为false
        options.setOfflineMapEnable(true);
        mapView = findViewById(R.id.mapview);
        tencentMap = mapView.getMap(options);
        OfflineMapComponent = tencentMap.getMapComponent(OfflineMapComponent.class);

        OfflineMapComponent.syncLatestData(new OfflineMapSyncedListener() {
            @Override
            public void onSynced(boolean result) {
                if (result) {
                    offlineItemList = OfflineMapComponent.getOfflineItemList();
                    for (int i = 0; i < offlineItemList.size(); i++) {
                        if (offlineItemList.get(i).getPinyin().equals("beijingshi")) {
                            offlineCities.add((OfflineCity) offlineItemList.get(i));
                        }

                        if (offlineItemList.get(i).getPinyin().equals("henansheng")) {
                            OfflineProvince offlineProvince = (OfflineProvince) offlineItemList.get(i);
                            //获取广东省下的城市
                            cityList = offlineProvince.getCities();
                        }
                    }

                    myAdapter1 = new MyAdapter1();
                    myAdapter2 = new MyAdapter2();

                    myAdapter1.setOnItemClickListener(DownloadManageActivity.this);
                    myAdapter2.setOnItemClickListener(DownloadManageActivity.this);
                    myAdapter1.setList(offlineCities);
                    myAdapter2.setList2(cityList);
                    recyclerView1.setAdapter(myAdapter1);
                    recyclerView2.setAdapter(myAdapter2);

                }

            }
        });

    }

    @Override
    public void onItemClick(View view, int position, OfflineCity offlineCity, OfflineStatus status) {

        switch (view.getId()) {
            case R.id.btn_downlaod: //下载
                download(offlineCity);
                break;
//            case R.id.btn_cancel: //取消
//                cancel();
//                break;
//            case R.id.btn_delete: //删除
//                delete();
//                break;
        }
    }

    private void download(final OfflineCity offlineCity) {
        currentOfflineItem = offlineCity;
        cityController = OfflineMapComponent.getOfflineItemController(offlineCity, DownloadManageActivity.this);
        if (cityController != null) {
            boolean needDownload = cityController.checkInvalidate();
            if (needDownload) {
                Log.i("TAG", offlineCity.getName() + "需更新下载");
                cityController.startDownload();
            } else {
                Log.i("TAG", "无更新打开离线");
                cityController.open();
                Toast.makeText(DownloadManageActivity.this, "无更新打开离线", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.i("TAG", "cityController 为空！");
        }
    }

    private void delete() {
        if (cityController != null) {
            cityController.removeCache();
            Log.i("TAG", "删除下载");
        }
    }

    private void cancel() {
        if (cityController != null) {
            cityController.stopDownload();
            Log.i("TAG", "取消下载");
        }
    }

    @Override
    public void onStatusChanged(OfflineItem item, OfflineStatus status) {
        completeName = item.getName();
        if (item == currentOfflineItem) {
            switch (status) {
                case OPEN: //开始离线
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_OPEN);
                        }
                    }).start();
                    Log.i("TAG", item.getName() + "开启离线");
                    break;
                case CLOSE: //关闭离线
                    Log.i("TAG", item.getName() + "关闭离线");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_CLOSE);
                        }
                    }).start();

                    break;
                case COMPLETED: //完成下载
                    cityController.open();
                    Log.i("TAG", item.getName() + "下载结束");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_COMPLETED);
                        }
                    }).start();

                    break;
                case CANCEL: //取消下载
                    Log.i("TAG", item.getName() + "取消下载");
                    cityController.stopDownload();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_CANCEL);
                        }
                    }).start();

                    break;
                case DOWNLOADING: //下载中
                    currentProgress = currentOfflineItem.getPercentage();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_DOWNLOADING);
                        }
                    }).start();

                    Log.i("TAG", item.getName() + "下载中" + "下载进度" + currentOfflineItem.getPercentage());
                    break;
                case ERROR: //离线错误
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_ERROR);
                        }
                    }).start();
                    Log.i("TAG", item.getName() + "离线错误");
                    break;
                case READY: //准备下载
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_READY);
                        }
                    }).start();
                    Log.i("TAG", item.getName() + "准备下载");
                    break;
                case START: //开始下载
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(STATE_START);
                        }
                    }).start();
                    Log.i("TAG", item.getName() + "开始下载");
                    break;
            }
        }
    }

}

class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyHolder1> {
    private List<OfflineCity> list;
    OnItemClickListener mOnItemClickListener;
    private OfflineStatus status;

    @NonNull
    @Override
    public MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secondary_layout, null);
        MyHolder1 rvHolder = new MyHolder1(view);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder1 holder, final int position) {
        holder.textView.setText(list.get(position).getName() + "\n" + formatDataSize(list.get(position).getSize()));
        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, holder.getAdapterPosition(), list.get(position), status);
                }
            }
        });

//        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnItemClickListener != null) {
//                    // mOnItemClickListener.onItemClick(view, holder.getAdapterPosition(), list.get(position));
//                }
//            }
//        });
//        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnItemClickListener != null) {
//                    //  mOnItemClickListener.onItemClick(view, holder.getAdapterPosition(), list.get(position));
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<OfflineCity> offlineCities) {
        this.list = offlineCities;
    }


    public void updateStatus(OfflineStatus offlineStatus) {
        this.status = offlineStatus;
    }


    class MyHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        Button btn_download;
//        Button btn_cancel;
//        Button btn_delete;

        public MyHolder1(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_city);
            btn_download = itemView.findViewById(R.id.btn_downlaod);
//            btn_cancel = itemView.findViewById(R.id.btn_cancel);
//            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position, OfflineCity offlineCity, OfflineStatus status);
    }

    public String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }


}

class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyHolder2> {
    private List<OfflineCity> list;
    OnItemClickListener mOnItemClickListener;
    private OfflineStatus status;


    @NonNull
    @Override
    public MyHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secondary_layout, null);
        MyHolder2 rvHolder = new MyHolder2(view);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder2 holder, final int position) {
        holder.textView.setText(list.get(position).getName() + "\n" + formatDataSize(list.get(position).getSize()));
        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, holder.getAdapterPosition(), list.get(position), status);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList2(List<OfflineCity> cityList) {
        this.list = cityList;
    }


    public void updateStatus(OfflineStatus offlineStatus) {
        this.status = offlineStatus;
    }


    class MyHolder2 extends RecyclerView.ViewHolder {
        TextView textView;
        Button btn_download;
        Button btn_cancel;
        Button btn_delete;

        public MyHolder2(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_city);
            btn_download = itemView.findViewById(R.id.btn_downlaod);
//            btn_cancel = itemView.findViewById(R.id.btn_cancel);
//            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position, OfflineCity offlineCity, OfflineStatus status);
    }

    public String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }
}
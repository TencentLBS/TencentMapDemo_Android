package com.tencent.map.vector.demo.basic;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import java.util.LinkedList;
import java.util.List;

public class RecyclerListActivity extends FragmentActivity {

    private static final String TAG = "RecyclerListActivity";

    RecyclerView recyclerView;
    private MapViewAdapter mapViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);
        recyclerView = findViewById(R.id.rv_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mapViewAdapter = new MapViewAdapter(this);
        recyclerView.setAdapter(mapViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }

    }

    private static class MapViewHolder extends RecyclerView.ViewHolder {
        MapView mMapView;

        public MapViewHolder(@NonNull View itemView, MapView mapView) {
            super(itemView);
            mMapView = mapView;
        }

        public void onRecycle() {
            if (mMapView != null) {
                mMapView.onPause();
                mMapView.onStop();
            }
        }

        public void onBind() {
            if (mMapView != null) {
                mMapView.onResume();
            }
        }
    }

    private static class MapViewAdapter extends RecyclerView.Adapter<MapViewHolder> {
        private List<MapView> mMapViews;
        private static final int COUNT = 15;
        private Context mContext;

        public MapViewAdapter(Context context) {
            mContext = context.getApplicationContext();
            mMapViews = new LinkedList<>();
        }

        @NonNull
        @Override
        public MapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int type) {

            FrameLayout mapContainer = new FrameLayout(mContext);
            if (type == 1) {
                TencentMapOptions tencentMapOptions = new TencentMapOptions();
                tencentMapOptions.setDisallowInterceptTouchEvent(true);
                MapView mapView = new MapView(mContext, tencentMapOptions);
                mapView.setId(View.NO_ID);
                mapView.setLayoutParams(new FrameLayout.LayoutParams(1000, 700));
                mapContainer.addView(mapView);


                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                return new MapViewHolder(mapContainer, mapView);
            } else {
                return new MapViewHolder(mapContainer, null);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MapViewHolder mapViewHolder, int i) {
            mapViewHolder.onBind();
            if (mapViewHolder.mMapView != null) {
                mMapViews.add(mapViewHolder.mMapView);
            }
        }

        @Override
        public int getItemCount() {
            return COUNT;
        }

        @Override
        public int getItemViewType(int position) {

            if (position % 3 == 0) {
                return 1;
            }

            return super.getItemViewType(position);
        }

        @Override
        public void onViewRecycled(@NonNull MapViewHolder holder) {

            holder.onRecycle();
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            for (MapView mapView : mMapViews) {
                mapView.onDestroy();
            }
        }
    }
}
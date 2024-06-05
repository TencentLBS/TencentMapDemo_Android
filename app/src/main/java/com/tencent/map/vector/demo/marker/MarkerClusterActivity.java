package com.tencent.map.vector.demo.marker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.AbsMapActivity;
import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.utils.CoordinateActivity;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.IAlphaAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.VisibleRegion;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.Cluster;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterItem;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterManager;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.view.ClusterRenderer;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.view.DefaultClusterRenderer;
import com.tencent.tencentmap.mapsdk.vector.utils.ui.IconGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MarkerClusterActivity extends AbsMapActivity {
    private TencentMap tencentMap;
    private boolean mHasAdded;
    //点聚合坐标数据集合
    private List<MarkerClusterItem> generalItemList = new ArrayList<>();
    private ClusterManager<MarkerClusterItem> markerClusterItemClusterManager;
    private int i = 8;
    //自定义聚合
    private ClusterManager<PetaItem> petaItemClusterManager;
    private ArrayList<PetaItem> petaItems;
    private ArrayList<LatLng> latLngs;
    private NonHierarchicalDistanceBasedAlgorithm<MarkerClusterItem> ndba;
    private DefaultClusterRenderer<MarkerClusterItem> markerClusterItemDefaultClusterRenderer;
    private String TAG="MarkerClusterActivity";

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap mTencentMap) {
        super.onCreate(savedInstanceState, mTencentMap);
        tencentMap = mTencentMap;
        //tencentMap.enableMultipleInfowindow(true);
        tencentMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //默认聚合管理
        configDefaultCluterManger();
        configCustomCluterManger();
        petaItemClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PetaItem>() {
            @Override
            public boolean onClusterClick(Cluster<PetaItem> cluster) {
                Collection<PetaItem> items = cluster.getItems();

                return false;
            }
        });
        tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinished(CameraPosition cameraPosition) {
                float zoom = cameraPosition.zoom;
                if (zoom < 6) {
                    tencentMap.setOnCameraChangeListener(markerClusterItemClusterManager);
                    markerClusterItemClusterManager.addItems(getMassiveCoords());
                    // tencentMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().include(latLngs).build(), 20));
                    markerClusterItemClusterManager.cluster();

                } else {
                    markerClusterItemClusterManager.cancel();
                }
                Log.d(TAG, "onCameraChangeFinished: " + zoom);
            }
        });
    }

    public class PetaItem implements ClusterItem {
        private final LatLng mLatLng;
        private int mDrawableResourceId;

        public PetaItem(double lat, double lon, int resourceId) {
            mLatLng = new LatLng(lat, lon);
            mDrawableResourceId = resourceId;
        }

        @Override
        public LatLng getPosition() {
            return mLatLng;
        }

        public int getDrawableResourceId() {
            return mDrawableResourceId;
        }
    }

    //聚合设置
    private void configDefaultCluterManger() {
        //实例化点聚合管理者
        markerClusterItemClusterManager = new ClusterManager<>(this, tencentMap);
        //默认聚合策略，调用时不用添加，如果需要其它聚合策略可以按以下修改
        ndba = new NonHierarchicalDistanceBasedAlgorithm<>(this);
        //设置点聚合距离
        ndba.setMaxDistanceAtZoom(35);
        //设置策略
        markerClusterItemClusterManager.setAlgorithm(ndba);
        //设置聚合渲染器，按照需要设置
        markerClusterItemDefaultClusterRenderer = new DefaultClusterRenderer<>(this, tencentMap, markerClusterItemClusterManager);
        //设置最小聚合数量，默认是4
        markerClusterItemDefaultClusterRenderer.setMinClusterSize(5);
        //定义聚合的分段，例如：当超过5个不满足10个数据，显示5+
        markerClusterItemDefaultClusterRenderer.setBuckets(new int[]{5, 10, 20, 50});
        markerClusterItemClusterManager.setRenderer(markerClusterItemDefaultClusterRenderer);


    }

    //自定义聚合样式设置
    private void configCustomCluterManger() {
        petaItemClusterManager = new ClusterManager<>(this, tencentMap);
        CustomIconClusterRenderer customIconClusterRender = new CustomIconClusterRenderer(this, tencentMap, petaItemClusterManager);
        customIconClusterRender.setMinClusterSize(3);
        petaItemClusterManager.setRenderer(customIconClusterRender);

    }


    //普通聚合
    private Collection<MarkerClusterItem> getGeneralCoords() {
        if (generalItemList == null || generalItemList.size() == 0) {
            generalItemList = getItemWithFileName("cluster_new");
        }
        return generalItemList;
    }

    //海量聚合
    private Collection<MarkerClusterItem> getMassiveCoords() {
        if (generalItemList == null || generalItemList.size() == 0) {
            generalItemList = getItemWithFileName("datab");
        }
        return generalItemList;
    }

    private List<MarkerClusterItem> getItemWithFileName(String cluster_new) {
        ArrayList<MarkerClusterItem> arrayList = new ArrayList<>();
        latLngs = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open(cluster_new);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\t");
                double longitue = Double.parseDouble(data[0]);
                double latitue = Double.parseDouble(data[1]);
                arrayList.add(new MarkerClusterItem(latitue, longitue));
                latLngs.add(new LatLng(latitue, longitue));
            }
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marker_cluster, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add_marker).setVisible(!mHasAdded);
        menu.findItem(R.id.menu_delete_marker).setVisible(mHasAdded);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tencentMap.setInfoWindowAdapter(new TencentMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LinearLayout inflate = (LinearLayout) View.inflate(getApplicationContext(), R.layout.custom_infowindow, null);
                TextView title = inflate.findViewById(R.id.tv_title);
                Point point = tencentMap.getProjection().toScreenLocation(marker.getPosition());
                title.setText("\t\t\t\t\t\t\t\t自定义View弹窗:\n点在屏幕位置：" + point);
                return inflate;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        final IAlphaAnimation alphaAnimation_custom = tencentMap.getMapContext().createAlphaAnimation(0f, 1f);
        alphaAnimation_custom.setDuration(1000);
        alphaAnimation_custom.setInterpolator(new AccelerateDecelerateInterpolator());
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Point point = tencentMap.getProjection().toScreenLocation(marker.getPosition());
                marker.setTitle(point.toString());
                marker.setAnimation(alphaAnimation_custom);
                marker.startAnimation();
                return false;
            }
        });
        switch (item.getItemId()) {
            case R.id.menu_add_marker_general:
                mHasAdded = true;
                //markerClusterItemClusterManager.cancel();
                tencentMap.setOnCameraChangeListener(markerClusterItemClusterManager);
                markerClusterItemClusterManager.addItems(getGeneralCoords());
                LatLng position = generalItemList.get(6).getPosition();
                CameraUpdate cameraUpdate;
                if (i < 16) {
                    i++;
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, i);
                } else {
                    i = 6;
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, i);
                }
                tencentMap.animateCamera(cameraUpdate);
                markerClusterItemClusterManager.cluster();

                break;
            case R.id.menu_add_marker_massive:
                mHasAdded = true;
                tencentMap.setOnCameraChangeListener(markerClusterItemClusterManager);
                markerClusterItemClusterManager.addItems(getMassiveCoords());

                LatLng position_massive = generalItemList.get(60).getPosition();
                CameraUpdate cameraUpdate_massive;
                if (i < 16) {
                    i++;
                    cameraUpdate_massive = CameraUpdateFactory.newLatLngZoom(position_massive, i);
                } else {
                    i = 6;
                    cameraUpdate_massive = CameraUpdateFactory.newLatLngZoom(position_massive, i);
                }
                tencentMap.animateCamera(cameraUpdate_massive);
                markerClusterItemClusterManager.cluster();
                break;
            case R.id.menu_add_marker_custom:
                mHasAdded = true;
                tencentMap.setOnCameraChangeListener(petaItemClusterManager);
                addCustomClusterItem();
                tencentMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.971595, 116.314316), 15));
                petaItemClusterManager.cluster();
                break;
            case R.id.menu_delete_marker:
                mHasAdded = false;
                clusterReMove();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addCustomClusterItem() {

        petaItemClusterManager.addItem(new PetaItem(39.971595, 116.294747, R.mipmap.petal_blue));

        petaItemClusterManager.addItem(new PetaItem(39.971595, 116.314316, R.mipmap.petal_red));

        petaItemClusterManager.addItem(new PetaItem(39.967385, 116.317063, R.mipmap.petal_green));

        petaItemClusterManager.addItem(new PetaItem(39.951596, 116.302300, R.mipmap.petal_yellow));

        petaItemClusterManager.addItem(new PetaItem(39.970543, 116.290627, R.mipmap.petal_orange));

        petaItemClusterManager.addItem(new PetaItem(39.966333, 116.311569, R.mipmap.petal_purple));
    }

    //移除聚合数据
    private void clusterReMove() {
        markerClusterItemClusterManager.cancel();
        markerClusterItemClusterManager.cluster();
        generalItemList.clear();
        petaItemClusterManager.cancel();
        petaItemClusterManager.cluster();
        tencentMap.clearAllOverlays();

    }

    class CustomIconClusterRenderer extends DefaultClusterRenderer<PetaItem> {

        private IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private ImageView mItemImageView = new ImageView(getApplicationContext());
        private ImageView mClusterImageView = new ImageView(getApplicationContext());

        public CustomIconClusterRenderer(
                Context context, TencentMap map, ClusterManager clusterManager) {
            super(context, map, clusterManager);
            mItemImageView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mIconGenerator.setContentView(mItemImageView);

            mClusterImageView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mClusterIconGenerator.setContentView(mClusterImageView);

            setMinClusterSize(1);
        }

        @Override
        public void onBeforeClusterRendered(
                Cluster<PetaItem> cluster, MarkerOptions markerOptions) {
            int[] resources = new int[cluster.getItems().size()];
            int i = 0;
            for (PetaItem item : cluster.getItems()) {
                resources[i++] = item.getDrawableResourceId();
            }

            PetalDrawable drawable = new PetalDrawable(getApplicationContext(), resources);
            mClusterImageView.setImageDrawable(drawable);
            Bitmap icon = mClusterIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        public void onBeforeClusterItemRendered(PetaItem item, MarkerOptions markerOptions) {
            mItemImageView.setImageResource(item.getDrawableResourceId());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

    class MarkerClusterItem implements ClusterItem {
        private final LatLng mLatLng;

        public MarkerClusterItem(double lat, double lon) {
            mLatLng = new LatLng(lat, lon);
        }

        @Override
        public LatLng getPosition() {
            return mLatLng;
        }
    }
}



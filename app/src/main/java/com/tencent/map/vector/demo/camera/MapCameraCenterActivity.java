package com.tencent.map.vector.demo.camera;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.map.vector.demo.AbsTestCaseMapActivity;
import com.tencent.map.vector.demo.utils.GeometryUtil;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

public class MapCameraCenterActivity extends AbsTestCaseMapActivity {

    Marker mMarker;
    CameraPosition mFromPosition;
    VisibleRegion mFromRegion;

    int paddingLeftRight = 200;
    int paddingTopBottom = 100;

    private final TestCase testCase1 = new TestCase("1 -> 固定锚点+固定级别18") {
        private Polygon mBizPolygon;

        @Override
        public void doOpt(final TencentMap tencentMap) {
            int height = mBottomLayout.getHeight();
            int mapWidth = tencentMap.getMapWidth();
            int mapHeight = tencentMap.getMapHeight();

            //1. 修改地图比例中心
            tencentMap.setCameraCenterProportion(0.5F, (float) (mapHeight - height) / (2 * mapHeight), false);
            //2. 移动到新中心点+缩放
            tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 18));
            //3. 输出动态视窗区域
            tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(final CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinished(final CameraPosition cameraPosition) {
                    tencentMap.setOnCameraChangeListener(null);

                    RectF bizRect = new RectF();
                    bizRect.set(paddingLeftRight, paddingTopBottom,
                            mapWidth - paddingLeftRight, mapHeight - height - paddingTopBottom);

                    LatLng bizBoundLeftTop = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.left, bizRect.top));
                    LatLng bizBoundLeftBottom = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.left, bizRect.bottom));
                    LatLng bizBoundRightTop = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.right, bizRect.top));
                    LatLng bizBoundRightBottom = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.right, bizRect.bottom));

                    LatLngBounds bizBound = LatLngBounds.builder()
                            .include(bizBoundLeftTop)
                            .include(bizBoundLeftBottom)
                            .include(bizBoundRightTop)
                            .include(bizBoundRightBottom)
                            .build();

                    if (mBizPolygon != null) {
                        mBizPolygon.remove();
                    }
                    mBizPolygon = tencentMap.addPolygon(new PolygonOptions()
                            .add(bizBoundLeftTop, bizBoundRightTop, bizBoundRightBottom, bizBoundLeftBottom)
                            .fillColor(0x88FFFF00)
                            .strokeWidth(0)
                    );

                    Log.d("MapCamera", "输出当前业务区域：" + bizBound + " 中心点：" + bizBound.getCenter());
                }
            });
        }

        @Override
        public void backOpt(final TencentMap tencentMap) {
            if (mMarker != null) {
                tencentMap.setCameraCenterProportion(0.5F, 0.5F, false);
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), mFromPosition.zoom));
            }
            if (mBizPolygon != null) {
                mBizPolygon.remove();
                mBizPolygon = null;
            }
        }
    };

    private final TestCase testCase2 = new TestCase("2 -> 新锚点+半径100m") {

        private Circle thisCircle;
        private Polygon mBizPolygon;
        private Marker thisCircleCenter;

        @Override
        public void doOpt(final TencentMap tencentMap) {
            //1. 输入参数：新中心坐标和半径
            LatLng newCenter = new LatLng(39.910811, 116.45657);
            int radius = 100;//单位:<米>

            //2.计算圆形的Bound
            LatLngBounds circleBound = LatLngBounds.builder()
                    .include(GeometryUtil.move(newCenter, -100, -100))
                    .include(GeometryUtil.move(newCenter, 100, -100))
                    .include(GeometryUtil.move(newCenter, 100, 100))
                    .include(GeometryUtil.move(newCenter, -100, 100))
                    .build();

            int height = mBottomLayout.getHeight();
            int mapWidth = tencentMap.getMapWidth();
            int mapHeight = tencentMap.getMapHeight();

            //3.计算视窗大小
            RectF screenRect = new RectF();
            screenRect.set(0, 0,
                    mapWidth, mapHeight - height);

            //4.计算最佳视野区域参数
            float skew = mFromPosition.tilt;//俯仰角
            float rotate = mFromPosition.bearing;//旋转角
            CameraPosition newPosition = tencentMap.calculateMapOverLook(circleBound, screenRect, skew, rotate, paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);

            //5. 修改地图比例中心
            tencentMap.setCameraCenterProportion(0.5F, (float) (mapHeight - height) / (2 * mapHeight), false);
            //6. 移动到新中心点+缩放
            tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newCenter, newPosition.zoom));
            //7. 输出动态视窗区域
            tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(final CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinished(final CameraPosition cameraPosition) {
                    tencentMap.setOnCameraChangeListener(null);

                    RectF bizRect = new RectF();
                    bizRect.set(paddingLeftRight, paddingTopBottom,
                            mapWidth - paddingLeftRight, mapHeight - height - paddingTopBottom);

                    LatLng bizBoundLeftTop = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.left, bizRect.top));
                    LatLng bizBoundLeftBottom = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.left, bizRect.bottom));
                    LatLng bizBoundRightTop = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.right, bizRect.top));
                    LatLng bizBoundRightBottom = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.right, bizRect.bottom));

                    LatLngBounds bizBound = LatLngBounds.builder()
                            .include(bizBoundLeftTop)
                            .include(bizBoundLeftBottom)
                            .include(bizBoundRightTop)
                            .include(bizBoundRightBottom)
                            .build();

                    thisCircleCenter = tencentMap.addMarker(new MarkerOptions(newCenter)
                            .anchor(0.5f, 1)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    thisCircle = tencentMap.addCircle(new CircleOptions().center(newCenter).radius(radius));

                    if (mBizPolygon != null) {
                        mBizPolygon.remove();
                    }
                    mBizPolygon = tencentMap.addPolygon(new PolygonOptions()
                            .add(bizBoundLeftTop, bizBoundRightTop, bizBoundRightBottom, bizBoundLeftBottom)
                            .fillColor(0x88FFFF00)
                            .strokeWidth(0)
                    );

                    Log.d("MapCamera", "输出当前业务区域：" + bizBound + " 中心点：" + bizBound.getCenter() + " 缩放级别：" + newPosition.zoom);
                }
            });
        }

        @Override
        public void backOpt(final TencentMap tencentMap) {
            if (mMarker != null) {
                tencentMap.setCameraCenterProportion(0.5F, 0.5F, false);
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), mFromPosition.zoom));
            }

            if (thisCircle != null) {
                thisCircle.remove();
            }

            if (thisCircleCenter != null) {
                thisCircleCenter.remove();
            }
        }
    };

    private final TestCase testCase3 = new TestCase("3 -> 经纬度区域") {

        private Polygon mBizPolygon;
        private Polygon mPointPolygon;

        private final List<Marker> mPointMarkers = new ArrayList<>();
        private final List<LatLng> mPoints = new ArrayList<>();

        @Override
        public void doOpt(final TencentMap tencentMap) {
            //1. 输入参数：随机点构成一个区域，也可以指定经纬度矩阵
            List<LatLng> points = mPoints;
            points.add(new LatLng(39.93503, 116.384869));
            points.add(new LatLng(39.927016,116.37446));
            points.add(new LatLng(39.930768,116.396274));
            points.add(new LatLng(39.937892,116.38885));
            LatLngBounds latLngBounds = LatLngBounds.builder()
                    .include(points)
                    .build();
            LatLng newCenter = latLngBounds.getCenter();

            //2. 获取当前地图尺寸
            int height = mBottomLayout.getHeight();
            int mapWidth = tencentMap.getMapWidth();
            int mapHeight = tencentMap.getMapHeight();

            //3.计算视窗大小
            RectF screenRect = new RectF();
            screenRect.set(0, 0, mapWidth, mapHeight - height);

            //4.计算最佳视野区域参数
            float skew = mFromPosition.tilt;//俯仰角
            float rotate = mFromPosition.bearing;//旋转角
            CameraPosition newPosition = tencentMap.calculateMapOverLook(latLngBounds, screenRect,
                    skew, rotate, paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);

            //5. 修改地图比例中心
            tencentMap.setCameraCenterProportion(0.5F, (float) (mapHeight - height) / (2 * mapHeight), false);
            //6. 移动到新中心点+缩放
            tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newCenter, newPosition.zoom));
            //7. 输出动态视窗区域
            tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(final CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinished(final CameraPosition cameraPosition) {
                    tencentMap.setOnCameraChangeListener(null);

                    RectF bizRect = new RectF();
                    bizRect.set(paddingLeftRight, paddingTopBottom,
                            mapWidth - paddingLeftRight, mapHeight - height - paddingTopBottom);

                    LatLng bizBoundLeftTop = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.left, bizRect.top));
                    LatLng bizBoundLeftBottom = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.left, bizRect.bottom));
                    LatLng bizBoundRightTop = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.right, bizRect.top));
                    LatLng bizBoundRightBottom = tencentMap.getProjection().fromScreenLocation(new PointF(bizRect.right, bizRect.bottom));

                    LatLngBounds bizBound = LatLngBounds.builder()
                            .include(bizBoundLeftTop)
                            .include(bizBoundLeftBottom)
                            .include(bizBoundRightTop)
                            .include(bizBoundRightBottom)
                            .build();

                    mBizPolygon = tencentMap.addPolygon(new PolygonOptions()
                            .add(bizBoundLeftTop, bizBoundRightTop, bizBoundRightBottom, bizBoundLeftBottom)
                            .fillColor(0x88FFFF00)
                            .strokeWidth(0)
                    );

                    mPointPolygon = tencentMap.addPolygon(new PolygonOptions()
                            .add(latLngBounds.getNorthWest(), latLngBounds.getNorthEast(), latLngBounds.getSouthEast(), latLngBounds.getSouthWest())
                            .fillColor(Color.argb(200, 216,231,214))
                            .strokeWidth(0)
                    );

                    for (LatLng p : mPoints) {
                        mPointMarkers.add(tencentMap.addMarker(new MarkerOptions(p)
                                .anchor(0.5f, 1f)
                                .icon(BitmapDescriptorFactory.defaultMarker())));
                    }

                    Log.d("MapCamera", "输出当前业务区域：" + bizBound + " 中心点：" + bizBound.getCenter() + " 缩放级别：" + newPosition.zoom);
                }
            });

        }

        @Override
        public void backOpt(final TencentMap tencentMap) {
            if (mMarker != null) {
                tencentMap.setCameraCenterProportion(0.5F, 0.5F, false);
                tencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), mFromPosition.zoom));
            }
            if (mBizPolygon != null) {
                mBizPolygon.remove();
                mBizPolygon = null;
            }

            for (Marker marker : mPointMarkers) {
                marker.remove();
            }
            mPointMarkers.clear();

            if (mPointPolygon != null) {
                mPointPolygon.remove();
                mPointPolygon = null;
            }
        }
    };

    @Override
    protected TestCase[] getTestCases() {
        return new TestCase[]{testCase1, testCase2, testCase3};
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState, final TencentMap tencentMap) {
        super.onCreate(savedInstanceState, tencentMap);
        //1.初始化态
        tencentMap.setIndoorEnabled(false);
        tencentMap.getUiSettings().setScaleViewEnabled(true);
        tencentMap.getUiSettings().setScaleViewFadeEnable(false);
        tencentMap.moveCamera(CameraUpdateFactory.zoomTo(8));

        tencentMap.addOnMapLoadedCallback(new TencentMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //2.获取当前地图相机信息
                mFromPosition = tencentMap.getCameraPosition();
                LatLng mCenter = mFromPosition.target;
                mMarker = tencentMap.addMarker(new MarkerOptions(mCenter)
                        .anchor(0.5f, 1)
                        .title("当前视野中心")
                        .snippet(mCenter.x() + "," + mCenter.y())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                mFromRegion = tencentMap.getProjection().getVisibleRegion();

                showCasePanel();
            }
        });

    }
}

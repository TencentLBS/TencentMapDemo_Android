package com.tencent.map.vector.demo.search;

import android.os.Bundle;
import android.util.Log;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.EBicyclingMatrixParam;
import com.tencent.lbssearch.object.param.MatrixParam;
import com.tencent.lbssearch.object.result.DistanceMatrixResultObject;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class EBicyclingMatrixActivity extends SupportMapFragmentActivity {

    // 起点
    private LatLng fromPoint = new LatLng(40.040219, 116.273348);
    // 终点列表
    private LatLng toPoint1 = new LatLng(40.048055, 116.281166);
    private LatLng toPoint2 = new LatLng(39.984154, 116.307490);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEBicyclingMatrix();
    }

    /**
     * 获取电动自行车距离矩阵（6.13.0 新增）
     */
    private void getEBicyclingMatrix() {
        EBicyclingMatrixParam param = new EBicyclingMatrixParam();
        // 设置策略：外卖配送场景
        param.setPolicy(EBicyclingMatrixParam.Policy.DELIVERY);
        // 添加起点
        param.addFromPoint(new MatrixParam.HeadingLatLng(fromPoint));
        // 添加多个终点，计算起点到各终点的距离/耗时
        param.addToPoint(toPoint1);
        param.addToPoint(toPoint2);

        TencentSearch tencentSearch = new TencentSearch(getApplicationContext());
        Log.i("TAG", "checkParams:" + param.checkParams());
        tencentSearch.getDistanceMatrix(param, new HttpResponseListener<DistanceMatrixResultObject>() {
            @Override
            public void onSuccess(int statusCode, DistanceMatrixResultObject object) {
                if (object == null) {
                    Log.i("TAG", "baseObject为空");
                    return;
                }
                showMatrixResult(object);
                Log.i("TAG", "message:" + object.message);
            }

            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                Log.i("TAG:", statusCode + "  " + responseString);
            }
        });
    }

    private void showMatrixResult(DistanceMatrixResultObject object) {
        if (object.result != null && object.result.rows != null) {
            for (int i = 0; i < object.result.rows.size(); i++) {
                DistanceMatrixResultObject.DistanceMatrixResult.RowResult row = object.result.rows.get(i);
                if (row.elements != null) {
                    for (int j = 0; j < row.elements.size(); j++) {
                        DistanceMatrixResultObject.DistanceMatrixResult.RowResult.DistanceElement element =
                                row.elements.get(j);
                        Log.i("TAG", "from[" + i + "] -> to[" + j + "] distance:" + element.distance
                                + " duration:" + element.duration + " status:" + element.status);
                    }
                }
            }
        } else {
            Log.i("TAG", "距离矩阵结果为空");
        }
    }
}

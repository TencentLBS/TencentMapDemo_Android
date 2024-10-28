//package com.tencent.map.vector.demo.search;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.tencent.map.vector.demo.R;
//import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
//import com.tencent.lbssearch.TencentSearch;
//import com.tencent.lbssearch.httpresponse.BaseObject;
//import com.tencent.lbssearch.httpresponse.HttpResponseListener;
//import com.tencent.lbssearch.object.param.TranslateParam;
//import com.tencent.lbssearch.object.result.TranslateResultObject;
//import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
//
//public class CoordianteTransferActivity extends SupportMapFragmentActivity {
//
//    private EditText etCoordinate;
//    private Spinner spCoordinate;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//    }
//
//    private void initView(){
//        etCoordinate = (EditText) findViewById(R.id.et_search_poi);
//        spCoordinate
//    }
//    /**
//     * 由字符串获取坐标
//     * @param context
//     * @param str
//     * @return
//     */
//    public static LatLng str2Coordinate(Context context, String str) {
//        if (!str.contains(",")) {
//            Toast.makeText(context, "经纬度用\",\"分割", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//        String[] strs = str.split(",");
//        double lat = 0;
//        double lng = 0;
//        try {
//            lat = Double.parseDouble(strs[0]);
//            lng = Double.parseDouble(strs[1]);
//        } catch (NumberFormatException e) {
//            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//            return null;
//        }
//        return new LatLng(lat, lng);
//    }
//
//    private static final String[] coorTypes = {
//            TranslateParam.CoordType.GPS.name(),
//            TranslateParam.CoordType.SOGOU.name(),
//            TranslateParam.CoordType.BAIDU.name(),
//            TranslateParam.CoordType.MAPBAR.name(),
//            TranslateParam.CoordType.STANDARD.name(),
//            TranslateParam.CoordType.SOGOUMERCATOR.name()
//    };
//
//    /**
//     * 坐标转换
//     */
//    protected void coorTranslate() {
//        String str = etCoordinate.getText().toString().trim();
//        LatLng latLng = str2Coordinate(this, str);
//        if (latLng == null) {
//            return;
//        }
//        TencentSearch tencentSearch = new TencentSearch(this);
//        TranslateParam translateParam = new TranslateParam();
//        translateParam.addLocation(latLng);
//        translateParam.coordType(TranslateParam.CoordType.valueOf(coorTypes[spCoordinate.getSelectedItemPosition()]));
//        tencentSearch.translate(translateParam, new HttpResponseListener<BaseObject>() {
//
//            @Override
//            public void onSuccess(int arg0, BaseObject arg1) {
//                // TODO Auto-generated method stub
//                if (arg1 == null) {
//                    return;
//                }
//                TranslateResultObject obj = (TranslateResultObject) arg1;
//                StringBuilder sb = new StringBuilder();
//                sb.append("\n latLng：" + obj.latLngs.toString());
//                printResult(sb.toString());
//            }
//
//            @Override
//            public void onFailure(int arg0, String arg1, Throwable arg2) {
//                // TODO Auto-generated method stub
//                printResult(arg1);
//            }
//        });
//    }
//
//
//}

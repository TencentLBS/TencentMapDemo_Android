package com.tencent.map.vector.demo.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.object.result.SuggestionResultObject;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchBasicActivity extends SupportMapFragmentActivity {
    private EditText etSearch;
    private Button btnSearch;
    private ListView lvSuggesion;
    private SuggestionAdapter suggestionAdapter;
    private final int MSG_SUGGESTION = 10000;
    private final MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<SearchBasicActivity> mActivity;

        public MyHandler(SearchBasicActivity activity) {
            // TODO Auto-generated constructor stub
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            SearchBasicActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView(){
        etSearch = (EditText) findViewById(R.id.et_search_poi);
        btnSearch = (Button) findViewById(R.id.btn_search_poi);
        etSearch.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        lvSuggesion = (ListView) findViewById(R.id.lv_suggestions);

        etSearch.addTextChangedListener(textWatcher);
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!etSearch.hasFocus()) {
                    lvSuggesion.setVisibility(View.GONE);
                }
            }
        });

        lvSuggesion.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                etSearch.removeTextChangedListener(textWatcher);
                CharSequence cs =
                        ((TextView)view.findViewById(R.id.label)).getText();
                etSearch.setText(cs);
                lvSuggesion.setVisibility(View.GONE);
                etSearch.addTextChangedListener(textWatcher);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPoi();
            }
        });
    }

    final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            suggestion(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };



    /**
     * poi检索
     */
    protected void searchPoi() {
        TencentSearch tencentSearch = new TencentSearch(this);
        String keyWord = etSearch.getText().toString().trim();
        //城市搜索
        SearchParam.Region region = new SearchParam.Region("北京").//设置搜索城市
                autoExtend(false);//设置搜索范围不扩大
        //圆形范围搜索
        LatLng latLng1 = new LatLng(39.984154, 116.307490);
        SearchParam.Nearby nearBy = new SearchParam.Nearby(latLng1, 1000);
        //矩形搜索，这里的范围是故宫
        LatLng latLng2 = new LatLng(39.913127, 116.392164);
        LatLng latLng3 = new LatLng(39.923034, 116.402078);
        SearchParam.Rectangle rectangle = new SearchParam.Rectangle(latLng2, latLng3);

        //filter()方法可以设置过滤类别，
        //search接口还提供了排序方式、返回条目数、返回页码具体用法见文档，
        //同时也可以参考官网的webservice对应接口的说明
        SearchParam searchParam = new SearchParam(keyWord, region);//.pageIndex(0).pageSize(20).filter("大学,中学");
        tencentSearch.search(searchParam, new HttpResponseListener<BaseObject>() {

            @Override
            public void onFailure(int arg0, String arg2,
                                  Throwable arg3) {
                Toast.makeText(getApplicationContext(), arg2, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                if (arg1 == null) {
                    return;
                }
                SearchResultObject obj = (SearchResultObject) arg1;
                if(obj.data == null){
                    return;
                }
                tencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(obj.data.get(0).latLng,15f, 0, 0)));
                for(SearchResultObject.SearchResultData data : obj.data){
                    Log.v("SearchDemo","title:"+data.title + ";" + data.address);
                    tencentMap.addMarker(new MarkerOptions()
                            .position(data.latLng)  //标注的位置
                            .title(data.title)     //标注的InfoWindow的标题
                            .snippet(data.address) //标注的InfoWindow的内容
                    );

                }
            }
        });
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SUGGESTION:
                showAutoComplete((SuggestionResultObject)msg.obj);
                break;

            default:
                break;
        }
    }

    /**
     * 显示完整ListView
     * @param obj
     */
    protected void showAutoComplete(SuggestionResultObject obj) {
        if (obj.data.size() == 0) {
            lvSuggesion.setVisibility(View.GONE);
            return;
        }
        if (suggestionAdapter == null) {
            suggestionAdapter = new SuggestionAdapter(obj.data);
            lvSuggesion.setAdapter(suggestionAdapter);
        } else {
            suggestionAdapter.setDatas(obj.data);
            suggestionAdapter.notifyDataSetChanged();
        }
        lvSuggesion.setVisibility(View.VISIBLE);
    }
    /**
     * 关键字提示
     * @param keyword
     */
    protected void suggestion(String keyword) {
        if (keyword.trim().length() == 0) {
            lvSuggesion.setVisibility(View.GONE);
            return;
        }
        TencentSearch tencentSearch = new TencentSearch(this);
        SuggestionParam suggestionParam = new SuggestionParam(keyword, "北京");
        //suggestion也提供了filter()方法和region方法
        //具体说明见文档，或者官网的webservice对应接口
        tencentSearch.suggestion(suggestionParam, new HttpResponseListener<BaseObject>() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                if (arg1 == null ||
                        etSearch.getText().toString().trim().length() == 0) {
                    lvSuggesion.setVisibility(View.GONE);
                    return;
                }

                Message msg = new Message();
                msg.what = MSG_SUGGESTION;
                msg.obj = arg1;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                //printResult(arg1);
            }
        });
    }

    class SuggestionAdapter extends BaseAdapter {

        List<SuggestionResultObject.SuggestionData> mSuggestionDatas;

        public SuggestionAdapter(List<SuggestionResultObject.SuggestionData> suggestionDatas) {
            // TODO Auto-generated constructor stub
            setDatas(suggestionDatas);
        }

        public void setDatas(List<SuggestionResultObject.SuggestionData> suggestionDatas) {
            mSuggestionDatas = suggestionDatas;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mSuggestionDatas.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mSuggestionDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(SearchBasicActivity.this,
                        R.layout.suggestion_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label);
                viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.desc);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(mSuggestionDatas.get(position).title);
            viewHolder.tvAddress.setText(mSuggestionDatas.get(position).address);
            return convertView;
        }

        private class ViewHolder{
            TextView tvTitle;
            TextView tvAddress;
        }
    }
}

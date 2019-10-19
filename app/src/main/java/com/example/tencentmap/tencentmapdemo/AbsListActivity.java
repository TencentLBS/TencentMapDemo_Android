package com.example.tencentmap.tencentmapdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class AbsListActivity extends AbsActivity {

    protected int getLayoutId() {
        return R.layout.activity_recycler_container;
    }

    private List<ActivityItem> mActivityItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setupViews();
    }

    protected boolean enableNextActivity() {
        return true;
    }

    private void setupViews() {
        RecyclerView recyclerView = findViewById(R.id.layout_recycle_container);
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            setupListData();

            ActivityAdapter activityAdapter = new ActivityAdapter();
            activityAdapter.submitList(mActivityItems);

            recyclerView.setAdapter(activityAdapter);
        }
    }

    private void setupListData() {
        mActivityItems = new ArrayList<>();

        ActivityInfo[] activityIfs = new ActivityInfo[0];
        try {
            activityIfs = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA).activities;
        } catch (PackageManager.NameNotFoundException pE) {
            pE.printStackTrace();
        }

        Arrays.sort(activityIfs, new Comparator<ActivityInfo>() {
            @Override
            public int compare(ActivityInfo o1, ActivityInfo o2) {
                return o1.name.compareTo(o2.name);
            }
        });

        //优先以ORDER排序
        Arrays.sort(activityIfs, new Comparator<ActivityInfo>() {
            @Override
            public int compare(ActivityInfo o1, ActivityInfo o2) {
                int order1 = Integer.MAX_VALUE;
                if (o1.metaData != null) {
                    order1 = o1.metaData.getInt("ORDER", Integer.MAX_VALUE);
                }
                int order2 = Integer.MAX_VALUE;
                if (o2.metaData != null) {
                    order2 = o2.metaData.getInt("ORDER", Integer.MAX_VALUE);
                }

                return order1 - order2;
            }
        });

        for (ActivityInfo info : activityIfs) {
            //当前activity的所有子界面
            if (info.enabled && getClass().getName().equals(info.parentActivityName)) {
                if (!TextUtils.isEmpty(info.parentActivityName)) {
                    ActivityInfo parentActivityInfo;
                    try {
                        parentActivityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                        mActivityItems.add(new ActivityItem(this, parentActivityInfo, info));
                    } catch (PackageManager.NameNotFoundException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        }
    }

    static class ActivityItem {
        String clz;
        String name;
        String parentName;
        String desc;

        ActivityItem(Context pContext, ActivityInfo pParentInfo, ActivityInfo pInfo) {
            clz = pInfo.name;
            name = pInfo.labelRes != 0 ? pContext.getString(pInfo.labelRes) : pInfo.loadLabel(pContext.getPackageManager()).toString();
            desc = pInfo.descriptionRes != 0 ? pContext.getString(pInfo.descriptionRes) : "";
            parentName = pParentInfo.descriptionRes != 0 ? pContext.getString(pParentInfo.descriptionRes) : "";
        }

        @Override
        public boolean equals(Object pO) {
            if (this == pO) return true;
            if (pO == null || getClass() != pO.getClass()) return false;

            ActivityItem item = (ActivityItem) pO;

            if (name != null ? !name.equals(item.name) : item.name != null) return false;
            if (parentName != null ? !parentName.equals(item.parentName) : item.parentName != null)
                return false;
            return desc != null ? desc.equals(item.desc) : item.desc == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            return result;
        }
    }

    private class ActivityAdapter extends ListAdapter<ActivityItem, ActivityItemHolder> {

        ActivityAdapter() {
            super(new DiffUtil.ItemCallback<ActivityItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull ActivityItem oldItem, @NonNull ActivityItem newItem) {
                    return oldItem.name.equals(newItem.name);
                }

                @Override
                public boolean areContentsTheSame(@NonNull ActivityItem oldItem, @NonNull ActivityItem newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public ActivityItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ActivityItemHolder(AbsListActivity.this, parent, viewType);
        }

        @Override
        public int getItemViewType(int position) {
//            ActivityItem item = getItem(position);
//            if (TextUtils.isEmpty(item.parentName)) {
//                return ActivityHolder.ITEM_TYPE_GROUP;
//            }
            return ActivityItemHolder.ITEM_TYPE_ENTRY;
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityItemHolder holder, int position) {
            holder.bindView(getItem(position));
        }
    }

    static class ActivityItemHolder<T extends ActivityItem> extends RecyclerView.ViewHolder {

        private static final int ITEM_TYPE_GROUP = 0;
        private static final int ITEM_TYPE_ENTRY = 1;

        private int mViewType;

        private TextView mTitle;
        private TextView mSubTitle;

        private AbsListActivity mActivity;

        ActivityItemHolder(AbsListActivity pActivityListActivity, ViewGroup pParent, int pViewType) {
            super(LayoutInflater.from(pActivityListActivity).inflate(ActivityItemHolder.getItemLayoutIds(pViewType), pParent, false));
            mViewType = pViewType;
            mActivity = pActivityListActivity;

            mTitle = itemView.findViewById(android.R.id.text1);
            mSubTitle = itemView.findViewById(android.R.id.text2);

            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            mSubTitle.setTextColor(Color.GRAY);
        }

        private static int getItemLayoutIds(int pViewType) {
            switch (pViewType) {
                case ITEM_TYPE_GROUP:
                    return android.R.layout.simple_list_item_1;
                case ITEM_TYPE_ENTRY:
                    return android.R.layout.simple_list_item_2;
            }
            return 0;
        }

        void bindView(T pItem) {

            if (mViewType == ITEM_TYPE_GROUP) {
                setupItemGroup(pItem);
            } else if (mViewType == ITEM_TYPE_ENTRY) {
                setupItemEntry(pItem);
            }
        }

        private void setupItemGroup(T pItem) {
            mTitle.setText(pItem.parentName);
        }

        private void setupItemEntry(final T pItem) {
            mTitle.setText(pItem.name);
            mSubTitle.setText(pItem.desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mActivity.enableNextActivity()) {
                        return;
                    }
                    try {
                        Intent intent = new Intent(mActivity, Class.forName(pItem.clz));
                        mActivity.startActivity(intent);
                    } catch (ClassNotFoundException pE) {
                        pE.printStackTrace();
                    }
                }
            });
        }
    }

}

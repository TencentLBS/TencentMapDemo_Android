package com.tencent.map.vector.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AbsListActivity {

    private static final String DEMO_TYPE = "DEMO_TYPE";

    private static List<DemoInfo> mDemoTypes;
    private static Map<String, List<DemoInfo>> mDemoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDemos();
        setupViews(getIntent().getStringExtra(DEMO_TYPE));
    }

    @Override
    protected String[] onRequestPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void setupViews(String type) {
        RecyclerView recyclerView = findViewById(R.id.layout_recycle_container);
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager =
                    new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(
                    getApplicationContext(), DividerItemDecoration.VERTICAL));

            DemoActivityAdapter activityAdapter = new DemoActivityAdapter(type);
            recyclerView.setAdapter(activityAdapter);
        }
    }

    private void getDemos() {
        if (mDemoMap != null) {
            return;
        }

        mDemoMap = new HashMap<>(16);
        mDemoTypes = new ArrayList<>(16);

        ActivityInfo[] activityIfs = new ActivityInfo[0];
        try {
            activityIfs = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA).activities;
        } catch (PackageManager.NameNotFoundException pE) {
            pE.printStackTrace();
        }

        for (ActivityInfo activityInfo : activityIfs) {
            if (activityInfo.metaData != null) {
                String demoType = activityInfo.metaData.getString(
                        getResources().getString(R.string.demo_type));
                List<DemoInfo> items = mDemoMap.get(demoType);
                if (items == null) {
                    items = new ArrayList<>();
                    mDemoMap.put(demoType, items);
                    mDemoTypes.add(new DemoInfo(
                            MainActivity.class.getName(),
                            demoType,
                            ""));
                }

                items.add(new DemoInfo(
                        activityInfo.name,
                        getString(activityInfo.labelRes),
                        getString(activityInfo.descriptionRes)));
            }
        }
    }

    static class DemoInfo {
        private String name;
        private String label;
        private String description;

        public DemoInfo(String name, String label, String description) {
            this.name = name;
            this.label = label;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DemoInfo)) {
                return false;
            }

            DemoInfo info = (DemoInfo) obj;

            if (!Objects.equals(this.name, info.name)) {
                return false;
            }
            if (!Objects.equals(this.label, info.label)) {
                return false;
            }
            if (!Objects.equals(this.description, info.description)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hash = name != null ? name.hashCode() : 0;
            hash = 31 * hash + (label != null ? label.hashCode() : 0);
            hash = 31 * hash + (description != null ? description.hashCode() : 0);
            return hash;
        }
    }

    private class DemoActivityAdapter extends ListAdapter<DemoInfo, DemoInfoHolder> {

        private int mType;

        DemoActivityAdapter(String type) {
            super(new DiffUtil.ItemCallback<DemoInfo>() {
                @Override
                public boolean areItemsTheSame(@NonNull DemoInfo oldItem, @NonNull DemoInfo newItem) {
                    return oldItem.name.equals(newItem.name);
                }

                @Override
                public boolean areContentsTheSame(@NonNull DemoInfo oldItem, @NonNull DemoInfo newItem) {
                    return oldItem.equals(newItem);
                }
            });
            if (TextUtils.isEmpty(type)) {
                mType = DemoInfoHolder.ITEM_TYPE_GROUP;
                submitList(mDemoTypes);
            } else {
                mType = DemoInfoHolder.ITEM_TYPE_ENTRY;
                submitList(mDemoMap.get(type));
            }
        }

        @NonNull
        @Override
        public DemoInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DemoInfoHolder(MainActivity.this, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull DemoInfoHolder holder, int position) {
            holder.bindView(getItem(position));
        }

        @Override
        public int getItemViewType(int position) {
            return mType;
        }
    }

    static class DemoInfoHolder<T extends DemoInfo> extends RecyclerView.ViewHolder {

        private static final int ITEM_TYPE_GROUP = 0;
        private static final int ITEM_TYPE_ENTRY = 1;

        private int mViewType;

        private TextView mTitle;
        private TextView mSubTitle;

        private Context mContext;

        DemoInfoHolder(Context context, ViewGroup pParent, int pViewType) {
            super(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, pParent, false));
            mViewType = pViewType;
            mContext = context;

            mTitle = itemView.findViewById(android.R.id.text1);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

            mSubTitle = itemView.findViewById(android.R.id.text2);
            mSubTitle.setTextColor(Color.GRAY);

        }

        void bindView(T pItem) {
            if (mViewType == ITEM_TYPE_GROUP) {
                setupItemGroup(pItem);
            } else if (mViewType == ITEM_TYPE_ENTRY) {
                setupItemEntry(pItem);
            }
        }

        private void setupItemGroup(final T pItem) {
            mTitle.setText(pItem.getLabel());
            mSubTitle.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(mContext, Class.forName(pItem.getName()));
                        intent.putExtra(DEMO_TYPE, pItem.getLabel());
                        mContext.startActivity(intent);
                    } catch (ClassNotFoundException pE) {
                        pE.printStackTrace();
                    }
                }
            });
        }

        private void setupItemEntry(final T pItem) {
            mTitle.setText(pItem.getLabel());
            mSubTitle.setText(pItem.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(mContext, Class.forName(pItem.getName()));
                        mContext.startActivity(intent);
                    } catch (ClassNotFoundException pE) {
                        pE.printStackTrace();
                    }
                }
            });
        }
    }
}

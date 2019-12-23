package com.tencent.map.vector.demo;

import android.os.Bundle;

public abstract class AbsListActivity extends AbsActivity {

    protected int getLayoutId() {
        return R.layout.activity_recycler_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }
}

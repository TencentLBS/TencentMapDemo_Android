package com.tencent.map.vector.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.tencent.tencentmap.mapsdk.maps.TencentMap;

public abstract class AbsTestCaseMapActivity extends AbsMapActivity {

    protected FrameLayout mBottomLayout;
    protected LinearLayout mLeftLayout;
    protected Button mBtnOpt;

    public abstract static class TestCase {
        public String optName;

        public TestCase(String name) {
            optName = name;
        }

        public abstract void doOpt(final TencentMap tencentMap);

        public abstract void backOpt(final TencentMap tencentMap);
    }

    private TestCase thisUsingTestCase;

    protected abstract TestCase[] getTestCases();

    protected void showCasePanel() {
        mLeftLayout = findViewById(R.id.view_left_opt);
        mLeftLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState, final TencentMap tencentMap) {
        super.onCreate(savedInstanceState, tencentMap);

        mLeftLayout = findViewById(R.id.view_left_opt);
        mBottomLayout = findViewById(R.id.view_bottom_opt);
        mBottomLayout.setBackgroundColor(0x99000000);
        mBottomLayout.setVisibility(View.INVISIBLE);
        mBottomLayout.setClickable(true);
        mLeftLayout.setVisibility(View.INVISIBLE);
        TestCase[] thisTestCases = getTestCases();
        if (thisTestCases == null || thisTestCases.length == 0) {
            return;
        }

        Button chooseCase = new Button(this);
        chooseCase.setText("选择测试用例");
        PopupMenu popupMenu = new PopupMenu(this, chooseCase);

        for (int i = 0; i < thisTestCases.length; i++) {
            popupMenu.getMenu().add(0, i, 0, thisTestCases[i].optName);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                if (item.getItemId() >= 0 && item.getItemId() < thisTestCases.length) {
                    thisUsingTestCase = thisTestCases[item.getItemId()];

                    if (mBtnOpt == null) {
                        mBtnOpt = new Button(AbsTestCaseMapActivity.this);
                        mLeftLayout.addView(mBtnOpt);
                        mBtnOpt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                mBtnOpt.setEnabled(false);
                                mBottomLayout.setVisibility(View.VISIBLE);
                                thisUsingTestCase.doOpt(tencentMap);
                            }
                        });
                    }
                    mBtnOpt.setEnabled(true);
                    mBtnOpt.setVisibility(View.VISIBLE);
                    mBtnOpt.setText(thisUsingTestCase.optName);
                    mBtnOpt.performClick();

                    return true;
                }
                return false;
            }
        });

        chooseCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                popupMenu.show();
            }
        });

        mLeftLayout.addView(chooseCase);

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (thisUsingTestCase != null) {
                    if (!mBtnOpt.isEnabled()) {
                        thisUsingTestCase.backOpt(tencentMap);
                        mBottomLayout.setVisibility(View.GONE);
                        mBtnOpt.setEnabled(true);
                    } else if(mBtnOpt.getVisibility() != View.GONE){
                        mBtnOpt.setVisibility(View.GONE);
                    } else {
                        thisUsingTestCase = null;
                        setEnabled(false);
                        onBackPressed();
                    }
                }
            }
        });
    }
}

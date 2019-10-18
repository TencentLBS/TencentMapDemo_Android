package com.example.tencentmap.tencentmapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class MainActivity extends AbsListActivity {

    private boolean mIsEnableNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String[] onRequestPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int ret : grantResults) {
            if (ret == PackageManager.PERMISSION_DENIED) {
                mIsEnableNext = false;
                Toast.makeText(this, "授权不成功，无法使用示例", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    protected boolean enableNextActivity() {
        if (!mIsEnableNext) {
            Toast.makeText(this, "授权不成功，无法使用示例", Toast.LENGTH_LONG).show();
        }
        return mIsEnableNext;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}

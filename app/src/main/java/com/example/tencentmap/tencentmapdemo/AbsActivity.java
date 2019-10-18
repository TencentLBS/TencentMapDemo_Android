package com.example.tencentmap.tencentmapdemo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public abstract class AbsActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 0x99FF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        String[] permissions = onRequestPermissions();
        if (permissions != null) {
            List<String> deniedPermissions = new ArrayList<>();
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }

            if (deniedPermissions.size() > 0) {
                requestPermissions(deniedPermissions.toArray(new String[0]), PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    protected String[] onRequestPermissions() {
        return null;
    }
}

package com.tencent.map.vector.demo.basic;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Language;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.OverSeaTileProvider;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class OverseaMapActivity extends SupportMapFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //纽约时代广场海外地图，需Key开通海外位置服务权限
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(40.75797, -73.985542),
                        11,
                        0f,
                        0f));
        //移动地图
        tencentMap.moveCamera(cameraSigma);
        //设置自定义海外图源
        tencentMap.setOverSeaTileProvider(new CustomOverSeaTileProvider());
    }

    class CustomOverSeaTileProvider extends OverSeaTileProvider {
        boolean mIsZhLanguage;
        boolean mIsNight;

        /**
         * 创建海外图源供应
         */
        public CustomOverSeaTileProvider() {
            //设置名称和版本号
            super("custom", 1);
        }

        @Override
        public URL getTileUrl(int x, int y, int zoom) {
            String url = "https://xxxx.com/x=%d&y=%d&z=%d&s=%s&l=&s";
            String formatUrl = String.format(Locale.ENGLISH, url, x, y, zoom, mIsNight ? "dark" : "day", mIsZhLanguage ? "zh" : "en");
            try {
                return new URL(formatUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public boolean onDayNightChange(boolean dayNight) {
            mIsNight = dayNight;
            return true;
        }

        public boolean onLanguageChange(Language language) {
            mIsZhLanguage = (language == Language.zh);
            return true;
        }

        @Override
        public Bitmap getLogo(boolean isNight) {
            AssetManager assetManager = getAssets();
            Bitmap iBitmap = null;
            InputStream is = null;
            try {
                is = assetManager.open("logo.png");
                iBitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                return null;
            } catch (OutOfMemoryError ignored) {
            } finally {
                // IO.safeClose(is);
            }
            return iBitmap;
        }
    }
}


package com.tencent.map.vector.demo.polyline;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.tencent.map.vector.demo.R;
import com.tencent.map.vector.demo.basic.SupportMapFragmentActivity;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @version v1.0
 * @since 2020/6/11
 */
public class MutablePolylineActivity extends SupportMapFragmentActivity {
    private boolean mIsAdd;
    private Polyline mPolyline;

    private final static Object[][] sData = {
            {"更新1"},
            {"更新2"},
            {"重置"},
            {"删除"},
    };

    private List<LatLng> mAppendPoints = new ArrayList<>();
    private int mAppendIndex;

    private Handler sHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1 && mPolyline != null && mAppendIndex < mAppendPoints.size()) {
                mPolyline.appendPoint(mAppendPoints.get(mAppendIndex));
                mAppendIndex++;

                sendEmptyMessageDelayed(1, 300);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_polyline_items, menu);

        MenuItem actionMenu = menu.findItem(R.id.menu_actions);
        SubMenu actionSubMenu = actionMenu.getSubMenu();

        for (int i = 0; i < sData.length; i++) {
            Object[] indoor = sData[i];
            actionSubMenu.add(0, i, 0, String.valueOf(indoor[0]));
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_actions).setVisible(mIsAdd);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (mPolyline != null && itemId >= 0 && itemId < sData.length) {
            if (itemId == 3) {
                mPolyline.remove();
                mIsAdd = false;
                mAppendIndex = 0;
                invalidateOptionsMenu();
            } else if (itemId == 2) {
                mPolyline.setPoints(getPoints(itemId));
            } else {
                mPolyline.appendPoints(getPoints(itemId));
            }
            return true;
        }

        List<Integer> pattern = new ArrayList<>();
        pattern.add(35);
        pattern.add(20);

        PolylineOptions options = new PolylineOptions()
                .addAll(getCreatePoints())
                .width(15);

        switch (item.getItemId()) {
            case R.id.menu_add:
                if (mIsAdd) {
                    break;
                }
                mIsAdd = true;
                mPolyline = tencentMap.addPolyline(options);
                break;
            case R.id.menu_add_anim:
                if (mIsAdd) {
                    break;
                }
                mIsAdd = true;

                mPolyline = tencentMap.addPolyline(options);

                sHandler.sendEmptyMessage(1);
                break;

        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    private static List<LatLng> getCreatePoints() {
        List<LatLng> listPoints = new ArrayList<>();
        addLine(listPoints);
        return listPoints;
    }

    private static List<LatLng> getPoints(int itemId) {
        List<LatLng> listPoints = new ArrayList<LatLng>();
        if (itemId == 0) {
            addLine1(listPoints);
        } else if (itemId == 1) {
            addLine2(listPoints);
        } else if (itemId == 2) {
            addLine(listPoints);
        }

        return listPoints;
    }

    private static void addLine(List<LatLng> listPoints) {
        listPoints.add(new LatLng(39.981787, 116.306649));
        listPoints.add(new LatLng(39.982021, 116.306739));
        listPoints.add(new LatLng(39.982351, 116.306883));
        listPoints.add(new LatLng(39.98233, 116.306047));
        listPoints.add(new LatLng(39.982324, 116.305867));
        listPoints.add(new LatLng(39.981918, 116.305885));
        listPoints.add(new LatLng(39.981298, 116.305921));
        listPoints.add(new LatLng(39.981091, 116.305939));
        listPoints.add(new LatLng(39.980506, 116.305975));
        listPoints.add(new LatLng(39.980148, 116.306002));
        listPoints.add(new LatLng(39.980121, 116.306002));
        listPoints.add(new LatLng(39.979708, 116.306038));
        listPoints.add(new LatLng(39.979205, 116.306074));
        listPoints.add(new LatLng(39.979205, 116.306074));
        listPoints.add(new LatLng(39.978813, 116.306101));
        listPoints.add(new LatLng(39.978015, 116.306182));
        listPoints.add(new LatLng(39.977299, 116.306227));
        listPoints.add(new LatLng(39.976996, 116.306245));
        listPoints.add(new LatLng(39.976913, 116.306245));
        listPoints.add(new LatLng(39.97597, 116.306308));
        listPoints.add(new LatLng(39.97575, 116.306326));
        listPoints.add(new LatLng(39.97564, 116.306335));
        listPoints.add(new LatLng(39.975178, 116.306371));
        listPoints.add(new LatLng(39.975185, 116.306514));
        listPoints.add(new LatLng(39.97564, 116.306272));
        listPoints.add(new LatLng(39.975633, 116.306272));
        listPoints.add(new LatLng(39.975715, 116.307997));
        listPoints.add(new LatLng(39.975819, 116.311545));
        listPoints.add(new LatLng(39.975936, 116.314878));
        listPoints.add(new LatLng(39.975998, 116.317528));
        listPoints.add(new LatLng(39.976025, 116.318785));
        listPoints.add(new LatLng(39.976108, 116.321714));
        listPoints.add(new LatLng(39.976259, 116.326843));
        listPoints.add(new LatLng(39.976328, 116.328622));
        listPoints.add(new LatLng(39.976397, 116.330356));
        listPoints.add(new LatLng(39.9765, 116.333967));
        listPoints.add(new LatLng(39.976459, 116.341019));
        listPoints.add(new LatLng(39.976473, 116.341674));
        listPoints.add(new LatLng(39.976473, 116.341944));
        listPoints.add(new LatLng(39.976473, 116.342546));
        listPoints.add(new LatLng(39.976479, 116.345295));
        listPoints.add(new LatLng(39.976197, 116.353829));
        listPoints.add(new LatLng(39.976459, 116.369926));
        listPoints.add(new LatLng(39.97672, 116.381353));
    }

    private static void addLine2(List<LatLng> listPoints) {
        listPoints.add(new LatLng(39.91254, 116.41786));
        listPoints.add(new LatLng(39.911258, 116.417905));
        listPoints.add(new LatLng(39.910459, 116.417923));
        listPoints.add(new LatLng(39.908557, 116.418049));
        listPoints.add(new LatLng(39.908337, 116.418058));
        listPoints.add(new LatLng(39.90824, 116.418067));
        listPoints.add(new LatLng(39.90669, 116.418148));
        listPoints.add(new LatLng(39.904795, 116.418283));
        listPoints.add(new LatLng(39.903416, 116.418265));
        listPoints.add(new LatLng(39.901218, 116.418408));
        listPoints.add(new LatLng(39.900805, 116.418417));
        listPoints.add(new LatLng(39.900805, 116.418426));
        listPoints.add(new LatLng(39.901335, 116.417968));
        listPoints.add(new LatLng(39.901342, 116.417968));
        listPoints.add(new LatLng(39.901342, 116.418004));
        listPoints.add(new LatLng(39.901197, 116.418193));
        listPoints.add(new LatLng(39.901204, 116.418426));
        listPoints.add(new LatLng(39.901218, 116.418552));
        listPoints.add(new LatLng(39.901087, 116.418624));
        listPoints.add(new LatLng(39.901053, 116.41884));
        listPoints.add(new LatLng(39.901004, 116.419028));
        listPoints.add(new LatLng(39.900922, 116.419388));
        listPoints.add(new LatLng(39.900839, 116.419774));
        listPoints.add(new LatLng(39.900749, 116.420043));
        listPoints.add(new LatLng(39.900722, 116.420178));
        listPoints.add(new LatLng(39.900667, 116.42034));
        listPoints.add(new LatLng(39.900619, 116.420519));
        listPoints.add(new LatLng(39.900557, 116.420744));
        listPoints.add(new LatLng(39.900515, 116.420915));
        listPoints.add(new LatLng(39.900488, 116.421067));
        listPoints.add(new LatLng(39.900467, 116.421274));
        listPoints.add(new LatLng(39.900467, 116.421301));
        listPoints.add(new LatLng(39.900467, 116.421301));
        listPoints.add(new LatLng(39.900674, 116.428856));
        listPoints.add(new LatLng(39.900681, 116.429287));
        listPoints.add(new LatLng(39.900674, 116.429287));
        listPoints.add(new LatLng(39.900694, 116.429745));
        listPoints.add(new LatLng(39.900736, 116.43173));
        listPoints.add(new LatLng(39.900729, 116.433132));
        listPoints.add(new LatLng(39.900729, 116.433267));
        listPoints.add(new LatLng(39.900743, 116.433545));
    }

    private static void addLine1(List<LatLng> listPoints) {
        listPoints.add(new LatLng(39.976748, 116.382314));
        listPoints.add(new LatLng(39.976851, 116.388045));
        listPoints.add(new LatLng(39.976892, 116.393597));
        listPoints.add(new LatLng(39.976906, 116.394199));
        listPoints.add(new LatLng(39.976906, 116.394298));
        listPoints.add(new LatLng(39.976996, 116.405949));
        listPoints.add(new LatLng(39.977016, 116.407692));
        listPoints.add(new LatLng(39.97701, 116.417564));
        listPoints.add(new LatLng(39.97701, 116.417564));
        listPoints.add(new LatLng(39.977127, 116.417591));
        listPoints.add(new LatLng(39.977127, 116.417582));
        listPoints.add(new LatLng(39.969017, 116.417932));
        listPoints.add(new LatLng(39.968549, 116.417977));
        listPoints.add(new LatLng(39.9666, 116.418094));
        listPoints.add(new LatLng(39.965099, 116.418193));
        listPoints.add(new LatLng(39.963957, 116.418256));
        listPoints.add(new LatLng(39.961533, 116.418301));
        listPoints.add(new LatLng(39.959343, 116.418301));
        listPoints.add(new LatLng(39.95422, 116.418732));
        listPoints.add(new LatLng(39.952375, 116.418858));
        listPoints.add(new LatLng(39.952106, 116.418876));
        listPoints.add(new LatLng(39.95192, 116.418849));
        listPoints.add(new LatLng(39.951693, 116.418696));
        listPoints.add(new LatLng(39.951528, 116.418525));
        listPoints.add(new LatLng(39.951383, 116.41822));
        listPoints.add(new LatLng(39.95128, 116.417941));
        listPoints.add(new LatLng(39.951239, 116.417609));
        listPoints.add(new LatLng(39.951218, 116.417312));
        listPoints.add(new LatLng(39.951218, 116.417088));
        listPoints.add(new LatLng(39.951197, 116.416899));
        listPoints.add(new LatLng(39.951115, 116.416675));
        listPoints.add(new LatLng(39.950984, 116.416513));
        listPoints.add(new LatLng(39.950839, 116.416378));
        listPoints.add(new LatLng(39.950639, 116.41627));
        listPoints.add(new LatLng(39.950426, 116.416217));
        listPoints.add(new LatLng(39.950095, 116.416243));
        listPoints.add(new LatLng(39.948835, 116.416486));
        listPoints.add(new LatLng(39.948697, 116.416486));
        listPoints.add(new LatLng(39.945557, 116.416648));
        listPoints.add(new LatLng(39.941686, 116.416791));
        listPoints.add(new LatLng(39.941005, 116.4168));
        listPoints.add(new LatLng(39.938442, 116.416944));
        listPoints.add(new LatLng(39.936045, 116.417016));
        listPoints.add(new LatLng(39.933662, 116.417142));
        listPoints.add(new LatLng(39.929247, 116.417295));
        listPoints.add(new LatLng(39.927683, 116.417393));
        listPoints.add(new LatLng(39.926553, 116.417438));
        listPoints.add(new LatLng(39.924583, 116.417492));
        listPoints.add(new LatLng(39.924369, 116.417492));
        listPoints.add(new LatLng(39.921779, 116.417573));
        listPoints.add(new LatLng(39.919044, 116.417654));
        listPoints.add(new LatLng(39.917404, 116.417708));
        listPoints.add(new LatLng(39.917287, 116.417717));
        listPoints.add(new LatLng(39.916233, 116.417825));
        listPoints.add(new LatLng(39.913904, 116.417807));
    }
}

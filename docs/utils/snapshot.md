## 截图

```java
/**
 * 重写截图的回调函数
 */
 public TencentMap.SnapshotReadyCallback snapshotReadyCallback = new TencentMap.SnapshotReadyCallback(){
     @Override
     public void onSnapshotReady(Bitmap snapshot) {
        // 存储Bitmap等操作
        imgView.setImageBitmap(snapshot);
     }
 };

 private Runnable runScreenShot = new Runnable() {

     @Override
     public void run() {
         tencentMap.snapshot(snapshotReadyCallback, Bitmap.Config.ARGB_8888);
     }

 };
 // 回调函数包含UI操作，要在主线程运行
 Handler handScreen = new Handler();
 handScreen.postDelayed(runScreenShot, 2000);
```




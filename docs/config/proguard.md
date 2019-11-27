### 混淆配置

如果需要混淆您的工程，请在module里找到proguard-rules.pro文件，添加如下混淆脚本：

```
-keep class com.tencent.tencentmap.**{*;}
-keep class com.tencent.map.**{*;}
-keep class com.tencent.beacontmap.**{*;}
-keep class navsns.**{*;}
-dontwarn com.qq.**
-dontwarn com.tencent.**
```
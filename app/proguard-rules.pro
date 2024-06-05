# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-dontwarn com.tencent.tmsqmsp.**
-keep public class com.tencent.tmsqmsp.**{*;}

-dontwarn com.tencent.tmsbeacon.**
-keep public class com.tencent.tmsbeacon.**{*;}

-dontwarn com.tencent.map.**
-keep public class com.tencent.map.** {*;}

-dontwarn com.tencent.mapsdk.**
-keep public class com.tencent.mapsdk.** {*;}

-dontwarn com.tencent.tencentmap.**
-keep public class com.tencent.tencentmap.** {*;}

-dontwarn com.tencent.lbssearch.**
-keep public class com.tencent.lbssearch.** {*;}
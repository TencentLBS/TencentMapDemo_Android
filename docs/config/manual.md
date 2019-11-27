### 通过拷贝jar包、so库添加SDK

地图SDK压缩包的下载地址如下：[https://lbs.qq.com/android\_v1/log.html](https://lbs.qq.com/android_v1/log.html)

#### 1、解压下载的压缩包并拷贝文件

Android Studio 项目的目录结构如下图所示：

![项目目录](/config/images/manual_project_dir.jpg)

压缩包解压后的文件目录结构如下图所示：

![SDK 目录](/config/images/manual_sdk_dir.jpg)

1. 将 lib 目录下的 "\*.jar" 文件拷贝到 Android Studio 项目对应的 app/libs/ 文件夹下。
2. 将jniLibs目录下的所有文件按照原目录格式，拷贝到Android Studio项目对应的app/src/main/jniLibs/目录下。

#### 2、修改配置

在app module的build.gradle里修改dependencies，在项目中的位置如下图所示：  

![module gradle](/config/images/manual_module_gradle_dir.jpg)

添加如下代码并 rebuild

```
implementation fileTree(dir: 'libs', include: ['*.jar'])
```

最终 app module 的 build.gradle 修改示例如下：
![](/config/images/manual_module_gradle.jpg)
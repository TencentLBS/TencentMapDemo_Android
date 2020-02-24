### 通过Gradle配置maven或jcenter仓库集成SDK
____

#### 1、在项目根目录的 build.gradle 文件中配置 repositories，添加 maven 或 jcenter 仓库地址

Android Studio 默认会在 Project 的 build.gradle 为所有 module 自动添加 jcenter 仓库地址，如果已存在，则不需要重复添加。  
Project的build.gradle文件在Project目录中位置如图所示：  

![maven gradle dir](/config/images/project_gradle_dir.jpg)

配置如下：

```
allprojects {
    repositories {
        google()
        jcenter() 
    }
 }
```

#### 2、在主工程app module的build.gradle文件配置dependencies

推荐开发者使用第二种方式集成SDK。  
app module的build.gradle文件在Project目录中位置：

![module gradle dir](/config/images/module_gradle_dir.jpg)

以腾讯地图的demo工程为例，添加地图SDK的配置如下：

![module gradle dir](/config/images/module_gradle.jpg)

如需引入指定版本SDK（所有SDK版本号与官网发版一致），则在app module的build.gradle中修改maven仓库版本号即可，如下图所示（4.2.4版本）：

```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.tencent.map:tencent-map-vector-sdk:4.2.4'
}
```

#### 注意事项：

依照上述方法集成SDK以后，就不需要使用第一种方式在libs文件夹下导入对应SDK的 so 和 jar 包，否则会出现类冲突。


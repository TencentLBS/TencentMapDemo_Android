## key配置

要正常使用腾讯地图SDK用户需要在[https://lbs.qq.com/console/key.html](https://lbs.qq.com/console/key.html)申请开发密钥：

![申请key](/config/images/apply_key.png)

Key的设置如下图所示：

![设置key](/config/images/config_key.png)

其中地图SDK的对应位置，应传入对应 App 的包名，保存设置即可。 开发者申请key后，把Key输入工程的AndroidManifest.xml文件中，在application节点里，添加名称为TencentMapSDK的meta，如下所示\(value值为申请的key\)：

```xml
<application
        <meta-data
        android:name="TencentMapSDK"
        android:value="*****-*****-*****-*****-*****-*****"/>
</application>
```



以腾讯地图的demo工程为例，AndroidManifest.xml 的权限配置与 Key 配置的示例如下图所示：

![](/config/images/config_project_key.png)


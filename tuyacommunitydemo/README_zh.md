# 涂鸦智慧社区 App SDK Android 版示例

---

## 简介
涂鸦智慧社区 App SDK Android 版是一套针对智慧社区提供的移动端开发工具。通过智慧社区 SDK，Android 开发者可以基于 SDK 快速实现社区相关的 App 功能开发。

## 什么是涂鸦智慧社区

涂鸦智慧社区基于社区业务服务，对小区基础信息智能实时同步更新，构建以住户（人）为核心，关联房屋、地址、关系等关联的社区运行数据，适用于各类新建小区、老旧小区改造项目，为其提供从业主端到软件控制端的一套完整方案服务。

涂鸦智慧社区解决方案为客户后期的产品维护与运营提供数字化、可视化的管理平台，帮助客户实现商业智能化，降低管理成本。

涂鸦智慧社区解决方案的能力特色：

+ 低成本的即开即用方案

+ 小区基础数据管理

+ 项目、空间、资产数据管理

+ 居民多维度数据管理

+ 完善的组织、住户、空间模型，赋能人员权限来操作不同业务，进行精细化管控

+ 快速增长的强大生态系统

+ 可持续的增值服务

## 快速集成

### 集成SDK

- 如果您还未安装 Android Studio，请访问 [安卓官网](https://developer.android.com/studio) 进行下载安装。

#### 第一步：创建 Android 工程

在 Android Studio 中新建工程。

#### 第二步：配置 build.gradle 文件

在安卓项目的 `build.gradle` 文件里，添加集成准备中下载的 `dependencies` 依赖库。

```java
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "arm64-v8a"
        }
    }
    packagingOptions {
        pickFirst 'lib/*/libc++_shared.so' // 多个aar存在此so，需要选择第一个
    }
}
dependencies {
	implementation 'com.tuya.smart:tuyacommunitysdk:1.0.6'
	implementation 'com.facebook.soloader:soloader:0.8.0'
	implementation 'com.alibaba:fastjson:1.1.67.android'
	implementation 'com.squareup.okhttp3:okhttp-urlconnection:3.12.3'
	implementation "androidx.annotation:annotation:1.0.0"
}
```

在根目录的 `build.gradle` 文件，中增加涂鸦 IoT Maven 仓库地址，进行仓库配置。

```java
repositories {
    maven {url "https://maven-other.tuya.com/repository/maven-releases/"}
}
```

<a id="bmp&keySetting"></a>

#### 第三步：集成安全图片和设置 Appkey 和 AppSecret

- 在 [App 工作台](https://iot.tuya.com/oem/sdkList)，找到您创建的 SDK。
- 在 **获取密钥** 中，点击 **下载安全图片** > **安全图片下载** 下载安全图片。
- 图中**证书**部分需要填写您需要接入应用的SHA256值，否则将不能使用。

	<img alt="Get Key" src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1629354107c0dedbc5e3a.png" width="800">

- 将下载的安全图片命名为 `t_s.bmp`，放置到工程目录的 `assets` 文件夹下。

	<img alt="Get Key" src="https://images.tuyacn.com/fe-static/docs/img/2de282a3-5498-479e-bb31-3688e3ac1eb2.png" width="300">

- 返回安卓项目，在 `AndroidManifest.xml` 文件里配置 appkey 和 appSecret，在配置相应的权限等。

	```java
	<meta-data
		android:name="TUYA_SMART_APPKEY"
		android:value="应用 Appkey" />
	<meta-data
		android:name="TUYA_SMART_SECRET"
		android:value="应用密钥 AppSecret" />
	```

#### 第四步：混淆配置

在 `proguard-rules.pro` 文件配置相应混淆配置。

```java
#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.tuya.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.tuya.smart.mqttclient.mqttv3.**

#OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class com.tuya.**{*;}
-dontwarn com.tuya.**
```

#### 第五步：初始化 SDK

您需要在 `Application` 的主线程中初始化 SDK，确保所有进程都能初始化。示例代码如下：

```java
public class TuyaSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TuyaCommunitySdk.init(this);
    }
}
```

> `appKey` 和 `appSecret` 可以配置在 `AndroidManifest.xml` 文件里，也可以在初始化代码里初始化。
>
> ```java
> TuyaCommunitySdk.init(Application application, String appkey, String appSerect)
> ```

#### 第六步：开启或关闭 SDK 的日志开关

- 在 debug 模式下，您可以开启 SDK 的日志开关，查看更多的日志信息，帮助您快速定位问题。
- 在 release 模式下，建议关闭日志开关。

```java
TuyaCommunitySdk.setDebugMode(true);
```

#### 运行 Demo App

> **注意**：在完成快速集成 SDK 后，您将获取到 SDK 使用的 `AppKey`、 `AppSecret`、安全图片信息。集成 SDK 时请确认 `AppKey`、`AppSecret`、安全图片是否与平台上的信息一致，任意一个不匹配会导致 SDK 无法使用。详细操作，请参考 [第三步：集成安全图片和设置 Appkey 和 AppSecret](#bmp&keySetting)。


Demo App 演示了 App SDK 的开发流程。在开发 App 之前，建议您先按照以下流程完成 Demo App 的操作。

#### Demo App 介绍

##### Demo App 主要功能包括：

- 账号管理功能
- 设备入网功能
- 创建家庭功能
- 家庭管理功能

##### Demo 示意图

<img alt="Demo app" src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16371988290423cc5e691.png" width="600">

##### 运行 Demo

1. 替换 `app` 目录下 `build.gradle` 文件中的 `applicationId` 为您的应用包名。

   ![快速集成 (Android)](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1631151740258917c28d9.png)

2. 确认您已经完成了 [第三步：集成安全图片和设置 Appkey 和 AppSecret](#bmp&keySetting)。

3. 然后点击运行，运行 Demo。

#### 故障排查

##### API 接口请求提示签名错误

- **问题现象**：运行 Demo 时提示以下错误：

  ```java
  {
  	"success": false,
  	"errorCode" : "SING_VALIDATE_FALED",
  	"status" : "error",
  	"errorMsg" : "Permission Verification Failed",
  	"t" : 1583208740059
  }
  ```

- **解决方法**：

  - 请检查您的 AppKey 、AppSecret 和 安全图片是否正确配置。
  - 安全图片是否放在正确目录，文件名是否为 `t_s.bmp`。
  - 将项目clean之后重新build。

##### 运行 Demo 报错非法客户端

- **问题现象**：为什么在涂鸦 IoT 平台为配置了 SHA256，然后直接运行 Demo 还是报错 非法客户端？

- **解决方法**：直接运行 Demo 需要在 app 模块的 build.gradle 中配置您自己的签名信息：

```java
android {
		...
		signingConfigs {
		debug {
			storeFile file('xxxx.jks')
			storePassword 'xxxxxx'
			keyAlias 'xxx'
			keyPassword 'xxxxxx'
		}
		release {
			storeFile file('xxxx.jks')
			storePassword 'xxxxxx'
			keyAlias 'xxx'
			keyPassword 'xxxxxx'
		}
  }
}
```

#### 技术支持

- Tuya IoT 开发者平台: https://developer.tuya.com/en/
- Tuya 开发者帮助中心: https://support.tuya.com/en/help
- Tuya 工单系统: https://service.console.tuya.com/


# Smart Community App SDK Sample for Android

[English](./README.md) | [中文文档](./README_zh.md)

## Overview

Tuya **Smart Community App SDK for Android** is used to develop mobile apps for Smart Community PaaS. Based on the SDK, you can quickly implement app functionality during app development on Android.

## What is Tuya Smart Community?

Tuya Smart Community PaaS enables a variety of features based on community services. For example, update basic information of a community in real time, and build unified community management databases with households (people) as the core. Houses, addresses, vehicles, relationships, and other details are linked by household in the databases. These features apply to newly-built communities and renovation projects of old communities. Integral services are provided for purposes ranging from benefiting owners to facilitating software control.

Tuya's smart community solutions support digital and visualized platform-based operations and maintenance (O&M) for your smart products. This accelerates the development of smart community projects and minimizes your management costs.

## Advantages

Tuya's smart community solutions combine the following advantages:

- Out-of-the-box solutions with best price performance
- Manage the basic data of communities.
- Manage the data of projects, spaces, and assets.
- Manage multi-dimensional data of residents.
- Build comprehensive models of organizations, households, and spaces, and grant permissions to different service roles to achieve precision community management.
- A robust and fast-growing ecosystem
- Sustainable value-added services

## Fast integration with Smart Community App SDK for Android

Before you start, make sure that you have performed the steps in [Preparation](https://developer.tuya.com/en/docs/app-development/android-preparation?id=Kavxruqfd1j4k). If you have not installed Android Studio, visit the [Android Studio official website](https://developer.android.com/studio) to download Android Studio.

### Step 1: Create an Android project

Create a project in Android Studio.

### Step 2: Configure build.gradle

Add `dependencies` to the `build.gradle` file of the Android project.

```java
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "arm64-v8a"
        }
    }
    packagingOptions {
        pickFirst 'lib/*/libc++_shared.so' // This `.so` file exists in multiple `.aar` files. Select the first one.
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

Add the Tuya IoT Maven repository URL to the `build.gradle` file in the root directory.

```java
repositories {
    maven {url "https://maven-other.tuya.com/repository/maven-releases/"}
}
```



### Step 3: Configure the security image, AppKey, and AppSecret

- Log in to the Tuya IoT Development Platform, go to the [SDK Development](https://iot.tuya.com/oem/sdkList) page, and then click the SDK to be managed.

- On the page that appears, click the **Get Key** tab and click **Download** in the **App Security Image for Android** field.

- In the **Certificate** section in the figure, you need to fill in the SHA256 value of the application that you want to access.Otherwise, the application cannot be used.

<img alt="Countdown picker" src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/162935428707a87f44ab0.png" width="800">

- Rename the security image as `t_s.bmp` and put the image in the `assets` folder of your project.

<img alt="Get Key" src="https://images.tuyacn.com/fe-static/docs/img/2de282a3-5498-479e-bb31-3688e3ac1eb2.png" width="300">

- Return to the Android project, configure `appkey` and `appSecret` in `AndroidManifest.xml`, and then set permissions for the app.

```java
<meta-data
	android:name="TUYA_SMART_APPKEY"
	android:value="AppKey" />
<meta-data
	android:name="TUYA_SMART_SECRET"
	android:value="AppSecret" />
```

### Step 4: Obfuscate the code

Configure obfuscation in `proguard-rules.pro`.

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

### Step 5: Initialize the SDK

Initialize the SDK in `Application`. Make sure that all processes are initialized. Example:

```java
public class TuyaSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TuyaCommunitySdk.init(this);
    }
}
```

Configure `appkey` and `appSecret` in `AndroidManifest.xml`, or run the initialization code.

```java
 TuyaCommunitySDK.init(Application application, String appkey, String appSerect)
```

### Step 6: Enable or disable logging

- In debug mode, you can enable SDK logging to facilitate troubleshooting.

- We recommend that you disable logging in release mode.

	```java
	TuyaCommunitySdk.setDebugMode(true);
	```

### Run the demo app

> **Note**: After you integrate the SDK, you can get the security image, `AppKey`, and `AppSecret`. Make sure that the security image, `AppKey`, and `AppSecret` are consistent with those used on the Tuya IoT Development Platform. Any mismatch will cause the SDK development to be failed. For more information, see [Step 3: Configure the security image, AppKey, and AppSecret](https://developer.tuya.com/en/docs/app-development/android-fast-integration?id=Kavxsb539odnm#bmp&keySetting).

In the following example, a demo app is used to describe the process of app development with the App SDK. Before the development of your app, we recommend that you run the demo app.

#### Feature overview

The demo app supports the following features:

- User management
- Device pairing
- Home creation
- Home management

**Demo app**:

   <img alt="Demo app" src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16371988290423cc5e691.png" width="600">

#### Run the sample

1. Choose `app` > `build.gradle` and change the value of `applicationId` to your app package name.

   ![Fast Integration with Smart Community App SDK for Android](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1631151740258917c28d9.png)

2. Make sure that you have finished the operations in [Step 3: Configure the security image, AppKey, and AppSecret](https://developer.tuya.com/en/docs/app-development/android-fast-integration?id=Kavxsb539odnm#bmp&keySetting).

3. Click **Run** to run the sample.

### Troubleshoot problems

#### Permission Verification Failed

- **Problem**: When the system runs the demo app, an error message is returned in the following response:

  ```java
  {
  	"success": false,
  	"errorCode" : "SING_VALIDATE_FALED",
  	"status" : "error",
  	"errorMsg" : "Permission Verification Failed",
  	"t" : 1583208740059
  }
  ```

- **Solutions**
   - Check whether your security image, AppKey, and AppSecret are correctly configured and consistent with those obtained in [Preparation](https://developer.tuya.com/en/docs/app-development/android-preparation?id=Kavxruqfd1j4k).

   - Check whether the security image is placed in the correct directory and whether the file name is `t_s.bmp`.

   - Clean and rebuild the project.

#### Illegal client

- **Problem**: After SHA256 hash values are configured on the Tuya IoT Development Platform and the sample is run, the error message saying **illegal client** is still returned. How do I troubleshoot the problem?

- **Solution**: Before you run the sample, add the following signature information to `build.gradle` of the app module.

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

#### Technical support

- Tuya IoT Developer Platform: https://developer.tuya.com/en/
- Help Center: https://support.tuya.com/en/help
- Service & Support: https://service.console.tuya.com/
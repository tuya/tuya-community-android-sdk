package com.tuya.community.business.sdk.demo;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuyasmart.stencil.app.ApiConfig;

/**
 * create by nielev on 2020/11/11
 */
public class TuyaApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        TuyaCommunitySDK.setDebugMode(true);
        TuyaCommunitySDK.init(this, "应用 Appkey",
                "应用密钥 AppSecret");
    }

}

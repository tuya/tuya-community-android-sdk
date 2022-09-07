package com.tuya.community.business.sdk.demo.utils;

import android.app.Activity;
import android.content.Context;

import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.login.activity.LoginActivity;
import com.tuya.community.business.sdk.demo.app.Constant;


public class LoginHelper {


    public static void afterLogin() {

    }

    public static void reLogin(Context context) {
        reLogin(context, true);
    }

    public static void reLogin(Context context, boolean tip) {
        onLogout(context);
        if (tip) {
            ToastUtil.shortToast(context, R.string.login_session_expired);
        }
        ActivityUtils.gotoActivity((Activity) context, LoginActivity.class, ActivityUtils.ANIMATE_FORWARD, true);
    }

    private static void onLogout(Context context) {
        exit(context);
    }

    public static void exit(Context context) {
        Constant.finishActivity();
    }
}

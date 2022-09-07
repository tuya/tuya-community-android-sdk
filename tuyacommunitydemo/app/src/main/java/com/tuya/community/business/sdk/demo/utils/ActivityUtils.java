package com.tuya.community.business.sdk.demo.utils;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;

import com.tuya.community.business.sdk.demo.R;


public class ActivityUtils {
    public static final int ANIMATE_FORWARD = 0;
    public static final int ANIMATE_BACK = 1;

    public static void gotoActivity(Activity from, Class<? extends Activity> clazz, int direction, boolean finished) {
        if (clazz == null) return;
        Intent intent = new Intent();
        intent.setClass(from, clazz);
        startActivity(from, intent, direction, finished);
    }

    public static void gotoLauncherActivity(Activity activity, int direction, boolean finished) {
        Intent intent = new Intent(activity, LauncherActivity.class);
        startActivity(activity, intent, direction, finished);
    }

    public static void startActivity(Activity activity, Intent intent, int direction, boolean finishLastActivity) {
        if (activity == null) return;
        activity.startActivity(intent);
        if (finishLastActivity) activity.finish();
        overridePendingTransition(activity, direction);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int backCode, int direction, boolean finishLastActivity) {
        if (activity == null) return;
        activity.startActivityForResult(intent, backCode);
        if (finishLastActivity) activity.finish();
        overridePendingTransition(activity, direction);
    }

    public static void back(Activity activity) {
        activity.finish();
        overridePendingTransition(activity, ANIMATE_BACK);
    }

    public static void back(Activity activity, int direction) {
        activity.finish();
        overridePendingTransition(activity, direction);
    }

    public static void overridePendingTransition(Activity activity, int direction) {
        if (direction == ANIMATE_FORWARD) {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (direction == ANIMATE_BACK) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            //do nothing
        }
    }

}

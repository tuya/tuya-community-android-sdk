package com.tuya.community.business.sdk.demo.utils;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.app.AlertDialog;

import com.tuya.community.business.sdk.demo.R;


public class UIFactory {

    public static AlertDialog.Builder buildAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert);
        return dialog;
    }

    public static AlertDialog.Builder buildSmartAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert_NoTitle);
        return dialog;
    }

    public static AlertDialog.Builder buildMultichoiceAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert_Multichoice);
        return dialog;
    }

    public static AlertDialog.Builder buildSinglechoiceAlertDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.Dialog_Alert_Singlechoice);
        return dialog;
    }

    private static Typeface mFontTtf;

    public static Typeface getFontTtf(Context ctx) {
        if (null == mFontTtf) {
            mFontTtf = Typeface.createFromAsset(ctx.getAssets(), "font/iconfont.ttf");
        }
        return mFontTtf;
    }

}

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.tuya.community.business.sdk.demo.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.System;

import com.tuya.smart.sdk.TuyaBaseSdk;

public class TimeUtil {
    public static final String TAG = "TimeUtil";

    public TimeUtil() {
    }

    public static boolean is24Hour(Context context) {
        ContentResolver cv = context.getContentResolver();
        String strTimeFormat = System.getString(cv, "time_12_24");
        return strTimeFormat != null && strTimeFormat.equals("24");
    }

    public static String getNowTime(long time) {
        return is24Hour(TuyaBaseSdk.getApplication()) ? CommonUtil.formatDate(time, "yyyy-MM-dd HH:mm:ss") : CommonUtil.formatDate(time, "yyyy-MM-dd hh:mm:ss");
    }
}


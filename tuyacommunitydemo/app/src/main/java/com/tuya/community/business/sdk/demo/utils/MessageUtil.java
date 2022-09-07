package com.tuya.community.business.sdk.demo.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.os.Message;

import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.network.http.BusinessResponse;

/** @deprecated */
@Deprecated
public class MessageUtil {
    public MessageUtil() {
    }

    public static Message getCallFailMessage(int msgWhat, BusinessResponse businessResponse) {
        return getCallFailMessage(msgWhat, businessResponse.getErrorCode(), businessResponse.getErrorMsg());
    }

    public static Message getCallFailMessage(int msgWhat, String errorCode, String errorMsg) {
        Message msg = getMessage(msgWhat);
        Result result = new Result();
        result.error = errorMsg;
        result.errorCode = errorCode;
        msg.obj = result;
        return msg;
    }

    public static Message getCallFailMessage(int msgWhat, String errorCode, String errorMsg, Object resultObj) {
        Message msg = getMessage(msgWhat);
        Result result = new Result();
        result.error = errorMsg;
        result.errorCode = errorCode;
        result.setObj(resultObj);
        msg.obj = result;
        return msg;
    }

    public static Message getCallFailMessage(int msgWhat, BusinessResponse businessResponse, Object resultObj) {
        return getCallFailMessage(msgWhat, businessResponse.getErrorCode(), businessResponse.getErrorMsg(), resultObj);
    }

    public static Message getMessage(int msgWhat) {
        Message msg = Message.obtain();
        msg.what = msgWhat;
        return msg;
    }

    public static Message getMessage(int msgWhat, int arg1) {
        Message msg = getMessage(msgWhat);
        msg.arg1 = arg1;
        return msg;
    }

    public static Message getMessage(int msgWhat, Object msgObj) {
        Message msg = getMessage(msgWhat);
        msg.obj = msgObj;
        return msg;
    }

    public static Message getMessage(int msgWhat, int arg1, Object msgObj) {
        Message msg = getMessage(msgWhat, msgObj);
        msg.arg1 = arg1;
        return msg;
    }

    public static Message getMessage(int msgWhat, int arg1, int arg2, Object msgObj) {
        Message msg = getMessage(msgWhat, arg1, msgObj);
        msg.arg2 = arg2;
        return msg;
    }

    public static Message getResultMessage(int msgWhat, Object msgObj) {
        Message msg = getMessage(msgWhat);
        Result result = new Result();
        result.setObj(msgObj);
        msg.obj = result;
        return msg;
    }
}

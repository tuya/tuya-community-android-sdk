package com.tuya.community.business.sdk.demo.login.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.sdk.api.IResultCallback;

public class GetVerificationCodeActivity extends BaseActivity {
    private static final String OPERATE_CODE = "operateCode";
    private static final String PHONE_NUMBER = "phoneNumber";

    private int operateCode; // 1、registe 2、forget password
    private String phoneNumber;
    private TextView tv_tip, tv_get_verification_code;
    private EditText et_account;

    public static void actionStart(Context context, int operateCode, String phoneNumber) {
        Intent intent = new Intent(context, GetVerificationCodeActivity.class);
        intent.putExtra(OPERATE_CODE, operateCode);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_verification_code);
        initParams();
        inintView();
        refreshView();
        initLisenter();
    }

    private void initParams() {
        operateCode = getIntent().getIntExtra(OPERATE_CODE, 1);
        phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
    }

    private void inintView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();

        tv_tip = findViewById(R.id.tv_tip);
        tv_get_verification_code = findViewById(R.id.tv_get_verification_code);
        et_account = findViewById(R.id.et_account);

        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = s.toString().trim();
            }
        });
    }

    private void refreshView() {
        if (operateCode == 1) { // registe
            tv_tip.setText(getResources().getString(R.string.register));
        } else {
            tv_tip.setText(getResources().getString(R.string.login_forget));
        }
        if (!TextUtils.isEmpty(phoneNumber)) {
            et_account.setText(phoneNumber);
        }
    }

    private void initLisenter() {
        tv_get_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode();
            }
        });
    }

    private void getVerificationCode() {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showToast(this, getResources().getString(R.string.input_phone_num_please));
            return;
        }
        TuyaOSUser.getUserInstance().sendVerifyCodeWithUserName(phoneNumber
                , ""
                , "86"
                , operateCode == 1 ? 1 : 3, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(GetVerificationCodeActivity.this, error);
                    }

                    @Override
                    public void onSuccess() {
                        ToastUtil.showToast(GetVerificationCodeActivity.this
                                , getResources().getString(R.string.get_verification_code_success));
                        InputVerificationCodeActivity.actionStart(GetVerificationCodeActivity.this, operateCode, phoneNumber);
                    }
                });
    }

    @Override
    public boolean needLogin() {
        return false;
    }
}

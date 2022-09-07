package com.tuya.community.business.sdk.demo.login.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.widget.PhoneCode;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.sdk.api.IResultCallback;

public class InputVerificationCodeActivity extends BaseActivity {
    private static final String OPERATE_CODE = "operateCode";
    private static final String PHONE_NUMBER = "phoneNumber";

    private int operateCode; // 1、registe 2、forget password
    private String phoneNumber;

    private TextView tv_input_verification_tip, tv_send_verification_again;
    private PhoneCode pc_1;

    CountDownTimer countDownTimer = new CountDownTimer(1000L * 60, 1000L) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (null != tv_send_verification_again) {
                tv_send_verification_again.setEnabled(false);
                tv_send_verification_again.setText(getResources().getString(R.string.resend) + "（" + millisUntilFinished / 1000 + "）");
            }
        }

        @Override
        public void onFinish() {
            if (null != tv_send_verification_again) {
                tv_send_verification_again.setEnabled(true);
                tv_send_verification_again.setText(getResources().getString(R.string.resend));
            }
        }
    };

    public static void actionStart(Context context, int operateCode, String phoneNumber) {
        Intent intent = new Intent(context, InputVerificationCodeActivity.class);
        intent.putExtra(OPERATE_CODE, operateCode);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_verification_code);
        initParams();
        initView();
        refreshView();
        initListener();
        startCountDown();
    }

    private void initParams() {
        operateCode = getIntent().getIntExtra(OPERATE_CODE, 1);
        phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
    }

    private void initView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();

        tv_input_verification_tip = findViewById(R.id.tv_input_verification_tip);
        tv_send_verification_again = findViewById(R.id.tv_send_verification_again);
        pc_1 = findViewById(R.id.pc_1);
    }

    private void refreshView() {
        tv_input_verification_tip.setText(getResources().getString(R.string.verification_has_send_tip) + "：86-" + phoneNumber);
    }

    private void initListener() {
        pc_1.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                checkVerificationCode(code);
            }

            @Override
            public void onInput() {

            }
        });

        tv_send_verification_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode();
            }
        });
    }

    private void checkVerificationCode(String code) {
        TuyaOSUser.getUserInstance().checkCodeWithUserName(phoneNumber
                , ""
                , "86"
                , code
                , operateCode == 1 ? 1 : 3
                , new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(InputVerificationCodeActivity.this, error);
                    }

                    @Override
                    public void onSuccess() {
                        EditPasswordActivity.actionStart(InputVerificationCodeActivity.this
                                , phoneNumber
                                , operateCode
                                , code);
                    }
                });
    }

    private void getVerificationCode() {
        TuyaOSUser.getUserInstance().sendVerifyCodeWithUserName(phoneNumber
                , ""
                , "86"
                , operateCode == 1 ? 1 : 3, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(InputVerificationCodeActivity.this, error);
                    }

                    @Override
                    public void onSuccess() {
                        startCountDown();
                        ToastUtil.showToast(InputVerificationCodeActivity.this
                                , getResources().getString(R.string.get_verification_code_success));
                    }
                });
    }

    private void startCountDown() {
        if (null != countDownTimer) {
            countDownTimer.start();
        }
    }

    private void stopCountDown() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCountDown();
    }

    @Override
    public boolean needLogin() {
        return false;
    }
}

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
import com.tuya.community.business.sdk.demo.activity.MainActivity;
import com.tuya.community.business.sdk.demo.app.Constant;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.api.IResetPasswordCallback;
import com.tuya.smart.android.user.bean.User;

import java.util.regex.Pattern;

public class EditPasswordActivity extends BaseActivity {
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String OPERATE_CODE = "operateCode";
    private static final String VERIFICATION_CODE = "verificationCode";

    private String phoneNumber;
    private int operateCode;
    private String verificationCode;

    private EditText et_edit_password;
    private TextView tv_confirm, tv_tip;

    String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

    public static void actionStart(Context context, String phoneNumber, int operateCode, String verificationCode) {
        Intent intent = new Intent(context, EditPasswordActivity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        intent.putExtra(OPERATE_CODE, operateCode);
        intent.putExtra(VERIFICATION_CODE, verificationCode);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        initPamars();
        iniView();
        refreshView();
        initListner();
    }

    private void initPamars() {
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra(PHONE_NUMBER);
        operateCode = intent.getIntExtra(OPERATE_CODE, 1);
        verificationCode = intent.getStringExtra(VERIFICATION_CODE);
    }

    private void iniView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();

        et_edit_password = findViewById(R.id.et_edit_password);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_tip = findViewById(R.id.tv_tip);
    }

    private void refreshView() {
        if (operateCode == 1) {
            tv_tip.setText(getResources().getString(R.string.set_password));
        } else { // reset password
            tv_tip.setText(getResources().getString(R.string.reset_password));
        }
    }

    private void initListner() {
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operateCode == 1) { // register
                    setPassword();
                } else { // reset password
                    resetPassword();
                }
            }
        });

        et_edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < str.length; i++) {
                        sb.append(str[i]);
                    }
                    et_edit_password.setText(sb.toString());
                    et_edit_password.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkoutPassword(s.toString());
            }
        });
    }

    private void setPassword() { // set password
        TuyaOSUser.getUserInstance().registerAccountWithPhone("86"
                , phoneNumber
                , et_edit_password.getText().toString()
                , verificationCode
                , new IRegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        login();
                    }

                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(EditPasswordActivity.this, error);
                    }
                });
    }

    private void resetPassword() { // reset password
        TuyaOSUser.getUserInstance().resetPhonePassword("86"
                , phoneNumber
                , verificationCode
                , et_edit_password.getText().toString()
                , new IResetPasswordCallback() {

                    @Override
                    public void onSuccess() {
                        login();
                    }

                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(EditPasswordActivity.this, error);
                    }
                });
    }

    private void login() {
        TuyaOSUser.getUserInstance().loginWithPhonePassword("86"
                , phoneNumber
                , et_edit_password.getText().toString()
                , new ILoginCallback() {
                    @Override
                    public void onSuccess(User user) {
                        ToastUtil.showToast(EditPasswordActivity.this, "Login success");
                        MainActivity.actionStart(EditPasswordActivity.this);
                        Constant.finishActivity(InputVerificationCodeActivity.class);
                        Constant.finishActivity(GetVerificationCodeActivity.class);
                        Constant.finishActivity(LoginActivity.class);
                        Constant.finishActivity(EditPasswordActivity.class);
                    }

                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(EditPasswordActivity.this, error);
                    }
                });
    }

    private void checkoutPassword(String password) {
        if (TextUtils.isEmpty(password) || !Pattern.matches(regex, password)) {
            tv_confirm.setEnabled(false);
        } else {
            tv_confirm.setEnabled(true);
        }
    }

    @Override
    public boolean needLogin() {
        return false;
    }
}

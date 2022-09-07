package com.tuya.community.business.sdk.demo.login.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.activity.MainActivity;
import com.tuya.community.business.sdk.demo.app.Constant;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;

public class LoginActivity extends BaseActivity {

    private TextView tv_register;
    private EditText et_account;
    private EditText et_password;
    private TextView tv_forget_password;
    private TextView tv_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();

        tv_register = findViewById(R.id.tv_register);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        tv_login = findViewById(R.id.tv_login);
    }

    private void initListener() {
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
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
                    et_password.setText(sb.toString());
                    et_password.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void register() {
        GetVerificationCodeActivity.actionStart(this, 1, et_account.getText().toString().trim());
    }

    private void forgetPassword() {
        GetVerificationCodeActivity.actionStart(this, 2, et_account.getText().toString().trim());
    }

    private void login() {
        TuyaOSUser.getUserInstance().loginWithPhonePassword("86"
                , et_account.getText().toString()
                , et_password.getText().toString()
                , new ILoginCallback() {
                    @Override
                    public void onSuccess(User user) {
                        ToastUtil.showToast(LoginActivity.this, getResources().getString(R.string.login_success));
                        MainActivity.actionStart(LoginActivity.this);
                        Constant.finishActivity(LoginActivity.class);
                    }

                    @Override
                    public void onError(String code, String error) {
                        ToastUtil.showToast(LoginActivity.this, error);
                    }
                });

    }

    @Override
    public boolean needLogin() {
        return false;
    }
}

package com.tuya.community.business.sdk.demo.house;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tuya.community.android.callback.ISuccessFailureCallback;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

public class VerifyInfoActivity extends BaseActivity {

    EditText etName;
    EditText etIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_info);
        etName = findViewById(R.id.editTextTextPersonName);
        etIdCard = findViewById(R.id.editTextTextPersonName2);
        initToolbar();
        setDisplayHomeAsUpEnabled();
        setTitle("verify user info");

        findViewById(R.id.vertify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etIdCard.getText()) && !TextUtils.isEmpty(etIdCard.getText())) {
                    TuyaCommunitySDK.getHouseMemberInstance().verifyUserInfo(etName.getText().toString(),
                            1, etIdCard.getText().toString(), new ISuccessFailureCallback() {
                                @Override
                                public void onSuccess() {
                                    startActivity(new Intent(VerifyInfoActivity.this, ChooseCommunityActivity.class));
                                    finish();
                                }

                                @Override
                                public void onFailure(String s, String s1) {
                                    ToastUtil.shortToast(VerifyInfoActivity.this, s1);
                                }
                            });
                }
            }
        });
    }
}
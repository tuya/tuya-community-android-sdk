package com.tuya.community.business.sdk.demo.house;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.house.bean.TuyaCommunityHouseBean;
import com.tuya.community.android.house.bean.TuyaCommunityMemberTypeBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;

public class AddHouseActivity extends BaseActivity {

    EditText etName;
    String mUserType = "";
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        etName = findViewById(R.id.editTextTextPersonName);
        radioGroup = findViewById(R.id.rg);
        initToolbar();
        setDisplayHomeAsUpEnabled();
        setTitle("add house");

        String communityId = getIntent().getStringExtra("communityId");
        String roomId = getIntent().getStringExtra("roomId");

        TuyaCommunitySDK.getHouseMemberInstance().getMemberTypeList(roomId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityMemberTypeBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityMemberTypeBean> data) {
                for (int i = 0; i < data.size(); i++) {
                    RadioButton radioButton = new RadioButton(AddHouseActivity.this);
                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

                    lp.setMargins(15, 0, 0, 0);
                    radioButton.setPadding(80, 0, 0, 0);
                    radioButton.setText(data.get(i).getMemberTypeName());
                    int finalI = i;
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mUserType = data.get(finalI).getMemberTypeId();
                        }
                    });
                    if (data.get(i).getMemberTypeCode().equals("HOUSEHOLDER")) {
                        mUserType = data.get(i).getMemberTypeId();
                    }
                    radioGroup.addView(radioButton);
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(AddHouseActivity.this, s1);
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUserType)) return;
                TuyaCommunitySDK.getHouseManagerInstance().addHouse(etName.getText().toString(), communityId,
                        roomId, mUserType, "", new ITuyaCommunityResultCallback<TuyaCommunityHouseBean>() {
                            @Override
                            public void onSuccess(TuyaCommunityHouseBean tuyaCommunityHouseBean) {
                                ToastUtil.shortToast(AddHouseActivity.this, "add house success!");
                                finish();
                            }

                            @Override
                            public void onFailure(String s, String s1) {
                                ToastUtil.shortToast(AddHouseActivity.this, s1);
                            }
                        });
            }
        });
    }
}
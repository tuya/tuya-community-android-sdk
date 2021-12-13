package com.tuya.community.business.sdk.demo.house;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.house.anntation.TuyaCommunityGenderType;
import com.tuya.community.android.house.anntation.TuyaCommunityMemberRole;
import com.tuya.community.android.house.bean.TuyaCommunityHouseBean;
import com.tuya.community.android.house.bean.TuyaCommunityMemberTypeBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;

public class AddMemberActivity extends BaseActivity {

    EditText etName, etPhone;
    String mUserType = "";
    RadioGroup radioGroup;
    String communityId;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        etName = findViewById(R.id.editTextTextPersonName);
        etPhone = findViewById(R.id.editTextPhone);
        radioGroup = findViewById(R.id.rg);
        initToolbar();
        setDisplayHomeAsUpEnabled();
        setTitle("add member");

        communityId = getIntent().getStringExtra("communityId");
        roomId = getIntent().getStringExtra("roomId");

        TuyaCommunitySDK.getHouseMemberInstance().getMemberTypeList(roomId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityMemberTypeBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityMemberTypeBean> data) {
                for (int i = 0; i < data.size(); i++) {
                    RadioButton radioButton = new RadioButton(AddMemberActivity.this);
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
                ToastUtil.shortToast(AddMemberActivity.this, s1);
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUserType)) return;
                if (TextUtils.isEmpty(etName.getText())) return;
                if (TextUtils.isEmpty(etPhone.getText())) return;
                TuyaCommunitySDK.getHouseMemberInstance().addMember(communityId, roomId,
                        etName.getText().toString(), TuyaCommunityGenderType.Female, etPhone.getText().toString(),
                         mUserType, TuyaCommunityMemberRole.ROLE_MEMBER,"", new ITuyaCommunityResultCallback<String>() {
                            @Override
                            public void onSuccess(String roomUserId) {
                                ToastUtil.shortToast(AddMemberActivity.this, "add member success!");
                                finish();
                            }

                            @Override
                            public void onFailure(String s, String s1) {
                                ToastUtil.shortToast(AddMemberActivity.this, s1);
                            }
                        });
            }
        });
    }
}
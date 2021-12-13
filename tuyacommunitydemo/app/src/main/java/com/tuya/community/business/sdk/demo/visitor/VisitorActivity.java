package com.tuya.community.business.sdk.demo.visitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.visitor.bean.TuyaCommunityVisitorReasonBean;
import com.tuya.community.android.visitor.enums.TuyaCommunityVisitorFromEnum;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;
import java.util.List;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.ROOM_ID;

public class VisitorActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "VisitorActivity";

    private Button mBtnRecord;
    private String communityId;
    private String roomId;
    private Context mContext;
    private List<TuyaCommunityVisitorReasonBean> reasonList;
    private EditText mEdPhone;
    private Button mBtnSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        mContext = this;
        initData();
        initView();
    }


    private void initView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();
        TextView title = findViewById(R.id.tv_toolbar_title);
        title.setText(getResources().getString(R.string.visitor));
        mBtnSub = findViewById(R.id.btn_sub_visitor_info);
        mEdPhone = findViewById(R.id.ed_visitor_phone);
        mBtnRecord = findViewById(R.id.btn_record);
        mBtnRecord.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);


    }

    private void initData() {
        communityId = getIntent().getStringExtra(COMMUNITY_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        getReasonList();
        getCarCongif();
    }


    public void getReasonList() {
        TuyaCommunitySDK.getVisitorInstance().getVisitorReasonList(communityId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityVisitorReasonBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityVisitorReasonBean> tuyaCommunityVisitorReasonBeans) {
                if (tuyaCommunityVisitorReasonBeans != null && tuyaCommunityVisitorReasonBeans.size() > 0)
                    reasonList = new ArrayList<>(tuyaCommunityVisitorReasonBeans);
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);
            }
        });

    }

    private void submitVisitorInfo() {
        if (TextUtils.isEmpty(mEdPhone.getText().toString())) {
            ToastUtil.shortToast(mContext, getResources().getString(R.string.input_visitor_phone));
            return;
        }
        if (reasonList == null || reasonList.size() == 0) {
            ToastUtil.shortToast(mContext, getResources().getString(R.string.visit_reason_empty));
            return;
        }

        TuyaCommunitySDK.getVisitorInstance().createPass(communityId, "测试", mEdPhone.getText().toString(), 1, reasonList.get(0).getVisitorReasonId(), System.currentTimeMillis(), System.currentTimeMillis() + 3600000, roomId, 2, "", TuyaCommunityVisitorFromEnum.Owner, new ITuyaCommunityResultCallback<String>() {
            @Override
            public void onSuccess(String s) {
                ToastUtil.shortToast(mContext, getResources().getString(R.string.visitor_success));
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);

            }
        });
    }


    public void getCarCongif() {
        TuyaCommunitySDK.getVisitorInstance().getCarConfig(communityId, new ITuyaCommunityResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.i(TAG, "onSuccess: " + aBoolean);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i(TAG, "onFailure: " + s1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_record) {
            Intent intent = new Intent(this, VisitorRecordListActivity.class);
            intent.putExtra(COMMUNITY_ID, communityId);
            intent.putExtra(ROOM_ID, roomId);
            startActivity(intent);

        } else if (v.getId() == R.id.btn_sub_visitor_info) {
            submitVisitorInfo();
        }

    }
}

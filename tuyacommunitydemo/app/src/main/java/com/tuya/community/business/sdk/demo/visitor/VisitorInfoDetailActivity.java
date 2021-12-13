package com.tuya.community.business.sdk.demo.visitor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.visitor.bean.TuyaCommunityVisitorInfoBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.VISITOR_ID;

public class VisitorInfoDetailActivity extends BaseActivity {
    private Context mContext;
    private TextView mTvName;
    private TextView mTvSex;
    private TextView mTvPhone;
    private TextView mTvCarNum;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private TextView mTvReason;
    private TextView mTvAddress;
    private TextView mTvStatus;
    private Button mBtnCancel;
    private String communityId;
    private String visitorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_visitor_detail);
        initView();
        initData();
    }

    private void initView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();
        TextView title = findViewById(R.id.tv_toolbar_title);
        title.setText(getResources().getString(R.string.visitor_info));
        mTvName = findViewById(R.id.tv_name);
        mTvSex = findViewById(R.id.tv_sex);
        mTvPhone = findViewById(R.id.tv_phone);
        mTvCarNum = findViewById(R.id.tv_car_num);
        mTvStartTime = findViewById(R.id.tv_start_time);
        mTvEndTime = findViewById(R.id.tv_end_time);
        mTvReason = findViewById(R.id.tv_reason);
        mTvAddress = findViewById(R.id.tv_address);
        mBtnCancel = findViewById(R.id.btn_visitor_cancel);
        mTvStatus = findViewById(R.id.tv_visitor_detail_status);
        mBtnCancel.setVisibility(View.GONE);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePass();
            }
        });

    }

    private void initData() {
        communityId = getIntent().getStringExtra(COMMUNITY_ID);
        visitorId = getIntent().getStringExtra(VISITOR_ID);
        getVisitorInfo();
    }

    public void deletePass() {

    }

    public void getVisitorInfo() {
        TuyaCommunitySDK.getVisitorInstance().getVisitorPassInfo(communityId, visitorId, new ITuyaCommunityResultCallback<TuyaCommunityVisitorInfoBean>() {
            @Override
            public void onSuccess(TuyaCommunityVisitorInfoBean tuyaCommunityVisitorInfoBean) {
                if (null != tuyaCommunityVisitorInfoBean) {
                    if (tuyaCommunityVisitorInfoBean.getVisitorStatus() == 0) {
                        mTvStatus.setText(mContext.getResources().getString(R.string.not_visitor));
                    } else if (tuyaCommunityVisitorInfoBean.getVisitorStatus() == 1) {
                        mTvStatus.setText(mContext.getResources().getString(R.string.visited));
                    } else if (tuyaCommunityVisitorInfoBean.getVisitorStatus() == 2) {
                        mTvStatus.setText(mContext.getResources().getString(R.string.miss_visitor));
                    }
                    mTvName.setText(tuyaCommunityVisitorInfoBean.getVisitorName());
                    if (tuyaCommunityVisitorInfoBean.getSex() == "1") {
                        mTvSex.setText(getResources().getString(R.string.male));
                    } else {
                        mTvSex.setText(getResources().getString(R.string.female));
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
                    mTvStartTime.setText(simpleDateFormat.format(new Date(tuyaCommunityVisitorInfoBean.getStartTime())));
                    mTvPhone.setText(tuyaCommunityVisitorInfoBean.getVisitorPhone());
                    mTvCarNum.setText(tuyaCommunityVisitorInfoBean.getCarNum());
                    mTvEndTime.setText(simpleDateFormat.format(new Date(tuyaCommunityVisitorInfoBean.getEndTime())));
                    mTvReason.setText(tuyaCommunityVisitorInfoBean.getVisitorReason());
                    mTvAddress.setText(tuyaCommunityVisitorInfoBean.getVisitorAddress());
                    mBtnCancel.setVisibility(tuyaCommunityVisitorInfoBean.getVisitorStatus() == 0 ? View.VISIBLE : View.GONE);
                }

            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);

            }
        });
    }

}

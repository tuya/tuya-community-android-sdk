package com.tuya.community.business.sdk.demo.visitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.visitor.bean.TuyaCommunityVisitorBean;
import com.tuya.community.android.visitor.bean.TuyaCommunityVisitorRecordListBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.SpacesItemDecoration;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;
import java.util.List;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.ROOM_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.VISITOR_ID;

public class VisitorRecordListActivity extends BaseActivity {
    private String communityId;
    private String roomId;
    private Context mContext;
    private RecyclerView mRvRecordList;
    private List<TuyaCommunityVisitorBean> recordList;
    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_record_list);
        mContext = this;
        initData();
        initView();
    }

    private void initData() {
        communityId = getIntent().getStringExtra(COMMUNITY_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        getVisitorRecord();
    }

    private void initView() {
        initToolbar();
        setDisplayHomeAsUpEnabled();
        TextView title = findViewById(R.id.tv_toolbar_title);
        title.setText(getResources().getString(R.string.visitor_list));
        mRvRecordList = findViewById(R.id.rv_record_list);
        mRvRecordList.setLayoutManager(new LinearLayoutManager(this));
        mRvRecordList.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.dp_16), 0, false));
        adapter = new RecordAdapter(mContext);
        mRvRecordList.setAdapter(adapter);
        adapter.setonDetailClickListenter(new RecordAdapter.onDetailClickListenter() {
            @Override
            public void onChoose(View view, int position) {
                Intent intent = new Intent(mContext, VisitorInfoDetailActivity.class);
                intent.putExtra(COMMUNITY_ID, communityId);
                intent.putExtra(VISITOR_ID, recordList.get(position).getVisitorId());
                startActivity(intent);
            }

        });
    }

    private void getVisitorRecord() {
        TuyaCommunitySDK.getVisitorInstance().getVisitorRecordList(communityId, roomId, 1, 15, new ITuyaCommunityResultCallback<TuyaCommunityVisitorRecordListBean>() {
            @Override
            public void onSuccess(TuyaCommunityVisitorRecordListBean tuyaCommunityVisitorRecordListBean) {
                if (tuyaCommunityVisitorRecordListBean.getData() == null || tuyaCommunityVisitorRecordListBean.getData().size() == 0) {
                    ToastUtil.shortToast(mContext, getResources().getString(R.string.no_list));
                } else {
                    adapter.updateData(tuyaCommunityVisitorRecordListBean.getData());
                    recordList = new ArrayList<>(tuyaCommunityVisitorRecordListBean.getData());
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(mContext, s1);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVisitorRecord();
    }
}

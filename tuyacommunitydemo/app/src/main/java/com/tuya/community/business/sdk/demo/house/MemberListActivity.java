package com.tuya.community.business.sdk.demo.house;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.callback.ISuccessFailureCallback;
import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.house.anntation.TuyaCommunityMemberAuditStatus;
import com.tuya.community.android.house.bean.TuyaCommunityMemberBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.house.adapter.MemberListAdapter;
import com.tuya.community.business.sdk.demo.utils.DialogUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.sdk.os.TuyaOSUser;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends BaseActivity {

    private MemberListAdapter mCommunityListAdapter;
    private RecyclerView rvHouseList;
    private ArrayList<TuyaCommunityMemberBean> memberBeanArrayList;
    String communityId;
    String roomId;
    long houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        initToolbar();
        setDisplayHomeAsUpEnabled();
        rvHouseList = findViewById(R.id.rv_house_list);
        setTitle("member list");

        communityId = getIntent().getStringExtra("communityId");
        roomId = getIntent().getStringExtra("roomId");
        houseId = getIntent().getLongExtra("houseId", 0);

        rvHouseList.setLayoutManager(new LinearLayoutManager(this));
        mCommunityListAdapter = new MemberListAdapter();
        rvHouseList.setAdapter(mCommunityListAdapter);
        mCommunityListAdapter.setOnItemClickListener(new MemberListAdapter.OnBusinessClickListener() {
            @Override
            public void onItemClick(TuyaCommunityMemberBean item, int position) {
                boolean isCurrentPass = item.getAudit() == TuyaCommunityMemberAuditStatus.Pass ? true : false;
                boolean isFamilyOwner = false;
                if (null == TuyaOSUser.getUserInstance().getUser()) {
                    return;
                }

                if (getCurrentHouseMember(memberBeanArrayList) != null && !TextUtils.isEmpty(getCurrentHouseMember(memberBeanArrayList).getUserTypeCode()) && "HOUSEHOLDER".equals(getCurrentHouseMember(memberBeanArrayList).getUserTypeCode())) {
                    isFamilyOwner = true;
                }
            }

            @Override
            public void onLongClick(TuyaCommunityMemberBean item, int position) {
                if (item.getAudit() == TuyaCommunityMemberAuditStatus.Pass) {//
                    DialogUtil.simpleConfirmDialog(MemberListActivity.this, "sure move out this member?", new DialogUtil.OnClickListener() {
                        @Override
                        public void onSureClick() {
                            TuyaCommunitySDK.getHouseManagerInstance().moveOutHouse(communityId, item.getRoomUserId(), new ISuccessFailureCallback() {
                                @Override
                                public void onSuccess() {
                                    if (item != null) {
                                        getMemberList();
                                    }
                                }

                                @Override
                                public void onFailure(String s, String s1) {
                                    if (s != null) {

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                } else {
                    DialogUtil.simpleConfirmDialog(MemberListActivity.this, "sure delete this member?", new DialogUtil.OnClickListener() {
                        @Override
                        public void onSureClick() {
                            TuyaCommunitySDK.getHouseMemberInstance().deleteMember(communityId, houseId, item.getRoomUserId(), new ISuccessFailureCallback() {
                                @Override
                                public void onSuccess() {
                                    if (item != null) {
                                        getMemberList();
                                    }
                                }

                                @Override
                                public void onFailure(String s, String s1) {
                                    if (s != null) {

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                }
            }
        });

        TextView add = findViewById(R.id.add_house);
        add.setText("add Member");
        findViewById(R.id.add_house).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberListActivity.this, AddMemberActivity.class);
                intent.putExtra("communityId", communityId);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMemberList();
    }

    private TuyaCommunityMemberBean getCurrentHouseMember(List<TuyaCommunityMemberBean> houseMembers) {
        if (houseMembers == null) {
            return null;
        }
        for (TuyaCommunityMemberBean member : houseMembers) {
            if (TuyaOSUser.getUserInstance().getUser().getUid().equals(member.getUid())) {
                return member;
            }
        }
        return null;
    }

    private void getMemberList() {
        TuyaCommunitySDK.getHouseMemberInstance().getMemberList(communityId, houseId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityMemberBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityMemberBean> tuyaCommunityMemberBeans) {
                if (tuyaCommunityMemberBeans != null) {
                    mCommunityListAdapter.setCommunityList(tuyaCommunityMemberBeans);
                    memberBeanArrayList = new ArrayList<>(tuyaCommunityMemberBeans);

                }
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
}
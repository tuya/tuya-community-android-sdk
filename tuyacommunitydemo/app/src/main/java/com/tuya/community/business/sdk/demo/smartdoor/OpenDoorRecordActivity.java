package com.tuya.community.business.sdk.demo.smartdoor;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.smartdoor.bean.TuyaCommunitySmartDoorOpenRecordBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.smartdoor.adapter.OpenDoorRecordListAdapter;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.ROOM_ID;

public class OpenDoorRecordActivity extends BaseActivity {

    String TAG = "OpenDoorRecordActivity";
    private String communityId;
    private String roomId;
    private RecyclerView rvDoorList;
    private OpenDoorRecordListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door_record);

        initToolbar();
        setDisplayHomeAsUpEnabled(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rvDoorList = findViewById(R.id.rv_house_list);
        setTitle("open door record");

        rvDoorList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new OpenDoorRecordListAdapter();
        rvDoorList.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(new OpenDoorRecordListAdapter.OnBusinessClickListener() {
            @Override
            public void onItemClick(TuyaCommunitySmartDoorOpenRecordBean item, int position) {

            }

            @Override
            public void onLongClick(TuyaCommunitySmartDoorOpenRecordBean item, int position) {

            }
        });

        communityId = getIntent().getStringExtra(COMMUNITY_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        TuyaCommunitySDK.getSmartDoorInstance().getSmartDoorOpenRecords(communityId, roomId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunitySmartDoorOpenRecordBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunitySmartDoorOpenRecordBean> tuyaCommunitySmartDoorInfoBeans) {
                mListAdapter.setCommunityList(tuyaCommunitySmartDoorInfoBeans);
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(OpenDoorRecordActivity.this, TAG + s1);
            }
        });

    }
}
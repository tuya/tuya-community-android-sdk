package com.tuya.community.business.sdk.demo.smartdoor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.smartdoor.bean.TuyaCommunitySmartDoorInfoBean;
import com.tuya.community.android.smartdoor.enums.TuyaCommunitySmartDoorOpenResult;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.smartdoor.adapter.SmartDoorListAdapter;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.ROOM_ID;

public class SmartDoorListActivity extends BaseActivity {

    String TAG = "SmartDoorListActivity";
    private String communityId;
    private String roomId;
    private RecyclerView rvDoorList;
    private SmartDoorListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_door);

        initToolbar();
        setDisplayHomeAsUpEnabled(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle("smart door list");
        rvDoorList = findViewById(R.id.rv_house_list);

        rvDoorList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new SmartDoorListAdapter();
        rvDoorList.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(new SmartDoorListAdapter.OnBusinessClickListener() {
            @Override
            public void onItemClick(TuyaCommunitySmartDoorInfoBean item, int position) {
                TuyaCommunitySDK.getSmartDoorInstance().openDoor(communityId, roomId, item.getDeviceId(), new ITuyaCommunityResultCallback<String>() {
                    @Override
                    public void onSuccess(String accessLogId) {
                        if (!TextUtils.isEmpty(accessLogId)) {
                            loopCheckOpenResult(accessLogId);
                        }
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        ToastUtil.shortToast(SmartDoorListActivity.this, TAG + s1);
                    }
                });
            }

            @Override
            public void onLongClick(TuyaCommunitySmartDoorInfoBean item, int position) {

            }
        });

        communityId = getIntent().getStringExtra(COMMUNITY_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        TuyaCommunitySDK.getSmartDoorInstance().getSmartDoorList(communityId, roomId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunitySmartDoorInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunitySmartDoorInfoBean> tuyaCommunitySmartDoorInfoBeans) {
                mListAdapter.setCommunityList(tuyaCommunitySmartDoorInfoBeans);
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(SmartDoorListActivity.this, TAG + s1);
            }
        });

        findViewById(R.id.door_open_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartDoorListActivity.this, OpenDoorRecordActivity.class);
                intent.putExtra(COMMUNITY_ID, communityId);
                intent.putExtra(ROOM_ID, roomId);
                startActivity(intent);
            }
        });
        findViewById(R.id.community_qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartDoorListActivity.this, CommunityQRCodeActivity.class);
                intent.putExtra(COMMUNITY_ID, communityId);
                intent.putExtra(ROOM_ID, roomId);
                startActivity(intent);
            }
        });
    }


    private void loopCheckOpenResult(final String accessLogId) {
        if (TextUtils.isEmpty(accessLogId)) {
            return;
        }

        TuyaCommunitySDK.getSmartDoorInstance().checkOpenDoorResult(communityId, accessLogId, new ITuyaCommunityResultCallback<TuyaCommunitySmartDoorOpenResult>() {
            @Override
            public void onSuccess(TuyaCommunitySmartDoorOpenResult tuyaCommunitySmartDoorOpenResult) {
                String result = "";//  0:unknow，1:success，2:fail
                result = String.valueOf(tuyaCommunitySmartDoorOpenResult.getValue());
                checkResult(accessLogId, result);
            }

            @Override
            public void onFailure(String s, String s1) {
                checkResult(accessLogId, "0");//  0:未知
            }
        });
    }

    private int loopCount;
    Handler mHandler;

    private void checkResult(final String accessLogId, String result) {
        if (TextUtils.equals(result, "0")) {
            loopCount++;
            if (loopCount >= 10) {
                return;
            }
            if (null == mHandler) mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loopCheckOpenResult(accessLogId);
                }
            }, 1000);
        } else if (TextUtils.equals(result, "1")) {
            ToastUtil.shortToast(SmartDoorListActivity.this, "open door success");
            loopCount = 0;
        } else if (TextUtils.equals(result, "2")) {
            ToastUtil.shortToast(SmartDoorListActivity.this, "open door failure");
            loopCount = 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
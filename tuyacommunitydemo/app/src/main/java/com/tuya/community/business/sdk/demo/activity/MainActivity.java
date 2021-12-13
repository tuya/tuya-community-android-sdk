package com.tuya.community.business.sdk.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.house.anntation.TuyaCommunityHouseAuditStatus;
import com.tuya.community.android.house.bean.TuyaCommunityHouseBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.house.HouseListActivity;
import com.tuya.community.business.sdk.demo.smartdoor.SmartDoorListActivity;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.Constants;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.business.sdk.demo.utils.Utils;
import com.tuya.community.business.sdk.demo.visitor.VisitorActivity;
import com.tuya.community.business.sdk.demo.visualspeak.VisualSpeakMainActivity;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.sdk.os.TuyaOSUser;
import com.tuya.smart.android.user.api.ILogoutCallback;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    TextView tvDetail;
    EditText etHouseId, etCommunityId, etRoomId;
    long houseId = 0;
    String communityId = "";
    String roomId = "";
    String roomUserId = "";
    TuyaCommunityHouseBean currentHouseBean;

    public static final String COMMUNITY_ID = "community_id";
    public static final String ROOM_ID = "room_id";
    public static final String VISITOR_ID = "visitor_id";
    public static final String ROOM_USER_ID = "room_user_id";
    public static final String IS_USER_FACE = "is_user_face";
    public static final String IS_UPLOAD = "is_upload";


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getHouseList(false);
        initListener();
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getHouseList(boolean click) {
        TuyaCommunitySDK.getHouseManagerInstance().getHouseList(new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityHouseBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityHouseBean> list) {
                if (list.size() > 0) {
                    currentHouseBean = list.get(0);
                    if (currentHouseBean.isGuestHouse()) {
                        if (click) HouseListActivity.addHouse(MainActivity.this);
                    } else {
                        setHouseInfo(currentHouseBean);
                    }
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                if (s != null) {

                }
            }
        });
    }

    private void setHouseInfo(TuyaCommunityHouseBean houseBean) {
        etHouseId.setText(houseBean.getHouseId() + "");
        houseId = houseBean.getHouseId();
        Utils.setCurrentHouseId(houseId);
        etCommunityId.setText(houseBean.getCommunityId() + "");
        etRoomId.setText(houseBean.getRoomId() + "");

        roomId = houseBean.getRoomId();
        communityId = houseBean.getCommunityId();
        roomUserId = houseBean.getRoomUserId();
        Utils.setCurrentCommunityId(communityId);
        Utils.setCurrentRoomId(roomId);
    }

    private void getHouseDetail(long houseId) {
        if (houseId == 0) return;

        TuyaCommunitySDK.newHouseInstance(houseId).getHouseDetail(new ITuyaCommunityResultCallback<TuyaCommunityHouseBean>() {
            @Override
            public void onSuccess(TuyaCommunityHouseBean houseBean) {
                if (houseBean != null) {
                    currentHouseBean = houseBean;
                    setHouseInfo(houseBean);
                    refreshHouseInfo(houseBean);
                }
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        findViewById(R.id.btn_visual_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(communityId) && !TextUtils.isEmpty(roomId)) {
                    Intent intent = new Intent(MainActivity.this, VisualSpeakMainActivity.class);
                    intent.putExtra(COMMUNITY_ID, communityId);
                    intent.putExtra(ROOM_ID, roomId);
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshHouseInfo(TuyaCommunityHouseBean bean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("community: ").append(bean.getCommunityName());
        stringBuilder.append("\n");
        stringBuilder.append("address: ").append(bean.getHouseAddress());
        stringBuilder.append("\n");
        stringBuilder.append("status: ").append(TuyaCommunityHouseAuditStatus.Pass == bean.getAuditStatus() ? "pass" :
                TuyaCommunityHouseAuditStatus.Failure == bean.getAuditStatus() ? "failure" :
                        TuyaCommunityHouseAuditStatus.MovedOut == bean.getAuditStatus() ? "move out" :
                                TuyaCommunityHouseAuditStatus.Pending == bean.getAuditStatus() ? "pending" : "pending");
        tvDetail.setText(stringBuilder.toString());
    }

    private void initListener() {
        findViewById(R.id.btn_home_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseListActivity.actionStart(MainActivity.this, -1);
            }
        });

        findViewById(R.id.btn_change_house).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (houseId != 0) {
                    HouseListActivity.actionStart(MainActivity.this, houseId);
                }
            }
        });
        findViewById(R.id.btn_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEnter()) return;
                Intent intent = new Intent(MainActivity.this, VisitorActivity.class);
                intent.putExtra(COMMUNITY_ID, communityId);
                intent.putExtra(ROOM_ID, roomId);
                startActivity(intent);

            }
        });
        findViewById(R.id.btn_smart_door).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEnter()) return;
                Intent intent = new Intent(MainActivity.this, SmartDoorListActivity.class);
                intent.putExtra(COMMUNITY_ID, communityId);
                intent.putExtra(ROOM_ID, roomId);
                startActivity(intent);

            }
        });
        findViewById(R.id.btn_login_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuyaOSUser.getUserInstance().logout(new ILogoutCallback() {
                    @Override
                    public void onSuccess() {
                        Utils.setCurrentHouseId(0);
                        checkLogin();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                    }
                });
            }
        });

        findViewById(R.id.btn_get_house).setOnClickListener(v -> {
            getHouseList(true);
        });

        findViewById(R.id.btn_home_detail).setOnClickListener(v -> {
            getHouseDetail(houseId);
        });
    }

    private boolean checkEnter() {
        if (TextUtils.isEmpty(communityId) || TextUtils.isEmpty(roomId)) {
            return true;
        }

        if (currentHouseBean.getAuditStatus() != TuyaCommunityHouseAuditStatus.Pass) {
            ToastUtil.shortToast(MainActivity.this, "The current house has not been approved");
            return true;
        }
        return false;
    }

    private void initView() {
        etHouseId = findViewById(R.id.et_homeId);
        etCommunityId = findViewById(R.id.et_projectId);
        etRoomId = findViewById(R.id.et_spaceTreeId);
        tvDetail = findViewById(R.id.tv_detail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CHANGE_HOUSE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (null != data) {
                long house_id = data.getLongExtra("house_id", -1);
                getHouseDetail(house_id);
            }
        }
    }
}

package com.tuya.community.business.sdk.demo.visualspeak;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.callback.ITuyaCommunityCallback;
import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.visualspeak.api.IOnVisualSpeakDeviceCommunicationListener;
import com.tuya.community.android.visualspeak.api.ITuyaCommunityAccessControl;
import com.tuya.community.android.visualspeak.api.ITuyaCommunityPublicMonitor;
import com.tuya.community.android.visualspeak.api.ITuyaCommunityVisualSpeak;
import com.tuya.community.android.visualspeak.bean.TYDeviceMsgDataBean;
import com.tuya.community.android.visualspeak.bean.TYMonitorDeviceBean;
import com.tuya.community.android.visualspeak.bean.TYVisualSpeakDeviceBean;
import com.tuya.community.android.visualspeak.enums.TYVisualSpeakCommunicationType;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.ROOM_ID;

public class VisualSpeakMainActivity extends BaseActivity implements View.OnClickListener, IOnVisualSpeakDeviceCommunicationListener {
    private static final String TAG = "VisualSpeakMainActivity";

    private String communityId;
    private String roomId;

    private Button btnAccessControlGetList;
    private Button btnAccessControlRegisterListener;
    private Button btnAccessControlUnRegisterListener;
    private Button btnPublicMonitorGetList;
    private TextView tvVisualSpeakData;
    private RecyclerView rvData;

    private ITuyaCommunityVisualSpeak visualSpeak;
    private ITuyaCommunityAccessControl communityAccessControl;
    private ITuyaCommunityPublicMonitor communityPublicMonitor;

    private VisualSpeakListAdapter visualSpeakListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_speak);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        communityId = getIntent().getStringExtra(COMMUNITY_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);

        visualSpeak = TuyaCommunitySDK.getCommunityVisualSpeakInstance();
        if (visualSpeak != null) {
            communityAccessControl = visualSpeak.getAccessControlManager();
            communityPublicMonitor = visualSpeak.getPublicMonitorManager();
        }
    }

    private void initView() {
        initToolbar();
        setTitle(R.string.visual_speak);
        setDisplayHomeAsUpEnabled();

        btnAccessControlGetList = findViewById(R.id.btn_get_access_control_list);
        btnAccessControlRegisterListener = findViewById(R.id.btn_access_control_register_listener);
        btnAccessControlUnRegisterListener = findViewById(R.id.btn_access_control_unregister_listener);
        btnPublicMonitorGetList = findViewById(R.id.btn_get_public_monitor_list);
        tvVisualSpeakData = findViewById(R.id.tv_device_list);
        rvData = findViewById(R.id.rv_device);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        visualSpeakListAdapter = new VisualSpeakListAdapter(this);
        rvData.setAdapter(visualSpeakListAdapter);
    }

    private void initListener() {
        btnAccessControlGetList.setOnClickListener(this);
        btnAccessControlRegisterListener.setOnClickListener(this);
        btnAccessControlUnRegisterListener.setOnClickListener(this);
        btnPublicMonitorGetList.setOnClickListener(this);

        visualSpeakListAdapter.setOnVisualSpeakDeviceItemClick(new VisualSpeakListAdapter.OnVisualSpeakDeviceItemClick() {
            @Override
            public void onClick(int type, VisualSpeakBean bean) {
                navigateVisualSpeakVideo(type, null, bean);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_access_control_list:
                getAccessControlDeviceList();
                break;
            case R.id.btn_access_control_register_listener:
                registerVisualSpeakListener();
                break;
            case R.id.btn_access_control_unregister_listener:
                unregisterVisualSpeakListener();
                break;
            case R.id.btn_get_public_monitor_list:
                getPublicMonitorDeviceList();
                break;
            default:
                break;
        }
    }

    private void getAccessControlDeviceList() {
        if (TextUtils.isEmpty(communityId)) {
            ToastUtil.shortToast(this, "communityId is null");
            return;
        }
        if (TextUtils.isEmpty(roomId)) {
            ToastUtil.shortToast(this, "roomId is null");
            return;
        }
        if (communityAccessControl != null) {
            communityAccessControl.getAccessControlList(communityId, roomId, new ITuyaCommunityResultCallback<List<TYVisualSpeakDeviceBean>>() {
                @Override
                public void onSuccess(List<TYVisualSpeakDeviceBean> tyVisualSpeakDeviceBeans) {
                    L.e(TAG, "[getDeviceList] success ");
                    List<VisualSpeakBean> list = new ArrayList<>();
                    for (TYVisualSpeakDeviceBean bean : tyVisualSpeakDeviceBeans) {
                        VisualSpeakBean visualSpeakBean = new VisualSpeakBean();
                        visualSpeakBean.setDeviceId(bean.getDeviceId());
                        visualSpeakBean.setDeviceName(bean.getDeviceName());
                        list.add(visualSpeakBean);
                    }
                    tvVisualSpeakData.setVisibility(View.VISIBLE);
                    if (list.size() > 0) {
                        tvVisualSpeakData.setText("Access Control Devices:");
                        visualSpeakListAdapter.setData(1, list);
                    } else {
                        tvVisualSpeakData.setText("access control devices is empty");
                        visualSpeakListAdapter.setData(1, new ArrayList<>());
                    }
                }

                @Override
                public void onFailure(String s, String s1) {
                    L.e(TAG, "[getDeviceList] code =" + s + " detail=" + s1);
                }
            });
        }
    }

    private void getPublicMonitorDeviceList() {
        if (TextUtils.isEmpty(communityId)) {
            ToastUtil.shortToast(this, "communityId is null");
            return;
        }
        if (TextUtils.isEmpty(roomId)) {
            ToastUtil.shortToast(this, "roomId is null");
            return;
        }
        if (communityPublicMonitor != null) {
            communityPublicMonitor.getPublicMonitorList(communityId, roomId, new ITuyaCommunityResultCallback<ArrayList<TYMonitorDeviceBean>>() {
                @Override
                public void onSuccess(ArrayList<TYMonitorDeviceBean> tyMonitorDeviceBeans) {
                    L.e(TAG, "[getDeviceList] success ");
                    List<VisualSpeakBean> list = new ArrayList<>();
                    for (TYMonitorDeviceBean bean : tyMonitorDeviceBeans) {
                        VisualSpeakBean visualSpeakBean = new VisualSpeakBean();
                        visualSpeakBean.setDeviceId(bean.getDeviceId());
                        visualSpeakBean.setDeviceName(bean.getDeviceName());
                        visualSpeakBean.setOnline(bean.getDeviceStatus() == 1);
                        list.add(visualSpeakBean);
                    }
                    tvVisualSpeakData.setVisibility(View.VISIBLE);
                    if (list.size() > 0) {
                        tvVisualSpeakData.setText("Public Monitor Devices:");
                        visualSpeakListAdapter.setData(2, list);
                    } else {
                        tvVisualSpeakData.setText("public monitor devices is empty");
                        visualSpeakListAdapter.setData(1, new ArrayList<>());
                    }

                }

                @Override
                public void onFailure(String s, String s1) {
                    L.e(TAG, "[getDeviceList] code =" + s + " detail=" + s1);
                }
            });
        }
    }

    private void registerVisualSpeakListener() {
        if (visualSpeak != null) {
            visualSpeak.registerVisualSpeakCommunicationListener(this);
            ToastUtil.shortToast(this, "registerVisualSpeakListener success");
        }
    }

    private void unregisterVisualSpeakListener() {
        if (visualSpeak != null) {
            visualSpeak.unRegisterVisualSpeakCommunicationListener(this);
            ToastUtil.shortToast(this, "unregisterVisualSpeakListener success");
        }
    }

    @Override
    public void receiveDeviceMessage(TYVisualSpeakCommunicationType tyVisualSpeakCommunicationType, TYDeviceMsgDataBean tyDeviceMsgDataBean) {
        switch (tyVisualSpeakCommunicationType) {
            case DEVICE_CALLING:
                if (visualSpeak != null) {
                    visualSpeak.loadConfig(tyDeviceMsgDataBean.getDeviceId(), null, new ITuyaCommunityCallback() {
                        @Override
                        public void onFailure(String s, String s1) {
                            ToastUtil.shortToast(VisualSpeakMainActivity.this, s1);
                        }

                        @Override
                        public void onSuccess() {
                            VisualSpeakBean visualSpeakBean = new VisualSpeakBean();
                            visualSpeakBean.setOnline(true);
                            visualSpeakBean.setDeviceName(tyDeviceMsgDataBean.getDeviceId());
                            visualSpeakBean.setDeviceId(tyDeviceMsgDataBean.getDeviceId());
                            navigateVisualSpeakVideo(3, tyDeviceMsgDataBean.getSn(), new VisualSpeakBean());
                        }
                    });
                }
                break;
            case DEVICE_CALL_OTHER_APP_ANSWER:
                break;
            case DEVICE_CALL_CANCEL:
                break;
            case DEVICE_CALL_TIMEOUT:
                break;
            case DEVICE_CALL_HANGUP:
                break;
            case TALKING_TIMEOUT:
                break;
            case UNKNOWN:
            default:
                break;
        }
    }

    /**
     * navigate to video
     *
     * @param type 1:access control actively view videos  2:public monitor view videos  3:access control device call received
     * @param data
     */
    private void navigateVisualSpeakVideo(int type, String sn, VisualSpeakBean data) {
        if (visualSpeak != null) {
            visualSpeak.loadConfig(data.getDeviceId(), null, new ITuyaCommunityCallback() {
                @Override
                public void onFailure(String s, String s1) {
                    L.e(TAG, "[loadDeviceConfig] onFailure:" + s1);
                }

                @Override
                public void onSuccess() {
                    L.d(TAG, "[loadDeviceConfig] onSuccess");
                    Intent intent = new Intent(VisualSpeakMainActivity.this, VisualSpeakVideoActivity.class);
                    intent.putExtra(VisualSpeakVideoActivity.DATA_INTENT_DEVICE_ID, data.getDeviceId());
                    intent.putExtra(VisualSpeakVideoActivity.DATA_INTENT_TYPE, type);
                    intent.putExtra(VisualSpeakVideoActivity.DATA_INTENT_COMMUNITY_ID, communityId);
                    intent.putExtra(VisualSpeakVideoActivity.DATA_INTENT_ROOM_ID, roomId);
                    if (!TextUtils.isEmpty(sn)) {
                        intent.putExtra(VisualSpeakVideoActivity.DATA_INTENT_SN, sn);
                    }
                    startActivity(intent);
                }
            });
        }
    }
}

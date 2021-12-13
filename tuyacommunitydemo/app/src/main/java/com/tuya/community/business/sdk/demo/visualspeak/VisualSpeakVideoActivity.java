package com.tuya.community.business.sdk.demo.visualspeak;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.tuya.community.android.callback.ISuccessFailureCallback;
import com.tuya.community.android.visualspeak.api.IOnVisualSpeakDeviceCommunicationListener;
import com.tuya.community.android.visualspeak.api.ITuyaCommunityAccessControl;
import com.tuya.community.android.visualspeak.api.ITuyaCommunityVisualSpeak;
import com.tuya.community.android.visualspeak.bean.TYDeviceMsgDataBean;
import com.tuya.community.android.visualspeak.enums.TYVisualSpeakCommunicationType;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.Constants;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.android.camera.sdk.TuyaIPCSdk;
import com.tuya.smart.android.camera.sdk.api.ITuyaIPCCore;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.camera.camerasdk.typlayer.callback.AbsP2pCameraListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.ipccamerasdk.p2p.ICameraP2P;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuya.smart.camera.utils.AudioUtils;
import com.tuyasmart.stencil.utils.MessageUtil;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.tuya.community.business.sdk.demo.utils.Constants.ARG1_OPERATE_FAIL;
import static com.tuya.community.business.sdk.demo.utils.Constants.ARG1_OPERATE_SUCCESS;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_CONNECT;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_MUTE;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_OPEN_DOOR;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_TALK_BACK_BEGIN;
import static com.tuya.community.business.sdk.demo.utils.Constants.MSG_TALK_BACK_OVER;

public class VisualSpeakVideoActivity extends BaseActivity implements View.OnClickListener, IOnVisualSpeakDeviceCommunicationListener {
    private static final String TAG = "VisualSpeakVideoActivity";
    public static final String DATA_INTENT_DEVICE_ID = "device_id";
    public static final String DATA_INTENT_ROOM_ID = "room_id";
    public static final String DATA_INTENT_COMMUNITY_ID = "community_id";
    public static final String DATA_INTENT_TYPE = "type";
    public static final String DATA_INTENT_SN = "sn";

    TuyaCameraView cameraView;
    Button btnOpenDoor;
    Button btnReject;
    Button btnAccept;
    private int p2pType;

    private boolean isSpeaking = false;
    private boolean isPlay = false;

    private String deviceId;
    private String communityId;
    private String roomId;
    private String sn;
    private int type;
    private ITuyaSmartCameraP2P cameraP2P = null;

    private int previewMute = ICameraP2P.MUTE;
    private int videoClarity = ICameraP2P.HD;

    private ITuyaCommunityVisualSpeak visualSpeak;
    private ITuyaCommunityAccessControl communityAccessControl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONNECT:
                    handleConnect(msg);
                    break;
                case MSG_MUTE:
                    handleMute(msg);
                    break;
                case MSG_TALK_BACK_BEGIN:
                    handleStartTalk(msg);
                    break;
                case MSG_TALK_BACK_OVER:
                    handleStopTalk(msg);
                    break;
                case MSG_OPEN_DOOR:
                    handleOpenDoor(msg);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_speak_video);
        initView();
        initData();
    }

    private void initView() {
        initToolbar();
        setTitle(R.string.visual_speak_video);
        setDisplayHomeAsUpEnabled();

        cameraView = findViewById(R.id.camera_video_view);
        btnOpenDoor = findViewById(R.id.btn_open_door);
        btnAccept = findViewById(R.id.btn_accept);
        btnReject = findViewById(R.id.btn_reject);

        btnOpenDoor.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);

    }

    private void initData() {
        visualSpeak = TuyaCommunitySDK.getCommunityVisualSpeakInstance();
        if (visualSpeak != null) {
            communityAccessControl = visualSpeak.getAccessControlManager();
            visualSpeak.registerVisualSpeakCommunicationListener(this);
        }

        deviceId = getIntent().getStringExtra(DATA_INTENT_DEVICE_ID);
        type = getIntent().getIntExtra(DATA_INTENT_TYPE, 1);
        communityId = getIntent().getStringExtra(DATA_INTENT_COMMUNITY_ID);
        roomId = getIntent().getStringExtra(DATA_INTENT_ROOM_ID);
        sn = getIntent().getStringExtra(DATA_INTENT_SN);

        L.d(TAG, "[initData] deviceId=" + deviceId + " type=" + type);
        if (type == 1) {
            btnOpenDoor.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            btnOpenDoor.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
            btnAccept.setVisibility(View.VISIBLE);
        }

        ITuyaIPCCore cameraInstance = TuyaIPCSdk.getCameraInstance();
        if (cameraInstance != null) {
            cameraP2P = cameraInstance.createCameraP2P(deviceId);
            p2pType = cameraInstance.getP2PType(deviceId);
            L.d(TAG, "[initData] p2pType=" + p2pType);
        }
        cameraView.setViewCallback(new AbsVideoViewCallback() {
            @Override
            public void onCreated(Object view) {
                super.onCreated(view);
                if (null != cameraP2P) {
                    cameraP2P.generateCameraView(view);
                }
            }
        });
        cameraView.createVideoView(p2pType);
        if (null == cameraP2P) {
            showNotSupportToast();
        }
    }

    private void showNotSupportToast() {
        showToast(getString(R.string.not_support_device));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_door:
                openDoor();
                break;
            case R.id.btn_accept:
                accept();
                break;
            case R.id.btn_reject:
                reject();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
        //must register again,or can't callback
        if (null != cameraP2P) {
            AudioUtils.getModel(this);
            cameraP2P.registerP2PCameraListener(p2pCameraListener);
            cameraP2P.generateCameraView(cameraView.createdView());
            if (cameraP2P.isConnecting()) {
                cameraP2P.startPreview(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isPlay = true;
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        L.d(TAG, "start preview onFailure, errCode: " + errCode);
                    }
                });
            }
            if (!cameraP2P.isConnecting()) {
                connectDevice();
            }
        }
    }

    private AbsP2pCameraListener p2pCameraListener = new AbsP2pCameraListener() {
        @Override
        public void onReceiveSpeakerEchoData(ByteBuffer pcm, int sampleRate) {
            if (null != cameraP2P) {
                int length = pcm.capacity();
                L.d(TAG, "receiveSpeakerEchoData pcmlength " + length + " sampleRate " + sampleRate);
                byte[] pcmData = new byte[length];
                pcm.get(pcmData, 0, length);
                cameraP2P.sendAudioTalkData(pcmData, length);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
        if (null != cameraP2P) {
            if (isSpeaking) {
                cameraP2P.stopAudioTalk(null);
            }
            if (isPlay) {
                cameraP2P.stopPreview(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {

                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {

                    }
                });
                isPlay = false;
            }
            cameraP2P.removeOnP2PCameraListener();
            cameraP2P.disconnect(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int i, int i1, String s) {

                }

                @Override
                public void onFailure(int i, int i1, int i2) {

                }
            });
        }
        AudioUtils.changeToNomal(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (null != cameraP2P) {
            cameraP2P.destroyP2P();
        }
        if (visualSpeak != null) {
            visualSpeak.unRegisterVisualSpeakCommunicationListener(this);
        }
    }

    @Override
    public void receiveDeviceMessage(TYVisualSpeakCommunicationType tyVisualSpeakCommunicationType, TYDeviceMsgDataBean tyDeviceMsgDataBean) {
        L.d(TAG, "[receiveDeviceMessage] type=" + tyVisualSpeakCommunicationType);
        if (tyDeviceMsgDataBean != null && tyDeviceMsgDataBean.getDeviceId().equals(deviceId)
                && tyDeviceMsgDataBean.getRoomId().equals(roomId)) {
            switch (tyVisualSpeakCommunicationType) {
                case DEVICE_CALL_OTHER_APP_ANSWER:
                    showToast("Other app already answered!");
                    finishActivity();
                    break;
                case DEVICE_CALL_CANCEL:
                    showToast("Device cancel!");
                    finishActivity();
                    break;
                case DEVICE_CALL_TIMEOUT:
                    showToast("Device call timeout!");
                    finishActivity();
                    break;
                case DEVICE_CALL_HANGUP:
                    showToast("Device hung up!");
                    finishActivity();
                    break;
                case TALKING_TIMEOUT:
                    showToast("Talking timeout!");
                    finishActivity();
                    break;
                case UNKNOWN:
                default:
                    break;
            }
        }
    }

    private void handleConnect(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            preview();
        } else {
            showToast(getString(R.string.connect_failed));
        }
    }

    private void handleMute(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
//            muteImg.setSelected(previewMute == ICameraP2P.MUTE);
        } else {
            showToast(getString(R.string.operation_failed));
        }
    }

    private void handleStopTalk(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            showToast(getString(R.string.operation_suc));
        } else {
            showToast(getString(R.string.operation_failed));
        }
    }

    private void handleStartTalk(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            showToast(getString(R.string.operation_suc));
        } else {
            showToast(getString(R.string.operation_failed));
        }
    }

    private void handleOpenDoor(Message msg) {
        if (msg.arg1 == ARG1_OPERATE_SUCCESS) {
            showToast(getString(R.string.operation_suc));
        } else {
            showToast(getString(R.string.operation_failed));
        }
    }

    private void preview() {
        cameraP2P.startPreview(videoClarity, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                L.d(TAG, "start preview onSuccess");
                isPlay = true;
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                L.d(TAG, "start preview onFailure, errCode: " + errCode);
                isPlay = false;
            }
        });
    }

    private void connectDevice() {
        cameraP2P.connect(deviceId, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int i, int i1, String s) {
                mHandler.sendMessage(MessageUtil.getMessage(MSG_CONNECT, ARG1_OPERATE_SUCCESS));
            }

            @Override
            public void onFailure(int i, int i1, int i2) {
                mHandler.sendMessage(MessageUtil.getMessage(MSG_CONNECT, ARG1_OPERATE_FAIL));
            }
        });
    }

    private void openDoor() {
        if (communityAccessControl != null) {
            communityAccessControl.openDoor(communityId, roomId, deviceId, new ISuccessFailureCallback() {
                @Override
                public void onSuccess() {
                    mHandler.sendMessage(MessageUtil.getMessage(MSG_OPEN_DOOR, ARG1_OPERATE_SUCCESS));
                }

                @Override
                public void onFailure(String s, String s1) {
                    L.e(TAG, "[openDoor] onFailure:" + s1);
                    mHandler.sendMessage(MessageUtil.getMessage(MSG_OPEN_DOOR, ARG1_OPERATE_SUCCESS));
                }
            });
        }
    }

    private void accept() {
        speak();
    }

    private void reject() {
        if (communityAccessControl != null) {
            Map<String, String> map = new HashMap<>();
            map.put("device_id", deviceId);
            map.put("community_id", communityId);
            map.put("room_id", roomId);
            map.put("sn", sn);
            communityAccessControl.reject(map, new ISuccessFailureCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }

    private void speak() {
        if (isSpeaking) {
            cameraP2P.stopAudioTalk(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    isSpeaking = false;
                    mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_OVER, ARG1_OPERATE_SUCCESS));
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    isSpeaking = false;
                    mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_OVER, ARG1_OPERATE_FAIL));

                }
            });
        } else {
            if (Constants.hasRecordPermission()) {
                cameraP2P.startAudioTalk(new OperationDelegateCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data) {
                        isSpeaking = true;
                        mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_BEGIN, ARG1_OPERATE_SUCCESS));
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode) {
                        isSpeaking = false;
                        mHandler.sendMessage(MessageUtil.getMessage(MSG_TALK_BACK_BEGIN, ARG1_OPERATE_FAIL));
                    }
                });
            } else {
                Constants.requestPermission(VisualSpeakVideoActivity.this, Manifest.permission.RECORD_AUDIO, Constants.EXTERNAL_AUDIO_REQ_CODE, "open_recording");
            }
        }
    }
}

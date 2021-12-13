package com.tuya.community.business.sdk.demo.smartdoor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.smartdoor.bean.TuyaCommunityQRCodeBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.utils.QRUtils;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import static com.tuya.community.business.sdk.demo.activity.MainActivity.COMMUNITY_ID;
import static com.tuya.community.business.sdk.demo.activity.MainActivity.ROOM_ID;

public class CommunityQRCodeActivity extends BaseActivity {

    ImageView ivQrcode;
    TextView tvDoors, tvEleTor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_q_r_code);

        initToolbar();
        setDisplayHomeAsUpEnabled(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle("qrcode list");

        ivQrcode = findViewById(R.id.iv_qrcode);
        tvDoors = findViewById(R.id.tv_doors);
        tvEleTor = findViewById(R.id.tv_elevtor);

        String communityId = getIntent().getStringExtra(COMMUNITY_ID);
        String roomId = getIntent().getStringExtra(ROOM_ID);
        TuyaCommunitySDK.getSmartDoorInstance().getCommunityQrCode(communityId, roomId, new ITuyaCommunityResultCallback<TuyaCommunityQRCodeBean>() {
            @Override
            public void onSuccess(TuyaCommunityQRCodeBean bean) {
                if (bean != null) {
                    try {
                        Bitmap bitmap = QRUtils.createQRCode(bean.getQrCodeUrl(), QRUtils.dp2px(CommunityQRCodeActivity.this, 215));
                        ivQrcode.setImageBitmap(bitmap);

                        if (bean.getAccessDoorList() != null && bean.getAccessDoorList().size() > 0) {
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < bean.getAccessDoorList().size(); i++) {
                                builder.append(bean.getAccessDoorList().get(i)).append("; ");
                            }
                            tvDoors.setText(builder.length() > 0 ? builder.toString().substring(0, builder.length() - 1) :
                                    "empty");
                        }
                        if (bean.getAccessElevatorList() != null && bean.getAccessElevatorList().size() > 0) {
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < bean.getAccessElevatorList().size(); i++) {
                                builder.append(bean.getAccessElevatorList().get(i)).append("; ");
                            }
                            tvEleTor.setText(builder.length() > 0 ? builder.toString().substring(0, builder.length() - 1) :
                                    "empty");
                        }
                    } catch (com.google.zxing.WriterException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(CommunityQRCodeActivity.this, s1);

            }
        });
    }
}
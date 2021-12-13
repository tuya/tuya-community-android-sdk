package com.tuya.community.business.sdk.demo.house;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.callback.ISuccessFailureCallback;
import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.house.anntation.TuyaCommunityHouseAuditStatus;
import com.tuya.community.android.house.bean.TuyaCommunityHouseBean;
import com.tuya.community.android.house.bean.TuyaCommunityUserCertificationInfoBean;
import com.tuya.community.android.house.bean.TuyaCommunityWeatherBean;
import com.tuya.community.android.house.bean.TuyaCommunityWeatherSketchBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.house.adapter.HouseListAdapter;
import com.tuya.community.business.sdk.demo.utils.ActivityUtils;
import com.tuya.community.business.sdk.demo.utils.DialogUtil;
import com.tuya.community.business.sdk.demo.utils.Utils;
import com.tuya.community.sdk.android.TuyaCommunitySDK;
import com.tuya.smart.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tuya.community.business.sdk.demo.utils.Constants.CHANGE_HOUSE_REQUEST_CODE;

public class HouseListActivity extends BaseActivity {
    private static final String HOUSE_ID = "house_id";

    private HouseListAdapter mCommunityListAdapter;
    private RecyclerView rvHouseList;

    private long houseId = -1;

    public static void actionStart(Context context, long houseId) {
        Intent intent = new Intent(context, HouseListActivity.class);
        intent.putExtra(HOUSE_ID, houseId);
        if (houseId >= 0) {
            ActivityUtils.startActivityForResult((Activity) context
                    , intent
                    , CHANGE_HOUSE_REQUEST_CODE
                    , ActivityUtils.ANIMATE_FORWARD
                    , false);
        } else {
            ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        setContentView(R.layout.activity_house);

        initToolbar();
        setDisplayHomeAsUpEnabled(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rvHouseList = findViewById(R.id.rv_house_list);
        findViewById(R.id.add_house).setVisibility(houseId < 0 ? View.VISIBLE : View.GONE);
        setTitle("my house list");

        rvHouseList.setLayoutManager(new LinearLayoutManager(this));
        mCommunityListAdapter = new HouseListAdapter(houseId);
        rvHouseList.setAdapter(mCommunityListAdapter);
        mCommunityListAdapter.setOnItemClickListener(new HouseListAdapter.OnBusinessClickListener() {
            @Override
            public void onItemClick(TuyaCommunityHouseBean item, int position) {
                //enter house detail
                if (houseId < 0) {
                    Intent intent = new Intent(HouseListActivity.this, MemberListActivity.class);
                    intent.putExtra("communityId", item.getCommunityId());
                    intent.putExtra("houseId", item.getHouseId());
                    intent.putExtra("roomId", item.getRoomId());
                    startActivity(intent);
                } else {
                    houseId = item.getHouseId();
                }
            }

            @Override
            public void onLongClick(TuyaCommunityHouseBean item, int position) {
                //delete house
                if (houseId < 0) {
                    if (item.getAuditStatus() == TuyaCommunityHouseAuditStatus.Pass) {//
                        DialogUtil.simpleConfirmDialog(HouseListActivity.this, "sure move out this house?", new DialogUtil.OnClickListener() {
                            @Override
                            public void onSureClick() {
                                TuyaCommunitySDK.getHouseManagerInstance().moveOutHouse(item.getCommunityId(), item.getRoomUserId(), new ISuccessFailureCallback() {
                                    @Override
                                    public void onSuccess() {
                                        if (item != null) {
                                            getHouseList();
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
                        DialogUtil.simpleConfirmDialog(HouseListActivity.this, "sure delete this house?", new DialogUtil.OnClickListener() {
                            @Override
                            public void onSureClick() {
                                TuyaCommunitySDK.getHouseManagerInstance().deleteHouse(item.getCommunityId(), item.getHouseId(), new ISuccessFailureCallback() {
                                    @Override
                                    public void onSuccess() {
                                        if (item != null) {
                                            getHouseList();
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
                } else {
                    houseId = item.getHouseId();
                }
            }
        });

        findViewById(R.id.add_house).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHouse(HouseListActivity.this);
            }
        });
    }

    public static void addHouse(Activity activity) {
        TuyaCommunitySDK.getHouseMemberInstance().getUserCertificationInfo(new ITuyaCommunityResultCallback<TuyaCommunityUserCertificationInfoBean>() {
            @Override
            public void onSuccess(TuyaCommunityUserCertificationInfoBean houseCerificationBean) {
                if (!houseCerificationBean.isUserIdentificationStatus()) {
                    activity.startActivity(new Intent(activity, VerifyInfoActivity.class));
                } else {
                    activity.startActivity(new Intent(activity, ChooseCommunityActivity.class));
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(activity, s1);
            }
        });
    }

    private void initParams() {
        houseId = getIntent().getLongExtra(HOUSE_ID, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHouseList();
    }

    private void getHouseList() {
        TuyaCommunitySDK.getHouseManagerInstance().getHouseList(new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityHouseBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityHouseBean> list) {
                if (list.size() > 0) {
                    mCommunityListAdapter.setCommunityList(list);
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                if (s != null) {
                    ToastUtil.shortToast(HouseListActivity.this, s1);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (houseId > 0) {
            Intent intent = new Intent();
            intent.putExtra("house_id", houseId);
            setResult(RESULT_OK, intent);
        }
        finish();
        super.onBackPressed();
    }
}
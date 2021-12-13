package com.tuya.community.business.sdk.demo.house;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.tuya.community.android.callback.ITuyaCommunityResultCallback;
import com.tuya.community.android.house.bean.TuyaCommunityBean;
import com.tuya.community.android.house.bean.TuyaCommunityHouseTreeBean;
import com.tuya.community.android.house.bean.TuyaCommunityListBean;
import com.tuya.community.business.sdk.demo.BaseActivity;
import com.tuya.community.business.sdk.demo.R;
import com.tuya.community.business.sdk.demo.house.adapter.CommunityListAdapter;
import com.tuya.community.business.sdk.demo.house.adapter.CommunityTreeListAdapter;
import com.tuya.community.business.sdk.demo.utils.ToastUtil;
import com.tuya.community.sdk.android.TuyaCommunitySDK;

import java.util.ArrayList;
import java.util.List;

public class ChooseCommunityActivity extends BaseActivity {

    private CommunityListAdapter mCommunityListAdapter;
    private RecyclerView mRvCommunityList;
    private String communityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_community);
        mRvCommunityList = findViewById(R.id.rv_community_list);
        initToolbar();
        setDisplayHomeAsUpEnabled();
        setTitle("select community house");

        mRvCommunityList.setLayoutManager(new LinearLayoutManager(this));
        mCommunityListAdapter = new CommunityListAdapter();
        mRvCommunityList.setAdapter(mCommunityListAdapter);

        mCommunityListAdapter.setOnBusinessClickListener(new CommunityListAdapter.OnBusinessClickListener() {
            @Override
            public void onCommunityClick(TuyaCommunityBean item, int position) {
                communityId = item.getCommunityId();
                getCommunityHouseList(item.getCommunityId());
            }
        });
        TuyaCommunitySDK.getHouseManagerInstance().getCommunityList(
                "",30.29712313081528, 120.06253572996653, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityListBean>>() {
                    @Override
                    public void onSuccess(ArrayList<TuyaCommunityListBean> tuyaCommunityListBeans) {
                        if (null != tuyaCommunityListBeans && tuyaCommunityListBeans.size() > 0) {
                            mCommunityListAdapter.setCommunityList(transform(tuyaCommunityListBeans));
                        }
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        ToastUtil.shortToast(ChooseCommunityActivity.this, s1);
                    }
                });
    }

    private void getCommunityHouseList(String treeId) {
        TuyaCommunitySDK.getHouseManagerInstance().getCommunityHouseTreeList(treeId, new ITuyaCommunityResultCallback<ArrayList<TuyaCommunityHouseTreeBean>>() {
            @Override
            public void onSuccess(ArrayList<TuyaCommunityHouseTreeBean> tuyaCommunityHouseTreeBeans) {
                if (null != tuyaCommunityHouseTreeBeans && tuyaCommunityHouseTreeBeans.size() > 0) {
                    mRvCommunityList.setAdapter(new CommunityTreeListAdapter(tuyaCommunityHouseTreeBeans, new CommunityTreeListAdapter.OnBusinessClickListener() {
                        @Override
                        public void onCommunityClick(TuyaCommunityHouseTreeBean item, int position) {
                            if (item.isHasMore()) {
                                getCommunityHouseList(item.getSpaceTreeId());
                            } else {
                                Intent intent = new Intent(ChooseCommunityActivity.this, AddHouseActivity.class);
                                intent.putExtra("communityId", communityId);
                                intent.putExtra("roomId", item.getSpaceTreeId());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }));

                }
            }

            @Override
            public void onFailure(String s, String s1) {
                ToastUtil.shortToast(ChooseCommunityActivity.this, s1);
            }
        });
    }

    private List<TuyaCommunityBean> transform(ArrayList<TuyaCommunityListBean> tuyaCommunityListBeans) {
        List<TuyaCommunityBean> list = new ArrayList<>();
        for (int i = 0; i < tuyaCommunityListBeans.size(); i++) {
            TuyaCommunityListBean communityListBean = tuyaCommunityListBeans.get(i);
            list.addAll(communityListBean.getList());
        }
        return list;
    }
}
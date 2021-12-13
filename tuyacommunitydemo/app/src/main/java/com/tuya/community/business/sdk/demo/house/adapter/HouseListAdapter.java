package com.tuya.community.business.sdk.demo.house.adapter;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.house.anntation.TuyaCommunityHouseAuditStatus;
import com.tuya.community.android.house.bean.TuyaCommunityHouseBean;
import com.tuya.community.business.sdk.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author:
 * @date: 2021/8/3
 */
public class HouseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long houseId;
    private OnBusinessClickListener mOnBusinessClickListener;

    private List<TuyaCommunityHouseBean> mData = new ArrayList<>();

    public HouseListAdapter(long houseId) {
        this.houseId = houseId;
    }

    public void setOnItemClickListener(OnBusinessClickListener onBusinessClickListener) {
        mOnBusinessClickListener = onBusinessClickListener;
    }

    public void setCommunityList(List<TuyaCommunityHouseBean> list) {
        mData = new ArrayList<>();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_house, viewGroup, false);
        return new ItemNormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TuyaCommunityHouseBean item = mData.get(position);
        ((ItemNormalViewHolder) holder).bindData(item);
        ((ItemNormalViewHolder) holder).bindListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isGuestHouse()) {
                    if (houseId >= 0) {
                        houseId = item.getHouseId();
                        notifyDataSetChanged();
                    }
                    mOnBusinessClickListener.onItemClick(item, position);
                }
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!item.isGuestHouse()) {
                    if (houseId >= 0) {
                        houseId = item.getHouseId();
                        notifyDataSetChanged();
                    }
                    mOnBusinessClickListener.onLongClick(item, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public interface OnBusinessClickListener {
        void onItemClick(TuyaCommunityHouseBean item, int position);

        void onLongClick(TuyaCommunityHouseBean item, int position);
    }

    class ItemNormalViewHolder extends RecyclerView.ViewHolder {

        private View mFlRoot;
        private TextView mTvTitle, tvState;
        private ImageView mIvArrowRight;
        private ImageView iv_house_select;

        ItemNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            tvState = itemView.findViewById(R.id.tv_state);
            mIvArrowRight = itemView.findViewById(R.id.iv_house_list_arrow_right);
            iv_house_select = itemView.findViewById(R.id.iv_house_select);
            iv_house_select.setColorFilter(R.color.colorPrimary, PorterDuff.Mode.SRC_ATOP);
            mFlRoot = itemView.findViewById(R.id.fl_root);
        }

        void bindData(TuyaCommunityHouseBean item) {
            if (item.isGuestHouse()) {
                mTvTitle.setText(item.getName());
            } else {
                mTvTitle.setText(item.getName());
                tvState.setText(TuyaCommunityHouseAuditStatus.Pass == item.getAuditStatus() ? "pass" :
                        TuyaCommunityHouseAuditStatus.Failure == item.getAuditStatus() ? "failure" :
                                TuyaCommunityHouseAuditStatus.MovedOut == item.getAuditStatus() ? "move out" :
                                        TuyaCommunityHouseAuditStatus.Pending == item.getAuditStatus() ? "pending" : "pending");

                if (houseId < 0) {
                    iv_house_select.setVisibility(View.GONE);
                    mIvArrowRight.setVisibility(View.VISIBLE);
                } else {
                    mIvArrowRight.setVisibility(View.GONE);
                    iv_house_select.setVisibility(item.getHouseId() == houseId ? View.VISIBLE : View.GONE);
                }
            }
        }

        void bindListener(View.OnClickListener listener, View.OnLongClickListener longClickListener) {
            mFlRoot.setOnClickListener(listener);
            mFlRoot.setOnLongClickListener(longClickListener);
        }
    }
}

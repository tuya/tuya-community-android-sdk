package com.tuya.community.business.sdk.demo.house.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.house.anntation.TuyaCommunityMemberAuditStatus;
import com.tuya.community.android.house.bean.TuyaCommunityMemberBean;
import com.tuya.community.business.sdk.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author:
 * @date: 2021/8/3
 */
public class MemberListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnBusinessClickListener mOnBusinessClickListener;

    private List<TuyaCommunityMemberBean> mData = new ArrayList<>();

    public void setOnItemClickListener(OnBusinessClickListener onBusinessClickListener) {
        mOnBusinessClickListener = onBusinessClickListener;
    }

    public void setCommunityList(List<TuyaCommunityMemberBean> list) {
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
        final TuyaCommunityMemberBean item = mData.get(position);
        ((ItemNormalViewHolder) holder).bindData(item);
        ((ItemNormalViewHolder) holder).bindListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBusinessClickListener.onItemClick(item, position);
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnBusinessClickListener.onLongClick(item, position);
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
        void onItemClick(TuyaCommunityMemberBean item, int position);

        void onLongClick(TuyaCommunityMemberBean item, int position);
    }

    class ItemNormalViewHolder extends RecyclerView.ViewHolder {

        private View mFlRoot;
        private TextView mTvTitle, tvState;

        ItemNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            tvState = itemView.findViewById(R.id.tv_state);
            mFlRoot = itemView.findViewById(R.id.fl_root);
        }

        void bindData(TuyaCommunityMemberBean item) {
            mTvTitle.setText(item.getNickName());
            String state = TuyaCommunityMemberAuditStatus.Pass == item.getAudit() ? "pass" :
                    TuyaCommunityMemberAuditStatus.Failure == item.getAudit() ? "failure" :
                            TuyaCommunityMemberAuditStatus.Pending == item.getAudit() ? "pending" : "";
            tvState.setText(state+"  -------  "+item.getUserTypeName());
        }

        void bindListener(View.OnClickListener listener, View.OnLongClickListener longClickListener) {
            mFlRoot.setOnClickListener(listener);
            mFlRoot.setOnLongClickListener(longClickListener);
        }
    }
}

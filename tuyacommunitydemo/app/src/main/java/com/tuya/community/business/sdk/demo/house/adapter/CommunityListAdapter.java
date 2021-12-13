package com.tuya.community.business.sdk.demo.house.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.house.bean.TuyaCommunityBean;
import com.tuya.community.business.sdk.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author:
 * @date: 2021/8/3
 */
public class CommunityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnBusinessClickListener mOnBusinessClickListener;

    private final List<TuyaCommunityBean> mData = new ArrayList<>();

    public void setOnBusinessClickListener(OnBusinessClickListener onBusinessClickListener) {
        mOnBusinessClickListener = onBusinessClickListener;
    }
    public void setCommunityList(List<TuyaCommunityBean> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.family_community_recycle_item_choice_list_normal, viewGroup, false);
        return new ItemNormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TuyaCommunityBean item = mData.get(position);
        ((ItemNormalViewHolder) holder).bindData(item);
        ((ItemNormalViewHolder) holder).bindListener(v -> mOnBusinessClickListener.onCommunityClick(item, position));
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
        void onCommunityClick(TuyaCommunityBean item, int position);
    }

    class ItemNormalViewHolder extends RecyclerView.ViewHolder {

        private View mFlRoot;
        private TextView mTvTitle;

        ItemNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mFlRoot = itemView.findViewById(R.id.fl_root);
        }

        void bindData(TuyaCommunityBean item) {
            mTvTitle.setText(item.getCommunityName());
        }

        void bindListener(View.OnClickListener listener) {
            mFlRoot.setOnClickListener(listener);
        }
    }
}

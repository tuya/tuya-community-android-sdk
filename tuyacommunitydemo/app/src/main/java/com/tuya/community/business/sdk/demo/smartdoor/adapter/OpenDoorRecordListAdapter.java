package com.tuya.community.business.sdk.demo.smartdoor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.smartdoor.bean.TuyaCommunitySmartDoorOpenRecordBean;
import com.tuya.community.business.sdk.demo.R;
import com.tuyasmart.stencil.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class OpenDoorRecordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnBusinessClickListener mOnBusinessClickListener;

    private List<TuyaCommunitySmartDoorOpenRecordBean> mData = new ArrayList<>();

    public void setOnItemClickListener(OnBusinessClickListener onBusinessClickListener) {
        mOnBusinessClickListener = onBusinessClickListener;
    }

    public void setCommunityList(List<TuyaCommunitySmartDoorOpenRecordBean> list) {
        mData = new ArrayList<>();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_smart_door, viewGroup, false);
        return new ItemNormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TuyaCommunitySmartDoorOpenRecordBean item = mData.get(position);
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
        void onItemClick(TuyaCommunitySmartDoorOpenRecordBean item, int position);

        void onLongClick(TuyaCommunitySmartDoorOpenRecordBean item, int position);
    }

    class ItemNormalViewHolder extends RecyclerView.ViewHolder {

        private View mFlRoot;
        private TextView mTvTitle, time;
        private ImageView mIvSmartDoorArrowRight;

        ItemNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            time = itemView.findViewById(R.id.tv_time);
            mIvSmartDoorArrowRight = itemView.findViewById(R.id.iv_smart_door_arrow_right);
            mFlRoot = itemView.findViewById(R.id.fl_root);
        }

        void bindData(TuyaCommunitySmartDoorOpenRecordBean item) {
            mTvTitle.setText(item.getAccessControlAddress());
            time.setText(TimeUtil.getNowTime(item.getAccessTime()));
            time.setVisibility(View.VISIBLE);
            mIvSmartDoorArrowRight.setVisibility(View.GONE);
        }

        void bindListener(View.OnClickListener listener, View.OnLongClickListener longClickListener) {
            mFlRoot.setOnClickListener(listener);
            mFlRoot.setOnLongClickListener(longClickListener);
        }
    }
}

package com.tuya.community.business.sdk.demo.visitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.android.visitor.bean.TuyaCommunityVisitorBean;
import com.tuya.community.business.sdk.demo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<TuyaCommunityVisitorBean> data = new ArrayList<>();
    private onDetailClickListenter listenter;
    private Context mContext;

    public void updateData(List<TuyaCommunityVisitorBean> list) {
        if (null != list) {
            data = new ArrayList<>();
            data.addAll(list);
            notifyDataSetChanged();
        }
    }

    public RecordAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_visitor_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder viewHolder, int i) {
        TuyaCommunityVisitorBean bean = data.get(i);
        viewHolder.mTvPhone.setText(bean.getPhone());
        viewHolder.mTvName.setText(bean.getVisitorName());
        if (bean.getVisitorStatus() == 0) {
            viewHolder.mTvstatus.setText(mContext.getResources().getString(R.string.not_visitor));
        } else if (bean.getVisitorStatus() == 1) {
            viewHolder.mTvstatus.setText(mContext.getResources().getString(R.string.visited));
        } else if (bean.getVisitorStatus() == 2) {
            viewHolder.mTvstatus.setText(mContext.getResources().getString(R.string.miss_visitor));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
        viewHolder.mTvStartTime.setText(simpleDateFormat.format(new Date(bean.getStartTime())));
        viewHolder.mTvEndTime.setText(simpleDateFormat.format(new Date(bean.getEndTime())));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listenter) {
                    listenter.onChoose(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        TextView mTvPhone;
        TextView mTvStartTime;
        TextView mTvEndTime;
        TextView mTvstatus;


        public ViewHolder(View itemView) {
            super(itemView);
            mTvEndTime = itemView.findViewById(R.id.tv_visitor_end_time);
            mTvStartTime = itemView.findViewById(R.id.tv_visitor_start_time);
            mTvName = itemView.findViewById(R.id.tv_visitor_name);
            mTvPhone = itemView.findViewById(R.id.tv_visitor_phone);
            mTvstatus = itemView.findViewById(R.id.tv_visitor_status);
        }
    }

    public void setonDetailClickListenter(onDetailClickListenter listenter) {
        this.listenter = listenter;
    }

    public interface onDetailClickListenter {
        void onChoose(View view, int position);
    }
}

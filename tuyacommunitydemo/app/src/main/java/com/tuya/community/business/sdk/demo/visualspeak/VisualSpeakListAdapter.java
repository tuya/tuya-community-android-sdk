package com.tuya.community.business.sdk.demo.visualspeak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.community.business.sdk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class VisualSpeakListAdapter extends RecyclerView.Adapter<VisualSpeakListAdapter.VisualSpeakViewHolder> {

    private List<VisualSpeakBean> list = new ArrayList<>();
    private Context context;
    //access control:1
    //public monitor:2
    private int deviceType = 0;

    private OnVisualSpeakDeviceItemClick onVisualSpeakDeviceItemClick;

    public VisualSpeakListAdapter(Context context) {
        this.context = context;
    }

    public void setData(int type, List<VisualSpeakBean> data) {
        this.deviceType = type;

        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VisualSpeakViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VisualSpeakViewHolder(LayoutInflater.from(context).inflate(R.layout.item_visual_speak_device, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VisualSpeakListAdapter.VisualSpeakViewHolder visualSpeakViewHolder, int i) {
        VisualSpeakBean bean = list.get(i);
        visualSpeakViewHolder.tvDeviceName.setText("Device Name: " + bean.getDeviceName());
        visualSpeakViewHolder.tvDeviceId.setText("Device Id: " + bean.getDeviceId());
        visualSpeakViewHolder.tvDeviceStatus.setText("Device is Online: " + bean.isOnline());
        if (deviceType == 2) {
            visualSpeakViewHolder.tvDeviceStatus.setVisibility(View.VISIBLE);
        } else {
            visualSpeakViewHolder.tvDeviceStatus.setVisibility(View.GONE);
        }
        visualSpeakViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVisualSpeakDeviceItemClick != null) {
                    VisualSpeakBean visualSpeakBean = list.get(i);
                    onVisualSpeakDeviceItemClick.onClick(deviceType, visualSpeakBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public OnVisualSpeakDeviceItemClick getOnVisualSpeakDeviceItemClick() {
        return onVisualSpeakDeviceItemClick;
    }

    public void setOnVisualSpeakDeviceItemClick(OnVisualSpeakDeviceItemClick onVisualSpeakDeviceItemClick) {
        this.onVisualSpeakDeviceItemClick = onVisualSpeakDeviceItemClick;
    }

    static class VisualSpeakViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDeviceName;
        public TextView tvDeviceId;
        public TextView tvDeviceStatus;

        public VisualSpeakViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.tv_device_name);
            tvDeviceId = itemView.findViewById(R.id.tv_device_id);
            tvDeviceStatus = itemView.findViewById(R.id.tv_device_status);
        }
    }

    interface OnVisualSpeakDeviceItemClick {
        void onClick(int type, VisualSpeakBean bean);
    }
}

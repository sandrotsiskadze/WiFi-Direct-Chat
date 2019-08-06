package com.example.wifidirectchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConnectItemAdapter extends RecyclerView.Adapter<ConnectItemAdapter.ViewHolder> {
    private List<String> deviceNameList;
    private OnConnectItemClickListener onConnectItemClickListener;

    public ConnectItemAdapter(List<String> deviceNameList, OnConnectItemClickListener onConnectItemClickListener) {
        this.deviceNameList = deviceNameList;
        this.onConnectItemClickListener = onConnectItemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConnectItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View connectItemView = inflater.inflate(R.layout.content_connect_item, parent, false);
        ConnectItemAdapter.ViewHolder viewHolder = new ConnectItemAdapter.ViewHolder(connectItemView, onConnectItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String deviceName = deviceNameList.get(position);

        TextView textView = holder.textView;

        textView.setText(deviceName);
    }

    @Override
    public int getItemCount() {
        return deviceNameList.size();
    }

    public interface OnConnectItemClickListener {
        void onConnectItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public OnConnectItemClickListener onConnectItemClickListener;

        public ViewHolder(@NonNull View itemView, OnConnectItemClickListener onConnectItemClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.connect_item_textView);
            this.onConnectItemClickListener = onConnectItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onConnectItemClickListener.onConnectItemClick(getAdapterPosition());
        }
    }
}

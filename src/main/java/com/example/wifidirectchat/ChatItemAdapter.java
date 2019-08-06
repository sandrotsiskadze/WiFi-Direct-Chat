package com.example.wifidirectchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;

    public ChatItemAdapter(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void insert(Message message) {
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View chatItemView;
        if (viewType == 0) {
            chatItemView = inflater.inflate(R.layout.content_chat_item_left, parent, false);
            ChatItemAdapter.ViewHolderLeft viewHolder = new ChatItemAdapter.ViewHolderLeft(chatItemView);
            return viewHolder;
        } else {
            chatItemView = inflater.inflate(R.layout.content_chat_item_right, parent, false);
            ChatItemAdapter.ViewHolderRight viewHolder = new ChatItemAdapter.ViewHolderRight(chatItemView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            ViewHolderLeft viewHolderLeft = (ViewHolderLeft) holder;
            Message message = messageList.get(position);
            TextView textView = viewHolderLeft.textView;
            textView.setText(message.getMessage());
        } else {
            ViewHolderRight viewHolderRight = (ViewHolderRight) holder;
            Message message = messageList.get(position);
            TextView textView = viewHolderRight.textView;
            textView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getOwner();
    }

    public class ViewHolderLeft extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolderLeft(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.chat_item_left_textView);
        }
    }

    public class ViewHolderRight extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolderRight(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.chat_item_right_textView);
        }
    }
}

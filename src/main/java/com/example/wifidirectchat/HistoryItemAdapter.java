package com.example.wifidirectchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    private List<Chat> chatList;
    private OnChatItemClickListener onChatItemClickListener;
    private OnChatItemLongClickListener onChatItemLongClickListener;

    public HistoryItemAdapter(List<Chat> chatList, OnChatItemClickListener onChatItemClickListener, OnChatItemLongClickListener onChatItemLongClickListener) {
        this.chatList = chatList;
        this.onChatItemClickListener = onChatItemClickListener;
        this.onChatItemLongClickListener = onChatItemLongClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View historyItemView = inflater.inflate(R.layout.content_history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(historyItemView, onChatItemClickListener, onChatItemLongClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemAdapter.ViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        TextView textView = holder.textView;
        TextView textView1 = holder.textView1;
        TextView textView2 = holder.textView2;

        textView.setText(chat.getName());
        textView1.setText(chat.getDate());
        textView2.setText(Integer.toString(chat.getMessageList().size()));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnChatItemClickListener {
        void onChatItemClick(int position);
    }

    public interface OnChatItemLongClickListener {
        void onChatItemLongClickListener(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textView;
        public TextView textView1;
        public TextView textView2;
        public OnChatItemClickListener onChatItemClickListener;
        public OnChatItemLongClickListener onChatItemLongClickListener;

        public ViewHolder(@NonNull View itemView, OnChatItemClickListener onChatItemClickListener, OnChatItemLongClickListener onChatItemLongClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.history_item_textView);
            textView1 = itemView.findViewById(R.id.history_item_textView1);
            textView2 = itemView.findViewById(R.id.history_item_textView2);
            this.onChatItemClickListener = onChatItemClickListener;
            this.onChatItemLongClickListener = onChatItemLongClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onChatItemClickListener.onChatItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onChatItemLongClickListener.onChatItemLongClickListener(getAdapterPosition());
            return false;
        }
    }
}

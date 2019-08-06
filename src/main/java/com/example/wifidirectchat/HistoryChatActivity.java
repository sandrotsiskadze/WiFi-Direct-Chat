package com.example.wifidirectchat;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryChatActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter presenter;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this, new DependencyInjectorImpl(), this);
        setContentView(R.layout.app_bar_history_chat);
        Toolbar toolbar = findViewById(R.id.history_chat_toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        chat = bundle.getParcelable("chat");
        toolbar.setTitle(chat.getName() + " " + chat.getDate());
        ChatItemAdapter chatItemAdapter = new ChatItemAdapter(chat.getMessageList());
        RecyclerView recyclerView = findViewById(R.id.history_chat_recyclerView);
        recyclerView.setAdapter(chatItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void back(View view) {
        finish();
    }

    public void deleteChat(View view) {
        ((MainPresenter) presenter).deleteChat(chat);
        finish();
    }
}

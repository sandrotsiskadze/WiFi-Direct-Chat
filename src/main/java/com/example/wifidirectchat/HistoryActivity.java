package com.example.wifidirectchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HistoryItemAdapter.OnChatItemClickListener, HistoryItemAdapter.OnChatItemLongClickListener, MainContract.View {
    private MainContract.Presenter presenter;
    private boolean isEmpty;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new MainPresenter(this, new DependencyInjectorImpl(), this);
        chatList = ((MainPresenter) presenter).getChats();
        isEmpty = chatList.isEmpty();
        if (isEmpty) {
            setContentView(R.layout.activity_history_empty);
            Toolbar toolbar = findViewById(R.id.history_empty_toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.history_empty_layout);
            NavigationView navigationView = findViewById(R.id.history_empty_nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            setContentView(R.layout.activity_history);
            Toolbar toolbar = findViewById(R.id.history_toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.history_layout);
            NavigationView navigationView = findViewById(R.id.history_nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            toolbar.setTitle(toolbar.getTitle() + " (" + chatList.size() + ")");
            HistoryItemAdapter historyItemAdapter = new HistoryItemAdapter(chatList, this, this);
            RecyclerView recyclerView = findViewById(R.id.history_recyclerView);
            recyclerView.setAdapter(historyItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.history_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat) {
            startActivity(new Intent(HistoryActivity.this, ChatActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
        }

        if (isEmpty) {
            DrawerLayout drawer = findViewById(R.id.history_empty_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            DrawerLayout drawer = findViewById(R.id.history_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onChatItemClick(int position) {
        Intent intent = new Intent(HistoryActivity.this, HistoryChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("chat", chatList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onChatItemLongClickListener(int position) {

    }

    public void clearHistory(View view) {
        ((MainPresenter) presenter).clearHistory();
        onResume();
    }
}

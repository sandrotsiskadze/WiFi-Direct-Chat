package com.example.wifidirectchat;

import android.content.Context;

import java.util.List;

public interface Interactor {
    List<Chat> getChats(Context context);

    void clearHistory(Context context);

    void deleteChat(Context context, Chat chat);

    void insertChat(Context context, Chat chat);

    Chat getChat(Context context, String name);
}

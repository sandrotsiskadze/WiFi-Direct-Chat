package com.example.wifidirectchat;

import java.util.List;

public interface MainContract {
    interface Presenter {
        List<Chat> getChats();
        void clearHistory();
        void deleteChat(Chat chat);
        void insertChat(Chat chat);
        Chat getChat(String name);
    }

    interface View {

    }
}

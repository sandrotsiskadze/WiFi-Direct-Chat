package com.example.wifidirectchat;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("SELECT * FROM chat")
    List<Chat> getChatList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChat(Chat... chats);

    @Delete
    void deleteChat(Chat chat);

    @Query("UPDATE chat SET messageList=(:messageList) WHERE id=(:id)")
    void updateChat(List<Message> messageList, int id);

    @Query("DELETE FROM chat")
    void clearHistory();

    @Query("SELECT * FROM chat WHERE name=(:name)")
    Chat getChat(String name);
}

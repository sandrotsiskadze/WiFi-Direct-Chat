package com.example.wifidirectchat;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MessageConverter {
    public static Gson gson = new Gson();

    @TypeConverter
    public static List<Message> stringToList(String data) {
        if (data == null) return Collections.emptyList();
        Type type = new TypeToken<List<Message>>() {
        }.getType();
        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String listToString(List<Message> messageList) {
        return gson.toJson(messageList);
    }
}

package com.example.wifidirectchat;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {Chat.class}, version = 1, exportSchema = false)
@TypeConverters({MessageConverter.class})
public abstract class Database extends RoomDatabase {
    private static final Object lock = new Object();
    private static Database instance;

    public static Database getInstance(Context context) {
        synchronized (lock) {
            if (instance == null)
                instance = Room.databaseBuilder(context, Database.class, "name").allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract Dao dao();
}

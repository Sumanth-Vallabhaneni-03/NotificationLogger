package com.example.notificationlogger;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NotificationEntity.class}, version = 1)
public abstract class NotificationDatabase extends RoomDatabase {
    private static NotificationDatabase instance;

    public static NotificationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, NotificationDatabase.class, "notifications.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract NotificationDao notificationDao();
}

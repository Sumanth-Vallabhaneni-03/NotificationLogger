package com.example.notificationlogger;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String appName;
    public String title;
    public String text;
    public long timestamp;
    public String image; // base64-encoded image
}

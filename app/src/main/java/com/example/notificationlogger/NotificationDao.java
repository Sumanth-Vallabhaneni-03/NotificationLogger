package com.example.notificationlogger;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insert(NotificationEntity notification);

    @Query("SELECT * FROM NotificationEntity ORDER BY timestamp DESC")
    List<NotificationEntity> getAll();

    @Query("DELETE FROM NotificationEntity")
    void deleteAll();

    @Query("DELETE FROM NotificationEntity WHERE id = :id")
    void deleteById(int id);
}

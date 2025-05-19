package com.example.notificationlogger;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    NotificationDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        recyclerView = findViewById(R.id.recyclerView);
        Button clearButton = findViewById(R.id.clearButton);

        dao = NotificationDatabase.getInstance(this).notificationDao();
        adapter = new NotificationAdapter(dao.getAll(), dao, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        clearButton.setOnClickListener(view -> {
            dao.deleteAll();
            adapter.setData(dao.getAll());
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NotificationEntity item = adapter.getItem(position);
                dao.deleteById(item.id);
                adapter.removeItem(position);
            }
        }).attachToRecyclerView(recyclerView);

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> {
            List<NotificationEntity> updatedData = dao.getAll();
            adapter.updateData(updatedData); // You'll need to implement updateData() in the adapter
        });


        if (!Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners")
                .contains(getPackageName())) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }
    }
}

package com.example.notificationlogger;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationEntity> list;
    private NotificationDao dao;
    private Context context;

    public NotificationAdapter(List<NotificationEntity> list, NotificationDao dao, Context context) {
        this.list = list;
        this.dao = dao;
        this.context = context;
    }

    public void setData(List<NotificationEntity> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public NotificationEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    // This is your own method, not RecyclerView's
    public void updateData(List<NotificationEntity> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        NotificationEntity item = list.get(position);
        holder.appName.setText(item.appName);
        holder.title.setText(item.title);
        holder.text.setText(item.text);
        holder.time.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(item.timestamp)));

        if (item.image != null && !item.image.isEmpty()) {
            Bitmap bitmap = BitmapUtils.base64ToBitmap(item.image);
            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap);
                holder.image.setVisibility(View.VISIBLE);
            } else {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView appName, title, text, time;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appName);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
        }
    }
}

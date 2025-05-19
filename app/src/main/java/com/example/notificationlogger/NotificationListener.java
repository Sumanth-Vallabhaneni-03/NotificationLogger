package com.example.notificationlogger;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    // Hold the previous notification to delay storing
    private NotificationEntity pendingEntity = null;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pkg = sbn.getPackageName();

        if (!pkg.equals("com.whatsapp.w4b")) return;

        Notification notification = sbn.getNotification();
        if (notification == null) return;

        Bundle extras = notification.extras;

        String title = extras.getString(Notification.EXTRA_TITLE, "");
        String text = extras.getString(Notification.EXTRA_TEXT, "");

        // Prefer big text if available
        CharSequence bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        if (bigText != null) {
            text = bigText.toString();
        }

        // ✅ Skip notifications with title "WA Business"
        if ("WA Business".equals(title)) {
            return;
        }

        // Extract large icon and convert to base64
        Bitmap largeIconBitmap = null;
        Icon largeIcon = notification.getLargeIcon();
        // You can optionally load the icon if needed
        // (Currently skipped or commented)

        String imageBase64 = BitmapUtils.bitmapToBase64(largeIconBitmap);

        // Save to DB
        NotificationEntity entity = new NotificationEntity();
        entity.appName = pkg;
        entity.title = title;
        entity.text = text;
        entity.timestamp = System.currentTimeMillis();
        entity.image = imageBase64;

        NotificationDatabase.getInstance(getApplicationContext())
                .notificationDao()
                .insert(entity);
    }

    @Override
    public void onDestroy() {
        // Optionally: clear the pending notification on service destroy
        pendingEntity = null;
        super.onDestroy();
    }
}

package com.engingplan.womarketing.ui.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.engingplan.womarketing.ui.R;
import com.engingplan.womarketing.ui.activity.HomeActivity;
import com.engingplan.womarketing.ui.activity.LoginActivity;
import com.engingplan.womarketing.ui.receive.NotificationReceiver;


public class NoticeService extends IntentService {
    public NoticeService() {
        super("");
    }

    public NoticeService(String name) {
        super(name);
    }

    PendingIntent pendingIntent = null;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            String channelId = "notice";
            String channelName = "通知消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            sendChatMsg(title, content, channelId);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        //设置点击通知栏的动作为启动另外一个广播
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        pendingIntent = PendingIntent.
                getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager.createNotificationChannel(channel);
    }

    public void sendChatMsg(String title, String content, String channelId) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notice)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notice))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1, notification);
    }


}

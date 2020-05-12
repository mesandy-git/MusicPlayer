package com.my.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.SyncStateContract;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import static com.my.musicplayer.MediaPlayerService.ACTION_NEXT;
import static com.my.musicplayer.MediaPlayerService.ACTION_PLAY;

public class MusicNotification  {
    static String CHANNEL_ID = "2000";

    private static void CreateNotification(Context context, String Title) {
        RemoteViews notificationLayout = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_small);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_large);

        Intent intent = new Intent(context, MyMainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notificationCompat = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(pi)
                .build();
        NotificationManager notificationManager;

        Intent playIntent = new Intent(context, MediaPlayerService.class);
        playIntent.setAction(ACTION_PLAY);
        PendingIntent pplayIntent = PendingIntent.getService(context, 0, playIntent, 0);
        notificationLayout.setOnClickPendingIntent(R.id.playBtnSmall, pplayIntent);
//        notificationLayout.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
    }
}

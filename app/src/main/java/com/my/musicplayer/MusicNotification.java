package com.my.musicplayer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import static com.my.musicplayer.MediaPlayerService.ACTION_NEXT;
import static com.my.musicplayer.MediaPlayerService.ACTION_PLAY;
import static com.my.musicplayer.MediaPlayerService.ACTION_PREV;
import static com.my.musicplayer.MyMainActivity.controller;

public class MusicNotification {
    static String CHANNEL_ID = "2000";

    static NotificationCompat.Builder CreateNotification(Context context, String Title) {
        RemoteViews notificationLayout = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_small);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_large);
        Intent mIntent = new Intent(context, MediaPlayerService.class);
        mIntent.setAction(ACTION_PLAY);
        PendingIntent pPlayIntent = PendingIntent.getService(context, 0, mIntent, 0);
        notificationLayout.setOnClickPendingIntent(R.id.playBtnSmall, pPlayIntent);

        mIntent.setAction(ACTION_NEXT);
        PendingIntent pNextIntent = PendingIntent.getService(context, 0, mIntent, 0);
        notificationLayout.setOnClickPendingIntent(R.id.nextSmall, pNextIntent);

        mIntent.setAction(ACTION_PREV);
        PendingIntent pPrevIntent = PendingIntent.getService(context, 0, mIntent, 0);
        notificationLayout.setOnClickPendingIntent(R.id.previousSmall, pPrevIntent);

        Intent intent = new Intent(context, MyMainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setShowWhen(false)
                .setContentTitle(controller.getLastTrackName())
                .setContentText("My Awesome Band")
                .setSmallIcon(R.mipmap.music)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_fast_rewind_black_24dp, "Previous", pPrevIntent)
                .addAction(R.drawable.ic_play_arrow_black_24dp, "Play", pPlayIntent)
                .addAction(R.drawable.ic_fast_forward_black_24dp, "Next", pNextIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2))
                .build();

        return notificationBuilder;
    }
}

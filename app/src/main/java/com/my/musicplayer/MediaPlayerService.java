package com.my.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.my.musicplayer.BuildConfig;

public class MediaPlayerService extends Service {
    public static final String ACTION_NEXT= BuildConfig.APPLICATION_ID+".ACTION_NEXT";
    public static final String ACTION_PREV=BuildConfig.APPLICATION_ID+".ACTION_PREV";
    public static final String ACTION_PLAY=BuildConfig.APPLICATION_ID+".ACTION_PLAY";
    public static final String ACTION_PAUSE=BuildConfig.APPLICATION_ID+".ACTION_PAUSE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(ACTION_PLAY)) {
                    Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
                }else if(action.equals(ACTION_PAUSE)) {
                    Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
                }else if(action.equals(ACTION_PREV)) {
                    Toast.makeText(this, "Prev", Toast.LENGTH_SHORT).show();
                }else if(action.equals(ACTION_NEXT)) {
                    Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

package com.my.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private Context context;
    private Activity activity;
    private MediaPlayer player;
    private Timer timer;

    Player(Context mContext, Activity mActivity) {
        this.context = mContext;
        this.activity = mActivity;
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
    }


    private void setTimeDuration() {
        if (player != null) {
            player.start();
            MyMainActivity.endTime.setText("" + player.getDuration());
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (player != null && player.isPlaying()) {
                                    MyMainActivity.startTime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyMainActivity.startTime.setText("" + player.getCurrentPosition());
                                        }
                                    });
                                } else {
                                    timer.cancel();
                                    timer.purge();
                                }
                            } catch (Exception e) {
                                Log.e("Exceptions", e.toString());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 0, 1000);
        }

    }

    void playMusic(String AudioURL) {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(AudioURL);
            player.prepare();
            //Log.e("TymStamp->", "Length: " + player.getTrackInfo().length + " Duration:" + player.getDuration());
            //playBtnSmallController.setImageResource(R.drawable.ic_pause_black_24dp);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TymStamp->", "" + e.toString());
            //playBtnSmallController.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            player.reset();
            player.release();
            player = null;
            timer.cancel();
            timer.purge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        setTimeDuration();
    }
}

package com.my.musicplayer;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import static com.my.musicplayer.MyMainActivity.albumArtLarge;
import static com.my.musicplayer.MyMainActivity.albumArtSmall;
import static com.my.musicplayer.MyMainActivity.controller;
import static com.my.musicplayer.MyMainActivity.endTime;
import static com.my.musicplayer.MyMainActivity.getController;
import static com.my.musicplayer.MyMainActivity.pauseMusics;
import static com.my.musicplayer.MyMainActivity.playMusics;
import static com.my.musicplayer.MyMainActivity.songName;
import static com.my.musicplayer.MyMainActivity.songNameLarge;
import static com.my.musicplayer.MyMainActivity.startTime;
import static com.my.musicplayer.MyMainActivity.timerBar;

public class Controller extends Application implements ActivityLifecycleCallbacks,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {
    public Pref pref;
    public static MediaPlayer mediaPlayer;
    public static boolean flag = false;
    String lastTrackName = "Not Playing";
    static Handler mHandler;
    public ArrayList<AudioModel> tempAudioList;
    private AudioManager mAudioManager;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            int iii = 2;
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                iii = intent.getIntExtra("state", -1);
                if (Integer.valueOf(iii) == 0) {
                    Log.d(BuildConfig.APPLICATION_ID, "microphone not plugged");
                    musicPauseCallBack();
                }
                if (Integer.valueOf(iii) == 1) {
                    Log.d(BuildConfig.APPLICATION_ID, "microphone plugged");

                }
            }
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            int currentDuration = mediaPlayer.getCurrentPosition();
            try {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    startTime.post(() -> startTime.setText("" + (currentDuration / 1000) / 60 + ":" + (currentDuration / 1000) % 60));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        timerBar.setProgress(mediaPlayer.getCurrentPosition(), true);
                    else
                        timerBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            } catch (Exception e) {
                Log.e("Exceptions", e.toString());
                e.printStackTrace();
            }
            mHandler.postDelayed(this, 100);
        }
    };

    public String getLastTrackName() {
        return lastTrackName;
    }

    public void setLastTrackName(String lastTrackName) {
        this.lastTrackName = lastTrackName;
        pref.setLastMusicName(lastTrackName);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDarkTheme();
        mediaPlayer = new MediaPlayer();
        tempAudioList = new ArrayList<>();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        Log.e("Controller", "Created" + pref.getSelectFragment());
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, receiverFilter);
        mHandler = new Handler();
    }

    void setDarkTheme() {
        pref = new Pref(this);
        try {
            if (pref.getDarkTheme())
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void runMusicRefresh() {
        Handler handler = new Handler();
        Runnable r = () -> getAllAudioFromDevice();
        handler.postDelayed(r, 100);
    }

    private void setMusicList(ArrayList<AudioModel> data) {
        SharedPreferences shared = getController().getSharedPreferences("MUSICLIST", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        Gson gson = new Gson();
        String musicsList = gson.toJson(data);
        editor.putString("MUSICLISTS", musicsList);
        editor.commit();
    }

    public void playAudio(String path) {
        if (!path.equalsIgnoreCase("")) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                pauseMusics();
                mediaPlayer.reset();
            }
            try {
                mediaPlayer.reset();
                if (getAudioFocus()) {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    songName.setText(getLastTrackName());
                    songNameLarge.setText(getLastTrackName());
                    pref.setLastMusicPath(path);
                    setAlbumArt(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(this, "Can't play", Toast.LENGTH_SHORT).show();
    }

    private void setAlbumArt(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            albumArtSmall.setImageBitmap(bitmap);
            albumArtLarge.setImageBitmap(bitmap);
        }
    }

    private boolean getAudioFocus() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes mPlaybackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            AudioFocusRequest mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mPlaybackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setOnAudioFocusChangeListener(this)
                    .build();
            int aFocus = mAudioManager.requestAudioFocus(mFocusRequest);
            return aFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            return false;
        }
    }

    public static void musicPauseCallBack() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pauseMusics();
            controller.removeCallBack();
            controller.mAudioManager = (AudioManager) controller.getSystemService(Context.AUDIO_SERVICE);
            controller.mAudioManager.abandonAudioFocus(null);
        }
    }


    public static void musicPlayCallBack() {
        if (!mediaPlayer.isPlaying()) {
            controller.getAudioFocus();
            mediaPlayer.start();
            playMusics();
            controller.updateProgressBar();
        }
    }

    public void updateProgressBar() {
        int totalDuration = mediaPlayer.getDuration();
        timerBar.setMax(totalDuration);
        endTime.setText("" + (totalDuration / 1000) / 60 + ":" + (totalDuration / 1000) % 60);
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public void removeCallBack() {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {


    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        unregisterReceiver(mReceiver);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    private ArrayList<AudioModel> getPlayList(String rootPath) {
        ArrayList<AudioModel> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles();
            for (File file : files != null ? files : new File[0]) {
                if (file.isDirectory()) {
                    if (file.toString().contains(".") || file.toString().contains("Android")) {
                        continue;
                    } else if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    AudioModel audioModel = new AudioModel();
                    audioModel.setaPath(file.getAbsolutePath());
                    audioModel.setaName(file.getName().replace(".mp3", ""));
                    fileList.add(audioModel);
                }
            }
            return fileList;
        } catch (Exception e) {
            Log.d("Ex", e.getMessage(), e);
            return null;
        }
    }

    public void getAllAudioFromDevice() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                AudioModel audioModel = new AudioModel();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                // String artist = c.getString(3);
                audioModel.setaName(name);
                audioModel.setaAlbum(album);
//                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
//                Log.e("Name :" + name, " Album :" + album);
//                Log.e("Path :" + path, " Artist :" + artist);
                tempAudioList.add(audioModel);
            }
            c.close();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempAudioList.sort((s1, s2) -> s1.getaName().compareTo(s2.getaName()));
        }
        Log.d("ListCreate", "List Created");
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange <= 0) {
            if (mediaPlayer.isPlaying())
                musicPauseCallBack();
        }
//        else {    musicPlayCallBack();    }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        updateProgressBar();
        flag = true;
        playMusics();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            mediaPlayer.reset();
            controller.removeCallBack();
            nextTrack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void nextTrack() {
        if (tempAudioList.size() > 0) {
            int pos = tempAudioList.size() - 1 == pref.getTrackPosition() ? 0 : pref.getTrackPosition() + 1;
            String path = tempAudioList.get(pos).getaPath();
            setLastTrackName(tempAudioList.get(pos).getaName());
            pref.setTrackPosition(pos);
            playAudio(path);
            Log.d("Position", "" + pos + " " + tempAudioList.get(pos).getaName());
        }
    }

    void previousTrack() {
        if (tempAudioList.size() > 0) {
            int pos = pref.getTrackPosition() == 0 ? tempAudioList.size() - 1 : pref.getTrackPosition() - 1;
            String path = tempAudioList.get(pos).getaPath();
            setLastTrackName(tempAudioList.get(pos).getaName());
            pref.setTrackPosition(pos);
            playAudio(path);
            Log.d("Position", "" + pos + " " + tempAudioList.get(pos).getaName());
        }
    }



}


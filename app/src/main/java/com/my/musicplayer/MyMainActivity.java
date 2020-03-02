package com.my.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;

public class MyMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private RelativeLayout smallSizeMusicController;
    private ConstraintLayout fullSizeMusicController;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView song_name;
    public static TextView startTime, endTime;
    private ImageView playBtnSmallController;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private MediaPlayer mediaPlayer;
    static Controller controller;
    static BottomNavigationView bottomNavigationView;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        controller = (Controller) getApplicationContext();
        setDarkTheme();
        bottomSheet();
        loadFragment(new LibraryFragment());
        initControls();
        playBtnSmallController.setOnClickListener(this);
        player = new Player(getApplicationContext(), this);
    }

    void setDarkTheme() {
        try {
            if (controller.pref.getDarkTheme())
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bottomSheet() {
        playBtnSmallController = findViewById(R.id.playBtnSmallController);
        smallSizeMusicController = findViewById(R.id.smallSizeMusicController);
        fullSizeMusicController = findViewById(R.id.fullSizeMusicController);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.standardBottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    smallSizeMusicController.setVisibility(View.GONE);
                    fullSizeMusicController.setBackgroundResource(R.drawable.bottom_sheet_corner);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    smallSizeMusicController.setVisibility(View.VISIBLE);
                    fullSizeMusicController.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    fullSizeMusicController.setVisibility(View.VISIBLE);
                    fullSizeMusicController.setBackgroundResource(R.drawable.bottom_sheet_corner);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });
    }

    static Controller getController() {
        return controller;
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    private void playMusics(String AudioURL) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(AudioURL);
            mediaPlayer.prepare();
            Log.e("TymStamp->", "Length: " + mediaPlayer.getTrackInfo().length + " Duration:" + mediaPlayer.getDuration());

            playBtnSmallController.setImageResource(R.drawable.ic_pause_black_24dp);
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TymStamp->",""+e.toString());
            playBtnSmallController.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                loadFragment(new LibraryFragment());
                //Toast.makeText(this, "Library", Toast.LENGTH_SHORT).show();
                break;
            case R.id.musics:
                loadFragment(new MusicFragment());
                //Toast.makeText(this, "Musics", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                loadFragment(new SearchFragment());
                //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                loadFragment(new SettingFragment());
                //Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playBtnSmallController) {
            String AudioURL = "https://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3";
            //playMusic(AudioURL);
            //playBtnSmallController.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            player.playMusic(AudioURL);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (isFinishing()) {
                Log.e("Restart-> ", "True");
            } else {
                Log.e("Restart-> ", "false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(MyMainActivity.class.getSimpleName(), "OnConfiguration Changed Called");
    }

    private void initControls() {
        try {
            volumeSeekbar = findViewById(R.id.volumeBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}

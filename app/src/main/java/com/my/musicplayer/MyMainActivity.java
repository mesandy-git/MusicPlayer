package com.my.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.my.musicplayer.Fragments.LibraryFragment;
import com.my.musicplayer.Fragments.MusicFragment;
import com.my.musicplayer.Fragments.SearchFragment;
import com.my.musicplayer.Fragments.SettingFragment;

import static com.my.musicplayer.Controller.flag;
import static com.my.musicplayer.Controller.mediaPlayer;
import static com.my.musicplayer.Controller.musicPauseCallBack;
import static com.my.musicplayer.Controller.musicPlayCallBack;

public class MyMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private RelativeLayout smallSizeMusicController;
    private ConstraintLayout fullSizeMusicController;
    private BottomSheetBehavior bottomSheetBehavior;
    static TextView songName;
    private int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 2000;
    public static TextView startTime, endTime;
    private static ImageView playBtnSmallController;
    static ImageView albumArtSmall, albumArtLarge;
    private static ImageView playBtnLargeController;
    private SeekBar volumeSeekbar = null;
    public static SeekBar timerBar = null;
    private AudioManager audioManager = null;
    static Controller controller;
    public static BottomNavigationView bottomNavigationView;
    private String isPlay;
    private String itemName;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        controller = (Controller) getApplicationContext();
        activity = MyMainActivity.this;
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        songName = findViewById(R.id.song_name);
        setDarkTheme();
        bottomSheet();
        loadFragment(new LibraryFragment());
        itemName = "library";
        initControls();
        seekController();
        playBtnSmallController.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                musicPauseCallBack();

            } else if (!mediaPlayer.isPlaying() && flag) {
                musicPlayCallBack();
            } else {
                controller.setLastTrackName(controller.pref.getLastMusicName());
                controller.playAudio(controller.pref.getLastMusicPath());
            }
        });
        playBtnLargeController.setOnClickListener(v -> playBtnSmallController.performClick());
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
        playBtnLargeController = findViewById(R.id.playBtnLargeController);
        albumArtLarge = findViewById(R.id.albumArtLarge);
        albumArtSmall = findViewById(R.id.albumArtSmall);
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
//                    fullSizeMusicController.setBackgroundResource(R.drawable.bottom_sheet_corner);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    smallSizeMusicController.setVisibility(View.VISIBLE);
                    fullSizeMusicController.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    fullSizeMusicController.setVisibility(View.VISIBLE);
//                    fullSizeMusicController.setBackgroundResource(R.drawable.bottom_sheet_corner);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });
    }

    public static Controller getController() {
        return controller;
    }

    public static Activity getAct() {
        return activity;
    }

    static void playMusics() {
        playBtnSmallController.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        playBtnLargeController.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);

    }

    static void pauseMusics() {
        playBtnSmallController.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
        playBtnLargeController.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                if (!itemName.equalsIgnoreCase("library")) {
                    loadFragment(new LibraryFragment());
                    itemName = "library";
                }
                break;
            case R.id.musics:
                if (!itemName.equalsIgnoreCase("music")) {
                    loadFragment(new MusicFragment());
                    itemName = "music";
                }
                break;
            case R.id.search:
                if (!itemName.equalsIgnoreCase("search")) {
                    loadFragment(new SearchFragment());
                    itemName = "search";
                }
                break;

            case R.id.setting:
                if (!itemName.equalsIgnoreCase("setting")) {
                    itemName = "setting";
                    loadFragment(new SettingFragment());
                }
                break;
        }
        return true;
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

    private void seekController() {
        timerBar = findViewById(R.id.timerBar);
        timerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//              startTime.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                controller.removeCallBack();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                controller.removeCallBack();
                mediaPlayer.seekTo(seekBar.getProgress());
                controller.updateProgressBar();
            }
        });

    }

//    public void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.addToBackStack(fragment.toString());
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.commit();
//    }

    public void loadFragment(Fragment fragment) {
        String tagFragmentName = fragment.getClass().getName();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.frameLayout, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    private void getPermissions() {
        controller = (Controller) getApplicationContext();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission has already been granted
            controller.runMusicRefresh();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE && grantResults.length != 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                controller.runMusicRefresh();

        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        songName.setText(controller.pref.getLastMusicName());
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                playMusics();
            } else {
                isPlay = "no";
                pauseMusics();
            }
        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        volumeSeekbar = findViewById(R.id.volumeBar);
        volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                }
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                }
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}

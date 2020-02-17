package com.my.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MyMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private RelativeLayout smallSizeMusicController;
    private ConstraintLayout fullSizeMusicController;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView song_name;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        smallSizeMusicController = findViewById(R.id.smallSizeMusicController);
        fullSizeMusicController = findViewById(R.id.fullSizeMusicController);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.standardBottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(false);
        loadFragment(new LibraryFragment());
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
        Button tvChangeTheme = findViewById(R.id.tvChangeTheme);
        tvChangeTheme.setOnClickListener(this);
        //tvChangeTheme.setVisibility(View.GONE);
        initControls();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                loadFragment(new LibraryFragment());
                Toast.makeText(this, "Library", Toast.LENGTH_SHORT).show();
                break;
            case R.id.musics:
                loadFragment(new MusicFragment());
                Toast.makeText(this, "Musics", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                loadFragment(new SearchFragment());
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                loadFragment(new SettingFragment());
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChangeTheme: {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.e("Mode", "if");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.e("Mode", "else");
                }
                MyMainActivity.this.recreate();
            }
        }
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

    private void loadFragment(Fragment fragment) {
//      create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
//      create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//      replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}

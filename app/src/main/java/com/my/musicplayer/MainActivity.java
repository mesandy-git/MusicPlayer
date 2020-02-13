package com.my.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout smallSizeMusicController;
    private ConstraintLayout fullSizeMusicController;
    private FrameLayout standardBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smallSizeMusicController = findViewById(R.id.smallSizeMusicController);
        fullSizeMusicController = findViewById(R.id.fullSizeMusicController);
        standardBottomSheet = findViewById(R.id.standardBottomSheet);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.standardBottomSheet));

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    smallSizeMusicController.setVisibility(View.GONE);
                    fullSizeMusicController.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                Toast.makeText(this, "Library", Toast.LENGTH_SHORT).show();
                break;
            case R.id.musics:
                Toast.makeText(this, "Musics", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}

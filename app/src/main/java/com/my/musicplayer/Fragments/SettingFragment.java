package com.my.musicplayer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.my.musicplayer.MyMainActivity;
import com.my.musicplayer.R;

public class SettingFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    MyMainActivity myMainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        MyMainActivity myMainActivity;
        setThemes(view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {

    }
    private void setThemes(View view) {
        Switch darkMode = view.findViewById(R.id.darkMode);
        if (MyMainActivity.getController().pref.getDarkTheme())
            darkMode.setChecked(true);
        else
            darkMode.setChecked(false);
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    MyMainActivity.getController().pref.setDarkTheme(true);
                    MyMainActivity.bottomNavigationView.setSelectedItemId(R.id.library);

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    MyMainActivity.getController().pref.setDarkTheme(false);
                    MyMainActivity.bottomNavigationView.setSelectedItemId(R.id.library);
                }
            }
        });
    }

}

package com.my.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private final String DARKTHEME = "dark_theme";
    private final String selectFragment = "selectFragment";
    private SharedPreferences sharedPreferences;
    private final String lastMusicPath = "lastMusic";
    private final String lastMusicName = "lastMusicName";

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    Pref(Context context) {
        sharedPreferences = context.getSharedPreferences("Musics", Context.MODE_PRIVATE);
    }
    int getTrackPosition() {
        return sharedPreferences.getInt("track_pos", 0);
    }

    public void setTrackPosition(int trackPosition) {
        edit().putInt("track_pos", trackPosition).commit();
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Boolean getDarkTheme() {
        return sharedPreferences.getBoolean(DARKTHEME, false);
    }

    String getLastMusicPath() {
        return sharedPreferences.getString(lastMusicPath, "");
    }

    void setLastMusicPath(String str) {
        edit().putString(lastMusicPath, str).commit();
    }

    String getLastMusicName() {
        return sharedPreferences.getString(lastMusicName, "Not Playing");
    }

    void setLastMusicName(String str) {
        edit().putString(lastMusicName, str).commit();
    }

    public void setDarkTheme(Boolean b) {
        edit().putBoolean(DARKTHEME, b).commit();
    }

    void setSelectFragment(Boolean b) {
        edit().putBoolean(selectFragment, b).commit();
    }

    private SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    boolean getSelectFragment() {
        return sharedPreferences.getBoolean(DARKTHEME, false);
    }
}

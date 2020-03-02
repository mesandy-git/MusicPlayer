package com.my.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private final String DARKTHEME = "dark_theme";
    private final String selectFragment = "selectFragment";
    private SharedPreferences sharedPreferences;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    Pref(Context context) {
        sharedPreferences = context.getSharedPreferences("Musics", Context.MODE_PRIVATE);
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    Boolean getDarkTheme() {
        return sharedPreferences.getBoolean(DARKTHEME, false);
    }

    void setDarkTheme(Boolean b) {
        edit().putBoolean(DARKTHEME, b).commit();
    }
    void setSelectFragment(Boolean b) {
        edit().putBoolean(selectFragment, b).commit();
    }

    private SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    public boolean getSelectFragment() {
        return sharedPreferences.getBoolean(DARKTHEME, false);
    }
}

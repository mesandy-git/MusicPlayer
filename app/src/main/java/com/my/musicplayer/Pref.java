package com.my.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
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

    private final String DARKTHEME = "dark_theme";

    Boolean getDARKTHEME() {
        return sharedPreferences.getBoolean(DARKTHEME, false);
    }

    void setDARKTHEME(Boolean b) {
        edit().putBoolean(DARKTHEME, b).commit();
    }

    private SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }


}

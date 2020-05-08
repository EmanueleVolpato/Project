package com.example.projectwork;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Nightmode", state);
        editor.commit();
    }

    public Boolean loadNightModeState() {
        Boolean state = sharedPreferences.getBoolean("Nightmode", false);
        return state;
    }
}

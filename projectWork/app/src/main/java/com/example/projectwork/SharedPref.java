package com.example.projectwork;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences sharedPreferences;
    public SharedPref(Context context)
    {
        sharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }


}

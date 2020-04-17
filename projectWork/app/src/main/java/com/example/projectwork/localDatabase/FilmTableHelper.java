package com.example.projectwork.localDatabase;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "film";
    public static final String TITOLO = "titolo";
    public static final String DESCRIZIONE = "descrizione";


    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DESCRIZIONE + " TEXT, " +
            TITOLO + " TEXT );";

}
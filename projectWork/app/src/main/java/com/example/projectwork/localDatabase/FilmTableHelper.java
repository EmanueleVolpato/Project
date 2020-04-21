package com.example.projectwork.localDatabase;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "film";
    public static final String ID_MOVIE = "id_movie";
    public static final String TITOLO = "titolo";
    public static final String FILM_ID = "filmId";
    public static final String DESCRIZIONE = "descrizione";
    public static final String IMG_PRINCIPALE = "img_principale";
    public static final String IMG_DETTAGLIO = "img_decrizione";
    public static final String FLAG_PREFERITO = "flag_preferito";


    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ID_MOVIE + " TEXT, " +
            DESCRIZIONE + " TEXT, " +
            FILM_ID + " INTEGER, " +
            IMG_PRINCIPALE + " TEXT, " +
            IMG_DETTAGLIO + " TEXT, " +
            FLAG_PREFERITO + " INT, " +
            TITOLO + " TEXT );";
}
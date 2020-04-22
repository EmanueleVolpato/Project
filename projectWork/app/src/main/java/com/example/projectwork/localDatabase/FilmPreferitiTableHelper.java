package com.example.projectwork.localDatabase;

import android.provider.BaseColumns;

public class FilmPreferitiTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "filmPreferiti";
    public static final String ID_MOVIE = "id_movie";
    public static final String TITOLO = "titolo";
    public static final String DESCRIZIONE = "descrizione";
    public static final String IMG_PRINCIPALE = "img_principale";
    public static final String IMG_DETTAGLIO = "img_decrizione";


    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            ID_MOVIE + " TEXT, " +
            DESCRIZIONE + " TEXT, " +
            IMG_PRINCIPALE + " TEXT, " +
            IMG_DETTAGLIO + " TEXT, " +
            TITOLO + " TEXT );";
}
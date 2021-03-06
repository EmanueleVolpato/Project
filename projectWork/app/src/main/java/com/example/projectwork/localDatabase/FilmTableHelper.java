package com.example.projectwork.localDatabase;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "film";
    public static final String ID_MOVIE = "id_movie";
    public static final String TITOLO = "titolo";
    public static final String DATA = "data";
    public static final String VOTO = "voto";
    public static final String DESCRIZIONE = "descrizione";
    public static final String IMG_PRINCIPALE = "img_principale";
    public static final String IMG_DETTAGLIO = "img_decrizione";
    public static final String GENERI = "generi";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            ID_MOVIE + " TEXT PRIMARY KEY, " +
            DESCRIZIONE + " TEXT, " +
            DATA + " TEXT, " +
            IMG_PRINCIPALE + " TEXT, " +
            VOTO + " INTEGER, " +
            GENERI + " TEXT, " +
            IMG_DETTAGLIO + " TEXT, " +
            TITOLO + " TEXT );";
}
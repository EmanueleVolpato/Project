package com.example.projectwork.localDatabase;

import android.provider.BaseColumns;

public class FilmPreferredTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "filmPreferiti";
    public static String ID_MOVIE = "id_movie";
    public static final String DATA = "data";
    public static final String TITOLO = "titolo";
    public static final String VOTO = "voto";
    public static final String DESCRIZIONE = "descrizione";
    public static final String IMG_PRINCIPALE = "img_principale";
    public static final String IMG_DETTAGLIO = "img_decrizione";
    public static final String KEY_GUEST_VOTO = "key_guest_voto";
    public static final String GENERI = "generi";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            ID_MOVIE + " TEXT PRIMARY KEY, " +
            DESCRIZIONE + " TEXT, " +
            DATA + " TEXT, " +
            VOTO + " INTEGER, " +
            IMG_PRINCIPALE + " TEXT, " +
            IMG_DETTAGLIO + " TEXT, " +
            KEY_GUEST_VOTO + " TEXT, " +
            GENERI + " TEXT, " +
            TITOLO + " TEXT );";
}
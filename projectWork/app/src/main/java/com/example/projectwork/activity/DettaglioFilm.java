package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferitiProvider;
import com.example.projectwork.localDatabase.FilmPreferitiTableHelper;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;

import java.util.ArrayList;
import java.util.List;

public class DettaglioFilm extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio, imgStella;
    Cursor mCursor;
    String idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imageViewDettaglio = findViewById(R.id.imageViewDettaglio);
        imgStella = findViewById(R.id.stella);

        if (getIntent().getExtras() != null) {
            final String titolo = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            final String descrizione = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            final String immagineDettaglio = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
            final String immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);

            idMovie = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);

            if (immagineDettaglio.equals(null) || immagineDettaglio.equals("") || (TextUtils.isEmpty(immagineDettaglio)))
            //immagineDettaglio = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);
            {
                Glide.with(DettaglioFilm.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                        .into(imageViewDettaglio);
            } else {
                Glide.with(DettaglioFilm.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immaginePrincipale)
                        .into(imageViewDettaglio);
            }

            txtTitolo.setText(titolo);
            txtDecrizione.setText(descrizione);

            String[] selectionArgs = {idMovie};
            mCursor = DettaglioFilm.this.getContentResolver().query(
                    FilmPreferitiProvider.FILMS_URI,
                    null,
                    FilmPreferitiTableHelper.ID_MOVIE + " = ?",
                    selectionArgs,
                    null);

            while (mCursor.moveToNext()) {
                imgStella.setImageResource(R.drawable.star_piena);
            }



            imgStella.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String[] selectionArgs = {idMovie};

                    mCursor = DettaglioFilm.this.getContentResolver().query(
                            FilmPreferitiProvider.FILMS_URI,
                            null,
                            FilmPreferitiTableHelper.ID_MOVIE + " = ?",
                            selectionArgs,
                            null);

                    int index = mCursor.getColumnIndex(FilmPreferitiTableHelper.ID_MOVIE);
                    String idDB = null;
                    while (mCursor.moveToNext()) {
                        Log.d("AAA", mCursor.getString(index));
                        idDB = mCursor.getString(index);
                    }

                    if (idDB != null) {
                        Toast.makeText(DettaglioFilm.this, "Film già inserito", Toast.LENGTH_SHORT).show();
                    } else {
                        imgStella.setImageResource(R.drawable.star_piena);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(FilmPreferitiTableHelper.ID_MOVIE, idMovie);
                        contentValues.put(FilmPreferitiTableHelper.TITOLO, titolo);
                        contentValues.put(FilmPreferitiTableHelper.DESCRIZIONE, descrizione);
                        contentValues.put(FilmPreferitiTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                        contentValues.put(FilmPreferitiTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                        DettaglioFilm.this.getContentResolver().insert(FilmPreferitiProvider.FILMS_URI, contentValues);

                        Log.d("AAAA", "Salvato film " + idMovie + titolo + descrizione + immagineDettaglio + immaginePrincipale);
                    }
                }
            });
        }
    }
}

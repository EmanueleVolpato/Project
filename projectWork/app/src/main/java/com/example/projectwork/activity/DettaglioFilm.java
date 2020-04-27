package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;

public class DettaglioFilm extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione,txtData;
    ImageView imgDettaglio, imgStella;
    Cursor mCursor;
    String idFilm;
    Dialog myDialog;
    String immagineDettaglio;
    String titolo;
    Button btnOk, btnCancel;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imgDettaglio = findViewById(R.id.imageViewDettaglio);
        txtData = findViewById(R.id.textViewDataFilm);

        imgStella = findViewById(R.id.stella);
        myDialog = new Dialog(this);

        if (getIntent().getExtras() != null) {
            titolo = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            final String descrizione = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            immagineDettaglio = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
            final String immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);
            data = getIntent().getExtras().getString(FilmTableHelper.DATA);

            idFilm = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);

            Glide.with(DettaglioFilm.this)
                    .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                    .into(imgDettaglio);

            txtTitolo.setText(titolo);
            txtData.setText(data);


            if (descrizione != "")
                txtDecrizione.setText(descrizione);
            else
                txtDecrizione.setText("NESSUNA OVERVIEW DISPONIBILE");

            String[] selectionArgs = {idFilm};
            mCursor = DettaglioFilm.this.getContentResolver().query(
                    FilmPreferredProvider.FILMS_URI,
                    null,
                    FilmPreferredTableHelper.ID_MOVIE + " = ?",
                    selectionArgs,
                    null);

            while (mCursor.moveToNext()) {
                imgStella.setImageResource(R.drawable.star_piena);
            }

            imgStella.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String[] selectionArgs = {idFilm};

                    mCursor = DettaglioFilm.this.getContentResolver().query(
                            FilmPreferredProvider.FILMS_URI,
                            null,
                            FilmPreferredTableHelper.ID_MOVIE + " = ?",
                            selectionArgs,
                            null);

                    int index = mCursor.getColumnIndex(FilmPreferredTableHelper.ID_MOVIE);
                    String idDB = null;
                    while (mCursor.moveToNext()) {
                        Log.d("AAA", mCursor.getString(index));
                        idDB = mCursor.getString(index);
                    }

                    if (idDB != null) {
                        ShowPopup(v);
                    } else {
                        imgStella.setImageResource(R.drawable.star_piena);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(FilmPreferredTableHelper.ID_MOVIE, idFilm);
                        contentValues.put(FilmPreferredTableHelper.TITOLO, titolo);
                        contentValues.put(FilmPreferredTableHelper.DATA, data);
                        contentValues.put(FilmPreferredTableHelper.DESCRIZIONE, descrizione);
                        contentValues.put(FilmPreferredTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                        contentValues.put(FilmPreferredTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                        DettaglioFilm.this.getContentResolver().insert(FilmPreferredProvider.FILMS_URI, contentValues);

                        Log.d("AAAA", "Salvato film " + idFilm + titolo + descrizione + immagineDettaglio + immaginePrincipale);
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ShowPopup(View v) {
        myDialog.setContentView(R.layout.dialog);
        btnCancel = myDialog.findViewById(R.id.buttoncancel);
        btnOk = myDialog.findViewById(R.id.buttonok);
        ImageView imageViewCancel;
        imageViewCancel = myDialog.findViewById(R.id.imageViewfilm);
        TextView textViewtitoloo;
        textViewtitoloo = myDialog.findViewById(R.id.textViewtitolo);

        textViewtitoloo.setText("Vuoi togliere " + titolo + " dai preferiti?");
        Glide.with(DettaglioFilm.this)
                .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                .into(imageViewCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(Uri.parse(String.valueOf(FilmPreferredProvider.FILMS_URI)), FilmPreferredTableHelper.ID_MOVIE + "=" + idFilm, null);
                imgStella.setImageResource(R.drawable.star);
                myDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}

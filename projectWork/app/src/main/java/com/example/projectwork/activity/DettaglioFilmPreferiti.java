package com.example.projectwork.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;

public class DettaglioFilmPreferiti  extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio;
    String idFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film_preferito);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imageViewDettaglio = findViewById(R.id.imageViewDettaglio);

        if (getIntent().getExtras() != null) {
            final String titolo = getIntent().getExtras().getString(FilmPreferredTableHelper.TITOLO);
            final String descrizione = getIntent().getExtras().getString(FilmPreferredTableHelper.DESCRIZIONE);
            final String immagineDettaglio = getIntent().getExtras().getString(FilmPreferredTableHelper.IMG_DETTAGLIO);
            idFilm = getIntent().getExtras().getString(FilmPreferredTableHelper.ID_MOVIE);

            Glide.with(DettaglioFilmPreferiti.this)
                    .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                    .into(imageViewDettaglio);

            txtTitolo.setText(titolo);
            txtDecrizione.setText(descrizione);

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

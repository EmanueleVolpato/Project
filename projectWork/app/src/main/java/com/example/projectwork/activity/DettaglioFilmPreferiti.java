package com.example.projectwork.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferitiTableHelper;

public class DettaglioFilmPreferiti  extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio;
    String idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film_preferito);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imageViewDettaglio = findViewById(R.id.imageViewDettaglio);

        if (getIntent().getExtras() != null) {
            final String titolo = getIntent().getExtras().getString(FilmPreferitiTableHelper.TITOLO);
            final String descrizione = getIntent().getExtras().getString(FilmPreferitiTableHelper.DESCRIZIONE);
            final String immagineDettaglio = getIntent().getExtras().getString(FilmPreferitiTableHelper.IMG_DETTAGLIO);
            final String immaginePrincipale = getIntent().getExtras().getString(FilmPreferitiTableHelper.IMG_PRINCIPALE);
            idMovie = getIntent().getExtras().getString(FilmPreferitiTableHelper.ID_MOVIE);

            Toast.makeText(DettaglioFilmPreferiti.this,idMovie+"",Toast.LENGTH_LONG).show();


            if (immagineDettaglio.equals(null) || immagineDettaglio.equals("") || (TextUtils.isEmpty(immagineDettaglio)))
            {
                Glide.with(DettaglioFilmPreferiti.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                        .into(imageViewDettaglio);
            } else {
                Glide.with(DettaglioFilmPreferiti.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immaginePrincipale)
                        .into(imageViewDettaglio);
            }


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

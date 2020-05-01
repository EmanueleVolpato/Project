package com.example.projectwork.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DettaglioFilmPreferiti  extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio;
    String idFilm, titolo, data;
    FloatingActionButton btnInformzioniFilmPreferiti;
    Dialog dialogInformzioniPreferiti;
    Dialog dialogVotaFilmPreferiti;
    String immagineDettaglio;
    String descrizione, voto;
    SharedPref sharedPref;
    RatingBar ratingBarpreferiti;
    private int oldScrollYPostion = 0;
    ScrollView scrollViewPreferiti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film_preferito);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglioPreferito);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglioPreferito);
        imageViewDettaglio = findViewById(R.id.imageViewDettaglioPreferito);
        btnInformzioniFilmPreferiti = findViewById(R.id.buttonApriDialogInformzioniPreferito);
        ratingBarpreferiti = findViewById(R.id.ratingBarVotoFilmPreferito);
        dialogInformzioniPreferiti = new Dialog(DettaglioFilmPreferiti.this);
        dialogVotaFilmPreferiti = new Dialog(DettaglioFilmPreferiti.this);
        scrollViewPreferiti = findViewById(R.id.scrollViewPreferiti);


        scrollViewPreferiti.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollViewPreferiti.getScrollY() > oldScrollYPostion) {
                    btnInformzioniFilmPreferiti.hide();
                } else if (scrollViewPreferiti.getScrollY() < oldScrollYPostion || scrollViewPreferiti.getScrollY() <= 0) {
                    btnInformzioniFilmPreferiti.show();
                }
                oldScrollYPostion = scrollViewPreferiti.getScrollY();
            }
        });


        if (getIntent().getExtras() != null) {
            titolo = getIntent().getExtras().getString(FilmPreferredTableHelper.TITOLO);
            descrizione = getIntent().getExtras().getString(FilmPreferredTableHelper.DESCRIZIONE);
            immagineDettaglio = getIntent().getExtras().getString(FilmPreferredTableHelper.IMG_DETTAGLIO);
            data = getIntent().getExtras().getString(FilmPreferredTableHelper.DATA);
            idFilm = getIntent().getExtras().getString(FilmPreferredTableHelper.ID_MOVIE);
            voto = (getIntent().getExtras().getString(FilmPreferredTableHelper.VOTO));


            int valore = Math.round(Float.parseFloat(voto)/2);
            ratingBarpreferiti.setRating((valore));


            Glide.with(DettaglioFilmPreferiti.this)
                    .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                    .into(imageViewDettaglio);

            txtTitolo.setText(titolo);
            if (!descrizione.equals(""))
                txtDecrizione.setText(descrizione);
            else {

                Toast.makeText(DettaglioFilmPreferiti.this,"Nessuna descrizione disponibile al momento",Toast.LENGTH_SHORT).show();

            }        }



        btnInformzioniFilmPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DettaglioFilmPreferiti.this, InformazioniAggiuntiveFilmPreferito.class);
                Bundle bundle = new Bundle();
                bundle.putString(FilmPreferredTableHelper.ID_MOVIE, idFilm);
                bundle.putString(FilmPreferredTableHelper.TITOLO,titolo);
                bundle.putString(FilmPreferredTableHelper.DATA, data);
                bundle.putString(FilmPreferredTableHelper.DESCRIZIONE, descrizione);
                bundle.putString(FilmPreferredTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}





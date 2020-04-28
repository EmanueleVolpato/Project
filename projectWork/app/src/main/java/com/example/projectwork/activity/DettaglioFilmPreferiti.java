package com.example.projectwork.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;

public class DettaglioFilmPreferiti  extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio;
    String idFilm, titolo, data;
    Button btnInformzioniFilmPreferiti;
    Dialog dialogInformzioniPreferiti;
    Dialog dialogVotaFilmPreferiti;
    String immagineDettaglio;
    String descrizione,voto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film_preferito);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imageViewDettaglio = findViewById(R.id.imageViewDettaglio);
        btnInformzioniFilmPreferiti = findViewById(R.id.buttonApriDialogInformzioniPreferiti);

        dialogInformzioniPreferiti = new Dialog(DettaglioFilmPreferiti.this);
        dialogVotaFilmPreferiti = new Dialog(DettaglioFilmPreferiti.this);


        if (getIntent().getExtras() != null) {
            titolo = getIntent().getExtras().getString(FilmPreferredTableHelper.TITOLO);
            descrizione = getIntent().getExtras().getString(FilmPreferredTableHelper.DESCRIZIONE);
            immagineDettaglio = getIntent().getExtras().getString(FilmPreferredTableHelper.IMG_DETTAGLIO);
            data = getIntent().getExtras().getString(FilmPreferredTableHelper.DATA);
            idFilm = getIntent().getExtras().getString(FilmPreferredTableHelper.ID_MOVIE);
            voto = (getIntent().getExtras().getString(FilmPreferredTableHelper.VOTO));



        Glide.with(DettaglioFilmPreferiti.this)
                .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                .into(imageViewDettaglio);

        txtTitolo.setText(titolo);
        txtDecrizione.setText(descrizione);


        btnInformzioniFilmPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInformzioniPreferiti.setContentView(R.layout.dialog_informzioni_preferiti);
                ImageView imageViewCopertina;
                imageViewCopertina = dialogInformzioniPreferiti.findViewById(R.id.imageViewfilmLikePreferiti);
                TextView titoloo;
                titoloo = dialogInformzioniPreferiti.findViewById(R.id.textViewtitoloLikePreferiti);
                ImageView esc;
                esc = dialogInformzioniPreferiti.findViewById(R.id.buttoncancelLikePreferiti);
                TextView dataUscita;
                dataUscita = dialogInformzioniPreferiti.findViewById(R.id.textViewDataDiUscitaPreferiti);
                ImageView imgVota = dialogInformzioniPreferiti.findViewById(R.id.imageViewVotaFilmPreferiti);


                dataUscita.setText(data);

                imgVota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopupVotaFilm(v);
                    }
                });


                esc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInformzioniPreferiti.dismiss();
                    }
                });


                Glide.with(DettaglioFilmPreferiti.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                        .into(imageViewCopertina);


                Float valutazione = Float.valueOf(voto)*10;
                int valore = Math.round(valutazione) ;



                ProgressBar progressBarPreferiti;
                progressBarPreferiti = dialogInformzioniPreferiti.findViewById(R.id.progressBarPreferiti);
                progressBarPreferiti.setMax(100);
                progressBarPreferiti.setProgress(valore);

                TextView votoAggiudicatoPreferiti;
                votoAggiudicatoPreferiti = dialogInformzioniPreferiti.findViewById(R.id.textVotoPreferiti);
                votoAggiudicatoPreferiti.setText(valore +"%");

                titoloo.setText(titolo);


                dialogInformzioniPreferiti.show();

            }
        });
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



    public void ShowPopupVotaFilm(View v) {
        dialogVotaFilmPreferiti.setContentView(R.layout.vota_dialog);
        ImageView imageViewCopertinaVoto;
        imageViewCopertinaVoto = dialogVotaFilmPreferiti.findViewById(R.id.imageViewCopertinaVotaFilm);

        Glide.with(DettaglioFilmPreferiti.this)
                .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                .into(imageViewCopertinaVoto);
        TextView titoloFilmVoto;
        titoloFilmVoto = dialogVotaFilmPreferiti.findViewById(R.id.textViewtitolovotaFilm);
        titoloFilmVoto.setText(titolo);

        RatingBar ratingBar;
        ratingBar = dialogVotaFilmPreferiti.findViewById(R.id.ratingBar);
        final TextView votoPersonale;
        votoPersonale = dialogVotaFilmPreferiti.findViewById(R.id.textViewvotoPersonale);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                votoPersonale.setText(String.valueOf("Il tuo voto "+(ratingBar.getRating()*2) +" /10"));
            }
        });


        ImageView escVota,votaFilm;
        escVota = dialogVotaFilmPreferiti.findViewById(R.id.buttonturnback);
        votaFilm = dialogVotaFilmPreferiti.findViewById(R.id.imgVotaFilm);

        escVota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogVotaFilmPreferiti.dismiss();
            }
        });

        votaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DettaglioFilmPreferiti.this,"votato",Toast.LENGTH_SHORT).show();
            }
        });

        dialogVotaFilmPreferiti.show();

    }

 }


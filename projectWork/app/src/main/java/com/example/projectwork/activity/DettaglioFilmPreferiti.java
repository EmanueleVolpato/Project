package com.example.projectwork.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

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

                int[] colorGreen = {Color.rgb(0, 187, 45), Color.rgb(156, 156, 156)};
                int[] colorRed = {Color.rgb(255, 0, 0), Color.rgb(156, 156, 156)};
                int[] colorArancio = {Color.rgb(255, 117, 20), Color.rgb(156, 156, 156)};
                int[] colorYellow = {Color.rgb(255, 255, 45), Color.rgb(156, 156, 156)};
                int[] colorBlue = {Color.rgb(3, 107, 218), Color.rgb(156, 156, 156)};


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


                Double valutazione = Double.valueOf(voto) * 10;
                Double conteggio = 100 - valutazione;

                PieChart pieChart = dialogInformzioniPreferiti.findViewById(R.id.pieChartPreferiti);

                Float number[] = {Float.valueOf(String.valueOf(valutazione)), Float.valueOf(String.valueOf(conteggio))};

                List<PieEntry> pieEntries = new ArrayList<>();
                for (int i = 0; i < number.length; i++) {
                    pieEntries.add(new PieEntry(number[i]));
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                final PieData data = new PieData(dataSet);
                pieChart.setUsePercentValues(true);
                data.setValueFormatter(new PercentFormatter(pieChart));
                pieChart.setHoleRadius(60);
                pieChart.getDescription().setEnabled(false);
                pieChart.setDrawRoundedSlices(true);

                if (valutazione <= 40) {
                    dataSet.setColors(colorRed);
                } else if (valutazione > 40 && valutazione <= 59) {
                    dataSet.setColors(colorArancio);
                } else if (valutazione >= 60 && valutazione <= 69) {
                    dataSet.setColors(colorYellow);
                } else if (valutazione > 69 && valutazione <= 89) {
                    dataSet.setColors(colorGreen);
                } else if (valutazione >= 90) {
                    dataSet.setColors(colorBlue);
                } else

                    pieChart.setRotationEnabled(false);
                pieChart.setTouchEnabled(false);
                pieChart.getLegend().setEnabled(false);
                data.setValueTextSize(15f);
                pieChart.animateX(1500);

                pieChart.setData(data);
                pieChart.invalidate();


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


package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class DettaglioFilm extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione,txtData;
    ImageView imgDettaglio, imgStella,imgVota;
    Cursor mCursor;
    String idFilm;
    String immagineDettaglio;
    String titolo;
    Button btnOk, btnCancel,btnInformzioni;
    String voto;
    String data;
    Dialog myDialoInfromazioniFilm, dialogVotaFilm,myDialogLikeFilm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imgDettaglio = findViewById(R.id.imageViewDettaglio);
        btnInformzioni = findViewById(R.id.buttonApriDialogInformzioni);
        myDialoInfromazioniFilm = new Dialog(DettaglioFilm.this);
        myDialogLikeFilm = new Dialog(DettaglioFilm.this);
        dialogVotaFilm = new Dialog(DettaglioFilm.this);

        if (getIntent().getExtras() != null) {
            titolo = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            final String descrizione = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            final String immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);
            data = getIntent().getExtras().getString(FilmTableHelper.DATA);
            idFilm = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);
            immagineDettaglio = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
            voto = (getIntent().getExtras().getString(FilmTableHelper.VOTO));


            Glide.with(DettaglioFilm.this)
                    .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                    .into(imgDettaglio);

            txtTitolo.setText(titolo);


            if (descrizione != "")
                txtDecrizione.setText(descrizione);
            else
                txtDecrizione.setText("NESSUNA OVERVIEW DISPONIBILE");


            btnInformzioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialoInfromazioniFilm.setContentView(R.layout.like_dialog);
                    ImageView imageViewCopertina;
                    imageViewCopertina = myDialoInfromazioniFilm.findViewById(R.id.imageViewfilmLike);
                    TextView titoloo;
                    titoloo = myDialoInfromazioniFilm.findViewById(R.id.textViewtitoloLike);
                    ImageView esc;
                    esc = myDialoInfromazioniFilm.findViewById(R.id.buttoncancelLike);
                    TextView dataUscita,genereFilm;
                    dataUscita = myDialoInfromazioniFilm.findViewById(R.id.textViewDataDiUscita);
                    genereFilm = myDialoInfromazioniFilm.findViewById(R.id.textViewgenereFilm);
                    imgStella = myDialoInfromazioniFilm.findViewById(R.id.imageViewpreferiti);
                    imgVota = myDialoInfromazioniFilm.findViewById(R.id.imageViewVotaFilm);


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
                            myDialoInfromazioniFilm.dismiss();
                        }
                    });


                    Double valutazione = Double.valueOf(voto)*10;
                    Double conteggio = 100 - valutazione;

                    PieChart pieChart = myDialoInfromazioniFilm.findViewById(R.id.pieChart);

                    Float number[] = {Float.valueOf(String.valueOf(valutazione)), Float.valueOf(String.valueOf(conteggio))};

                    List<PieEntry> pieEntries = new ArrayList<>();
                    for (int i = 0; i < number.length; i++) {
                        pieEntries.add(new PieEntry(number[i]));
                    }

                    PieDataSet dataSet = new PieDataSet(pieEntries, "");
                    final PieData dataa = new PieData(dataSet);
                    pieChart.setUsePercentValues(true);
                    dataa.setValueFormatter(new PercentFormatter(pieChart));
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
                    dataa.setValueTextSize(15f);
                    pieChart.animateX(1500);

                    pieChart.setData(dataa);
                    pieChart.invalidate();


                    titoloo.setText(titolo);


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
                                idDB = mCursor.getString(index);
                            }

                            if (idDB != null) {
                                ShowPopupPreferito(v);
                            } else {

                                imgStella.setImageResource(R.drawable.star_piena);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(FilmPreferredTableHelper.ID_MOVIE, idFilm);
                                contentValues.put(FilmPreferredTableHelper.TITOLO, titolo);
                                contentValues.put(FilmPreferredTableHelper.DATA, String.valueOf(data));
                                contentValues.put(FilmPreferredTableHelper.DESCRIZIONE, descrizione);
                                contentValues.put(FilmPreferredTableHelper.VOTO, voto);
                                contentValues.put(FilmPreferredTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                                contentValues.put(FilmPreferredTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                                DettaglioFilm.this.getContentResolver().insert(FilmPreferredProvider.FILMS_URI, contentValues);
                            }
                        }
                    });

                    Glide.with(DettaglioFilm.this)
                            .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                            .into(imageViewCopertina);

                    myDialoInfromazioniFilm.show();

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

    public void ShowPopupPreferito(View v) {
        myDialoInfromazioniFilm.dismiss();
        myDialogLikeFilm.setContentView(R.layout.dialog);
        btnCancel = myDialogLikeFilm.findViewById(R.id.buttoncancel);
        btnOk = myDialogLikeFilm.findViewById(R.id.buttonok);
        ImageView imageViewCancel;
        imageViewCancel = myDialogLikeFilm.findViewById(R.id.imageViewfilm);
        TextView textViewtitoloo;
        textViewtitoloo = myDialogLikeFilm.findViewById(R.id.textViewtitolo);

        textViewtitoloo.setText("Vuoi togliere " + titolo + " dai preferiti?");
        Glide.with(DettaglioFilm.this)
                .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                .into(imageViewCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogLikeFilm.dismiss();
                myDialoInfromazioniFilm.show();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(Uri.parse(String.valueOf(FilmPreferredProvider.FILMS_URI)), FilmPreferredTableHelper.ID_MOVIE + "=" + idFilm, null);
                imgStella.setImageResource(R.drawable.star);
                myDialogLikeFilm.dismiss();
                myDialoInfromazioniFilm.show();
            }
        });
        myDialogLikeFilm.show();
    }



    public void ShowPopupVotaFilm(View v) {
        dialogVotaFilm.setContentView(R.layout.vota_dialog);
        ImageView imageViewCopertinaVoto;
        imageViewCopertinaVoto = dialogVotaFilm.findViewById(R.id.imageViewCopertinaVotaFilm);

        Glide.with(DettaglioFilm.this)
                .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                .into(imageViewCopertinaVoto);
        TextView titoloFilmVoto;
        titoloFilmVoto = dialogVotaFilm.findViewById(R.id.textViewtitolovotaFilm);
        titoloFilmVoto.setText(titolo);

        RatingBar ratingBar;
        ratingBar = dialogVotaFilm.findViewById(R.id.ratingBar);
        final TextView votoPersonale;
        votoPersonale = dialogVotaFilm.findViewById(R.id.textViewvotoPersonale);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                votoPersonale.setText(String.valueOf("Il tuo voto "+(ratingBar.getRating()*2) +" /10"));
            }
        });


        ImageView escVota,votaFilm;
        escVota = dialogVotaFilm.findViewById(R.id.buttonturnback);
        votaFilm = dialogVotaFilm.findViewById(R.id.imgVotaFilm);

        escVota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogVotaFilm.dismiss();
            }
        });

        votaFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DettaglioFilm.this,"votato",Toast.LENGTH_SHORT).show();
            }
        });

        dialogVotaFilm.show();

    }
}

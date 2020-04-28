package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    ImageView imgDettaglio, imgStella;
    Cursor mCursor;
    String idFilm;
    Dialog myDialog;
    String immagineDettaglio;
    String titolo;
    Button btnOk, btnCancel,btnInformzioni;
    String data;
    Dialog myDialogLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imgDettaglio = findViewById(R.id.imageViewDettaglio);
        btnInformzioni = findViewById(R.id.buttonApriDialogInformzioni);
        myDialogLike = new Dialog(DettaglioFilm.this);
        myDialog = new Dialog(this);

        if (getIntent().getExtras() != null) {
            titolo = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            final String descrizione = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            immagineDettaglio = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
            data = getIntent().getExtras().getString(FilmTableHelper.DATA);
            idFilm = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);
            final String voto = (getIntent().getExtras().getString(FilmTableHelper.VOTO));


            Glide.with(DettaglioFilm.this)
                    .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                    .into(imgDettaglio);

            txtTitolo.setText(titolo);


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

/*            imgStella.setOnClickListener(new View.OnClickListener() {
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

 */


            btnInformzioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DettaglioFilm.this, "CIAO", Toast.LENGTH_SHORT).show();
                    myDialogLike.setContentView(R.layout.like_dialog);
                    ImageView imageViewCopertina;
                    imageViewCopertina = myDialogLike.findViewById(R.id.imageViewfilmLike);
                    TextView titoloo;
                    titoloo = myDialogLike.findViewById(R.id.textViewtitoloLike);
                    Button esc;
                    esc = myDialogLike.findViewById(R.id.buttoncancelLike);
                    TextView dataUscita,genereFilm;
                    dataUscita = myDialogLike.findViewById(R.id.textViewDataDiUscita);
                    genereFilm = myDialogLike.findViewById(R.id.textViewgenereFilm);

                    dataUscita.setText(data);

                    int[] colorGreen = {Color.rgb(0, 187, 45), Color.rgb(156, 156, 156)};
                    int[] colorRed = {Color.rgb(255, 0, 0), Color.rgb(156, 156, 156)};
                    int[] colorArancio = {Color.rgb(255, 117, 20), Color.rgb(156, 156, 156)};
                    int[] colorYellow = {Color.rgb(255, 255, 45), Color.rgb(156, 156, 156)};
                    int[] colorBlue = {Color.rgb(3, 107, 218), Color.rgb(156, 156, 156)};




                    esc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialogLike.dismiss();
                            Toast.makeText(DettaglioFilm.this, "esci", Toast.LENGTH_SHORT).show();

                        }
                    });

                    Double valutazione = Double.valueOf(voto)*10;
                    Double conteggio = 100 - valutazione;

                    PieChart pieChart = myDialogLike.findViewById(R.id.pieChart);

                    Float number[] = {Float.valueOf(String.valueOf(valutazione)), Float.valueOf(String.valueOf(conteggio))};

                    List<PieEntry> pieEntries = new ArrayList<>();
                    for (int i = 0; i < number.length; i++) {
                        pieEntries.add(new PieEntry(number[i]));
                    }

                    PieDataSet dataSet = new PieDataSet(pieEntries, "");
                    PieData data = new PieData(dataSet);
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
                    pieChart.getLegend().setEnabled(false);
                    data.setValueTextSize(15f);
                    pieChart.animateX(1500);

                    pieChart.setData(data);
                    pieChart.invalidate();


                    titoloo.setText(titolo);


                    Glide.with(DettaglioFilm.this)
                            .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                            .into(imageViewCopertina);

                    myDialogLike.show();

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
        myDialog.show();
    }
}

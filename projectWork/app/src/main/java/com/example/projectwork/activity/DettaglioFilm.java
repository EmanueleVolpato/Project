package com.example.projectwork.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebServiceVideoFilm;
import com.example.projectwork.services.IWebServiceVoteFilm;
import com.example.projectwork.services.JsonVota;
import com.example.projectwork.services.VideoResults;
import com.example.projectwork.services.VoteFilmResults;
import com.example.projectwork.services.WebService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import java.util.List;

public class DettaglioFilm extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imgDettaglio;
    Cursor mCursor;
    String idFilm;
    String immagineDettaglio;
    String titolo;
    FloatingActionButton btnInformzioni;
    String voto;
    String data;
    String descrizione;
    SharedPref sharedPref;
    String idSessionGuest;
    RatingBar ratingBarVotoFilm;
    String immaginePrincipale;
    ScrollView scrollView;
    private int oldScrollYPostion = 0;
    String keyVideo = null;
    private WebService webService;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";


    ImageView buttonYouTube,imageViewAggiungiAiPreferiti;
    Dialog myDialogLikeFilm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imgDettaglio = findViewById(R.id.imageViewDettaglio);
        btnInformzioni = findViewById(R.id.buttonApriDialogInformzioni);
        ratingBarVotoFilm = findViewById(R.id.ratingBarVotoFilm);
        scrollView = findViewById(R.id.scrollView);
        buttonYouTube = findViewById(R.id.imageViewApriYoutube);
        imageViewAggiungiAiPreferiti = findViewById(R.id.imageViewAggiungiPreferiti);
        myDialogLikeFilm = new Dialog(DettaglioFilm.this);


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY() > oldScrollYPostion) {
                    btnInformzioni.hide();
                } else if (scrollView.getScrollY() < oldScrollYPostion || scrollView.getScrollY() <= 0) {
                    btnInformzioni.show();
                }
                oldScrollYPostion = scrollView.getScrollY();
            }
        });

        if (getIntent().getExtras() != null) {

            titolo = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            descrizione = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);
            data = getIntent().getExtras().getString(FilmTableHelper.DATA);
            idFilm = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);
            immagineDettaglio = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
            voto = (getIntent().getExtras().getString(FilmTableHelper.VOTO));


            if (controlloConnessione()) {
                webService = WebService.getInstance();
                getVideo(idFilm, "it");
            }

            Glide.with(DettaglioFilm.this)
                    .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                    .into(imgDettaglio);


            if (!titolo.equals("")) {
                txtTitolo.setText(titolo);
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DettaglioFilm.this);
                builder1.setMessage("NESSUN TITOLO DISPONIBILE AL MOMENTO.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }


            String[] selectionArgss = {idFilm};
            mCursor = DettaglioFilm.this.getContentResolver().query(
                    FilmPreferredProvider.FILMS_URI,
                    null,
                    FilmPreferredTableHelper.ID_MOVIE + " = ?",
                    selectionArgss,
                    null);

            while (mCursor.moveToNext()) {
                imageViewAggiungiAiPreferiti.setImageResource(R.drawable.star_piena);
            }


            imageViewAggiungiAiPreferiti.setOnClickListener(new View.OnClickListener() {
                @Override
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
                        ShowPopupTogiDaPreferiti(v);
                    } else {
                        imageViewAggiungiAiPreferiti.setImageResource(R.drawable.star_piena);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(FilmPreferredTableHelper.ID_MOVIE, idFilm);
                        contentValues.put(FilmPreferredTableHelper.TITOLO, String.valueOf(titolo));
                        contentValues.put(FilmPreferredTableHelper.DATA, String.valueOf(data));
                        contentValues.put(FilmPreferredTableHelper.DESCRIZIONE, descrizione);
                        contentValues.put(FilmPreferredTableHelper.VOTO, voto);
                        contentValues.put(FilmPreferredTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                        contentValues.put(FilmPreferredTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                        DettaglioFilm.this.getContentResolver().insert(FilmPreferredProvider.FILMS_URI, contentValues);
                    }

                }
            });
        }



            if (!descrizione.equals(""))
                txtDecrizione.setText(descrizione);
            else {

                Toast.makeText(DettaglioFilm.this,"Nessuna descrizione disponibile al momento",Toast.LENGTH_SHORT).show();

            }


            int valore = Math.round(Float.parseFloat(voto)/2);
            ratingBarVotoFilm.setRating((valore));



            btnInformzioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(DettaglioFilm.this, InformazioniAggiuntiveFilm.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(FilmTableHelper.ID_MOVIE, idFilm);
                    bundle.putString(FilmTableHelper.TITOLO,titolo);
                    bundle.putString(FilmTableHelper.VOTO,voto);
                    bundle.putString(FilmTableHelper.DATA, data);
                    bundle.putString(FilmTableHelper.DESCRIZIONE, descrizione);
                    bundle.putString(FilmTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                    bundle.putString(FilmTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        buttonYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DettaglioFilm.this, VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("video", idFilm);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        }

    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void ShowPopupTogiDaPreferiti(View v) {
        myDialogLikeFilm.setContentView(R.layout.dialog);
        Button btnOk, btnCancel;
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
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(Uri.parse(String.valueOf(FilmPreferredProvider.FILMS_URI)), FilmPreferredTableHelper.ID_MOVIE + "=" + idFilm, null);
                imageViewAggiungiAiPreferiti.setImageResource(R.drawable.star);
                myDialogLikeFilm.dismiss();
            }
        });
        myDialogLikeFilm.show();
    }


    private void getVideo(String idFilm, String language) {
        webService.getVideoFilm(idFilm, API_KEY, language, new IWebServiceVideoFilm() {
            @Override
            public void onVideoFetched(boolean success, List<VideoResults.Data> videos, int errorCode, String errorMessage) {
                if (success) {
                    try {
                        if (videos != null)
                            keyVideo = videos.get(0).getKey();
                    } catch (Exception ex) {
                        Toast.makeText(DettaglioFilm.this, "errore link video youtube", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DettaglioFilm.this, "errore link video youtube", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean controlloConnessione() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}

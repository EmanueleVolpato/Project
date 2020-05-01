package com.example.projectwork.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.adapter.RecycleViewAdapter;
import com.example.projectwork.adapter.RecyclerViewAdapterFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.FilmResults;
import com.example.projectwork.services.GenresResults;
import com.example.projectwork.services.IWebServiceGenres;
import com.example.projectwork.services.IWebServiceVideoFilm;
import com.example.projectwork.services.IWebServiceVoteFilm;
import com.example.projectwork.services.JsonVota;
import com.example.projectwork.services.VideoResults;
import com.example.projectwork.services.VoteFilmResults;
import com.example.projectwork.services.WebService;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Video.VideoColumns.LANGUAGE;

public class InformazioniAggiuntiveFilm extends AppCompatActivity {
    ImageView imageView, imageViewAggiungiAiPreferiti;
    TextView titolo, data, genere;
    RatingBar ratingBarVotoPersonale;
    Button buttonVota;
    ImageView buttonYouTube;
    Cursor mCursor;
    String dataFilm, idFilm, immagineDettaglioFilm, voto, immaginePrincipale, descrizioneFilm, titoloFilm;
    String idSessionGuest;
    private WebService webService;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    SharedPref sharedPref;
    Dialog myDialogLikeFilm;
    String keyVideo = null;

    RecyclerView recyclerViewFilmSimili;
    ArrayList<MainModel> models;
    MainAdapter mainAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informazioni_aggiuntive_film);

        imageView = findViewById(R.id.imageViewInformazioniAggiuntive);
        titolo = findViewById(R.id.titoloFilmInformzioni);
        data = findViewById(R.id.DataFilm);
        genere = findViewById(R.id.genereFilmInformazioni);
        ratingBarVotoPersonale = findViewById(R.id.ratingBarVotoPersonaleFilm);
        buttonVota = findViewById(R.id.buttonVotaFilm);
        buttonYouTube = findViewById(R.id.imageViewApriYoutube);
        imageViewAggiungiAiPreferiti = findViewById(R.id.imageViewAggiungiPreferiti);
        myDialogLikeFilm = new Dialog(InformazioniAggiuntiveFilm.this);
        recyclerViewFilmSimili = findViewById(R.id.recyclerViewSimili);



        Integer[]logo = {R.drawable.logo,R.drawable.info,R.drawable.star,R.drawable.star_piena};
        String[] nome = {"logo","info","star","starpiena"};
        models = new ArrayList<>();
        for(int i =0;i<logo.length;i++)
        {
            MainModel model = new MainModel(logo[i],nome[i]);
            models.add(model);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(InformazioniAggiuntiveFilm.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewFilmSimili.setLayoutManager(layoutManager);
        recyclerViewFilmSimili.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(InformazioniAggiuntiveFilm.this,models);
        recyclerViewFilmSimili.setAdapter(mainAdapter);




        if (getIntent().getExtras() != null) {

            String[] selectionArgs = {"key_session"};
            Cursor c = InformazioniAggiuntiveFilm.this.getContentResolver().query(
                    FilmPreferredProvider.FILMS_URI,
                    null,
                    FilmPreferredTableHelper.ID_MOVIE + " = ?",
                    selectionArgs,
                    null);

            while (c.moveToNext()) {
                idSessionGuest = c.getString(c.getColumnIndex(FilmPreferredTableHelper.KEY_GUEST_VOTO));
            }

            if (controlloConnessione()) {
                webService = WebService.getInstance();
            }


            if (getIntent().getExtras() != null) {
                titoloFilm = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
                descrizioneFilm = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
                dataFilm = getIntent().getExtras().getString(FilmTableHelper.DATA);
                idFilm = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);
                immagineDettaglioFilm = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
                voto = (getIntent().getExtras().getString(FilmTableHelper.VOTO));
                immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);

                if (controlloConnessione()) {
                    webService = WebService.getInstance();
                    getVideo(idFilm, "it");
                }

                titolo.setText(titoloFilm);

                Date dateIniziale= null;
                try {
                    dateIniziale = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN).parse(dataFilm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataFilm = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(dateIniziale);

                data.setText(dataFilm);

                Glide.with(InformazioniAggiuntiveFilm.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglioFilm)
                        .into(imageView);


                String[] selectionArgss = {idFilm};
                mCursor = InformazioniAggiuntiveFilm.this.getContentResolver().query(
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

                        mCursor = InformazioniAggiuntiveFilm.this.getContentResolver().query(
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
                            contentValues.put(FilmPreferredTableHelper.TITOLO, String.valueOf(titoloFilm));
                            contentValues.put(FilmPreferredTableHelper.DATA, String.valueOf(dataFilm));
                            contentValues.put(FilmPreferredTableHelper.DESCRIZIONE, descrizioneFilm);
                            contentValues.put(FilmPreferredTableHelper.VOTO, voto);
                            contentValues.put(FilmPreferredTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                            contentValues.put(FilmPreferredTableHelper.IMG_DETTAGLIO, immagineDettaglioFilm);
                            InformazioniAggiuntiveFilm.this.getContentResolver().insert(FilmPreferredProvider.FILMS_URI, contentValues);
                        }

                    }
                });
            }


            ratingBarVotoPersonale.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    buttonVota.setVisibility(View.VISIBLE);
                }
            });

            buttonVota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (controlloConnessione()) {
                        JsonVota j = new JsonVota();
                        votaFilm(idFilm, j.ApiJsonMap(ratingBarVotoPersonale.getRating() * 2));
                    }
                }
            });

            buttonYouTube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InformazioniAggiuntiveFilm.this, VideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("video", idFilm);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        //  listGenres();


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
                        Toast.makeText(InformazioniAggiuntiveFilm.this, "errore link video youtube", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InformazioniAggiuntiveFilm.this, "errore link video youtube", Toast.LENGTH_SHORT).show();
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


    private void votaFilm(String ID, JsonObject j) {
        webService.votaFilm(ID, API_KEY, idSessionGuest, j, new IWebServiceVoteFilm() {
            @Override
            public void onVoteFetched(boolean success, VoteFilmResults voteResult, int errorCode, String errorMessage) {
                if (success) {
                    Toast.makeText(InformazioniAggiuntiveFilm.this,"Votazione avvenuta con successo",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(InformazioniAggiuntiveFilm.this,"Errore",Toast.LENGTH_SHORT).show();

                }
            }
        });
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

        textViewtitoloo.setText("Vuoi togliere " + titoloFilm + " dai preferiti?");
        Glide.with(InformazioniAggiuntiveFilm.this)
                .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglioFilm)
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


    private void listGenres() {
        webService.listGenres(API_KEY, LANGUAGE, new IWebServiceGenres() {
            @Override
            public void onGenresFetched(boolean success, List<GenresResults.Data> genres, int errorCode, String errorMessage) {
                if (success) {
                    for (int i = 0; i < genres.size(); i++) {

                    }

                } else {
                    Toast.makeText(InformazioniAggiuntiveFilm.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

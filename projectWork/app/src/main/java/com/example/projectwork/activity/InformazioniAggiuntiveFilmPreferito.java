package com.example.projectwork.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.adapter.FilmSimiliAdapter;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.FilmResults;
import com.example.projectwork.services.GenresResults;
import com.example.projectwork.services.IWebService;
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

public class InformazioniAggiuntiveFilmPreferito extends AppCompatActivity {

    ImageView imageViewInformazioniPreferiti;
    TextView titoloInformazioniPreferiti, dataInformazioniPreferiti, correlati, genere;
    RatingBar ratingBarVotoPersonaleInformazioniPreferiti;
    Button buttonVotaInformazioniPreferiti;
    String dataFilmPreferito, idFilmPreferito, immagineDettaglioFilmPreferito, votoPreferito, descrizioneFilmPreferito, titoloFilmPreferito;
    String idSessionGuest;
    SharedPref sharedPref;
    private WebService webService;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";

    int PAGE = 1;
    String LANGUAGE = "it";


    RecyclerView recyclerViewFilmSimiliPreferiti;
    List<FilmResults.Data> internetFilmSimili;
    FilmSimiliAdapter adapter;

    int[] generiFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informazioni_aggiuntive_film_preferito);

        imageViewInformazioniPreferiti = findViewById(R.id.imageViewInformazioniAggiuntivePreferiti);
        titoloInformazioniPreferiti = findViewById(R.id.titoloFilmInformzioniPreferiti);
        dataInformazioniPreferiti = findViewById(R.id.DataFilmPreferiti);
        genere = findViewById(R.id.genere);
        ratingBarVotoPersonaleInformazioniPreferiti = findViewById(R.id.ratingBarVotoPersonaleFilmPreferiti);
        buttonVotaInformazioniPreferiti = findViewById(R.id.buttonVotaFilmPreferiti);
        recyclerViewFilmSimiliPreferiti = findViewById(R.id.recyclerViewSimiliPreferiti);
        correlati = findViewById(R.id.txtCorrelati);


        if (getIntent().getExtras() != null) {

            String[] selectionArgs = {"key_session"};
            Cursor c = InformazioniAggiuntiveFilmPreferito.this.getContentResolver().query(
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
                titoloFilmPreferito = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
                descrizioneFilmPreferito = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
                dataFilmPreferito = getIntent().getExtras().getString(FilmTableHelper.DATA);
                idFilmPreferito = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);
                immagineDettaglioFilmPreferito = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
                votoPreferito = (getIntent().getExtras().getString(FilmTableHelper.VOTO));

                generiFilm = (getIntent().getExtras().getIntArray(FilmPreferredTableHelper.GENERI));

                if (controlloConnessione()) {
                    webService = WebService.getInstance();
                    PAGE = 1;
                    LANGUAGE = "it";
                    internetFilmSimili = new ArrayList<>();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(InformazioniAggiuntiveFilmPreferito.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewFilmSimiliPreferiti.setLayoutManager(layoutManager);
                    recyclerViewFilmSimiliPreferiti.setItemAnimator(new DefaultItemAnimator());
                    adapter = new FilmSimiliAdapter(InformazioniAggiuntiveFilmPreferito.this, internetFilmSimili);
                    recyclerViewFilmSimiliPreferiti.setAdapter(adapter);
                    getSimilarFilms();

                    recyclerViewFilmSimiliPreferiti.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (!recyclerView.canScrollVertically(1)) {
                                PAGE++;
                                webService = WebService.getInstance();
                                getSimilarFilms();
                            }
                        }
                    });

                    correlati.setClickable(true);
                    correlati.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerViewFilmSimiliPreferiti.smoothScrollToPosition(0);
                        }
                    });

                    listGenres(generiFilm);
                }

                titoloInformazioniPreferiti.setText(titoloFilmPreferito);

                Date dateIniziale = null;
                try {
                    dateIniziale = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN).parse(dataFilmPreferito);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataFilmPreferito = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(dateIniziale);
                dataInformazioniPreferiti.setText(dataFilmPreferito);


                Glide.with(InformazioniAggiuntiveFilmPreferito.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglioFilmPreferito)
                        .into(imageViewInformazioniPreferiti);
            }
        }

        ratingBarVotoPersonaleInformazioniPreferiti.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                buttonVotaInformazioniPreferiti.setVisibility(View.VISIBLE);
            }
        });

        buttonVotaInformazioniPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlloConnessione()) {
                    JsonVota j = new JsonVota();
                    votaFilm(idFilmPreferito, j.ApiJsonMap(ratingBarVotoPersonaleInformazioniPreferiti.getRating() * 2));
                }
            }
        });


    }

    private void getSimilarFilms() {
        webService.getSimilarFilms(idFilmPreferito, API_KEY, LANGUAGE, PAGE, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
                if (success) {
                    //Toast.makeText(InformazioniAggiuntiveFilm.this, String.valueOf(films.size()), Toast.LENGTH_SHORT).show();
                    internetFilmSimili.addAll(films);
                    adapter.setFilms(internetFilmSimili);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(InformazioniAggiuntiveFilmPreferito.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InformazioniAggiuntiveFilmPreferito.this, "Votazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InformazioniAggiuntiveFilmPreferito.this, "Errore", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void listGenres(final int[] idGeneriFilm) {
        webService.listGenres(API_KEY, LANGUAGE, new IWebServiceGenres() {
            @Override
            public void onGenresFetched(boolean success, List<GenresResults.Data> genres, int errorCode, String errorMessage) {
                if (success) {
                    for (int i = 0; i < idGeneriFilm.length; i++) {
                        for (int j = 0; j < genres.size(); j++) {
                            if (idGeneriFilm[i] == genres.get(j).getId()) {
                                if (i == 0)
                                    genere.setText(genere.getText() + " " + genres.get(i).getName());
                                else
                                    genere.setText(genere.getText() + ", " + genres.get(i).getName());
                            }
                        }
                    }
                } else {
                    Toast.makeText(InformazioniAggiuntiveFilmPreferito.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
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

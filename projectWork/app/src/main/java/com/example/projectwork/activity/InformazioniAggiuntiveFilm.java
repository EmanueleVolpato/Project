package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.projectwork.services.IWebServiceVoteFilm;
import com.example.projectwork.services.JsonVota;
import com.example.projectwork.services.VoteFilmResults;
import com.example.projectwork.services.WebService;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InformazioniAggiuntiveFilm extends AppCompatActivity {

    ImageView imageView;
    TextView titolo, data, genere, correlati;
    RatingBar ratingBarVotoPersonale;
    Button buttonVota;
    String dataFilm, idFilm, immagineDettaglioFilm, voto, immaginePrincipale, descrizioneFilm, titoloFilm;
    String idSessionGuest;
    private WebService webService;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    SharedPref sharedPref;
    int PAGE;
    String LANGUAGE;
    RecyclerView recyclerViewFilmSimili;
    List<FilmResults.Data> internetFilmSimili;
    FilmSimiliAdapter adapter;
    int[] generiFilm;

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
        recyclerViewFilmSimili = findViewById(R.id.recyclerViewSimili);
        correlati = findViewById(R.id.txtCorrelati);

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

            if (getIntent().getExtras() != null) {
                titoloFilm = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
                descrizioneFilm = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
                dataFilm = getIntent().getExtras().getString(FilmTableHelper.DATA);
                idFilm = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);
                immagineDettaglioFilm = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);
                voto = (getIntent().getExtras().getString(FilmTableHelper.VOTO));
                immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);

                generiFilm = (getIntent().getExtras().getIntArray("generiID"));

                if (controlloConnessione()) {
                    PAGE = 1;
                    LANGUAGE = "it";
                    webService = WebService.getInstance();
                    internetFilmSimili = new ArrayList<>();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(InformazioniAggiuntiveFilm.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewFilmSimili.setLayoutManager(layoutManager);
                    recyclerViewFilmSimili.setItemAnimator(new DefaultItemAnimator());
                    adapter = new FilmSimiliAdapter(InformazioniAggiuntiveFilm.this, internetFilmSimili);
                    recyclerViewFilmSimili.setAdapter(adapter);
                    getSimilarFilms();

                    recyclerViewFilmSimili.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            recyclerViewFilmSimili.smoothScrollToPosition(0);
                        }
                    });
                    listGenres(generiFilm);
                }

                titolo.setText(titoloFilm);

                Date dateIniziale = null;
                try {
                    dateIniziale = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN).parse(dataFilm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataFilm = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(dateIniziale);

                data.setText(dataFilm);


                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    Glide.with(InformazioniAggiuntiveFilm.this)
                            .load("https://image.tmdb.org/t/p/w500/" + immaginePrincipale)
                            .into(imageView);
                }else
                {
                    Glide.with(InformazioniAggiuntiveFilm.this)
                            .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglioFilm)
                            .into(imageView);
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
                        else
                        {
                            Toast.makeText(InformazioniAggiuntiveFilm.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void getSimilarFilms() {
        webService.getSimilarFilms(idFilm, API_KEY, LANGUAGE, PAGE, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
                if (success) {
                    internetFilmSimili.addAll(films);
                    adapter.setFilms(internetFilmSimili);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(InformazioniAggiuntiveFilm.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InformazioniAggiuntiveFilm.this, "Votazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InformazioniAggiuntiveFilm.this, "Errore", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(InformazioniAggiuntiveFilm.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
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

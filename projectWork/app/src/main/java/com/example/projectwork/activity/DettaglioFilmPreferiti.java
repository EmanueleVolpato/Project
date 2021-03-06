package com.example.projectwork.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebServiceVideoFilm;
import com.example.projectwork.services.VideoResults;
import com.example.projectwork.services.WebService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DettaglioFilmPreferiti extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio, imageYoutube;
    String idFilm, titolo, data, immagineDettaglio, descrizione, voto, immaginePrincipale;
    String keyVideo = null;
    FloatingActionButton btnInformzioniFilmPreferiti;
    Dialog dialogVotaFilmPreferiti, dialogInformzioniPreferiti;
    SharedPref sharedPref;
    RatingBar ratingBarpreferiti;
    private int oldScrollYPostion = 0;
    ScrollView scrollViewPreferiti;
    private WebService webService;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    int[] generiFilm;

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
        immaginePrincipale = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);
        dialogInformzioniPreferiti = new Dialog(DettaglioFilmPreferiti.this);
        dialogVotaFilmPreferiti = new Dialog(DettaglioFilmPreferiti.this);
        scrollViewPreferiti = findViewById(R.id.scrollViewPreferiti);
        imageYoutube = findViewById(R.id.imageViewApriYoutubePreferiti);

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
            immaginePrincipale = (getIntent().getExtras().getString(FilmPreferredTableHelper.IMG_PRINCIPALE));
            generiFilm = (getIntent().getExtras().getIntArray(FilmPreferredTableHelper.GENERI));

            int valore = Math.round(Float.parseFloat(voto) / 2);
            ratingBarpreferiti.setRating((valore));

            if (controlloConnessione()) {
                webService = WebService.getInstance();
                getVideo(idFilm, "it");
            }

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Glide.with(DettaglioFilmPreferiti.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immaginePrincipale)
                        .placeholder(R.drawable.loading)
                        .into(imageViewDettaglio);
            } else {
                Glide.with(DettaglioFilmPreferiti.this)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                        .placeholder(R.drawable.loading)
                        .into(imageViewDettaglio);
            }

            txtTitolo.setText(titolo);
            if (!descrizione.equals(""))
                txtDecrizione.setText(descrizione);
            else {

                Toast.makeText(DettaglioFilmPreferiti.this, "nessuna descrizione disponibile al momento", Toast.LENGTH_SHORT).show();

            }
        }

        btnInformzioniFilmPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DettaglioFilmPreferiti.this, InformazioniAggiuntiveFilmPreferito.class);
                Bundle bundle = new Bundle();
                bundle.putString(FilmPreferredTableHelper.ID_MOVIE, idFilm);
                bundle.putString(FilmPreferredTableHelper.TITOLO, titolo);
                bundle.putString(FilmPreferredTableHelper.DATA, data);
                bundle.putString(FilmPreferredTableHelper.DESCRIZIONE, descrizione);
                bundle.putString(FilmPreferredTableHelper.IMG_DETTAGLIO, immagineDettaglio);
                bundle.putString(FilmPreferredTableHelper.IMG_PRINCIPALE, immaginePrincipale);
                bundle.putIntArray(FilmPreferredTableHelper.GENERI, generiFilm);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        imageYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DettaglioFilmPreferiti.this, VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("video", idFilm);
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

    private void getVideo(String idFilm, String language) {
        webService.getVideoFilm(idFilm, API_KEY, language, new IWebServiceVideoFilm() {
            @Override
            public void onVideoFetched(boolean success, List<VideoResults.Data> videos, int errorCode, String errorMessage) {
                if (success) {
                    try {
                        if (videos != null)
                            keyVideo = videos.get(0).getKey();
                    } catch (Exception ex) {
                    }
                } else {
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





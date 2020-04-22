package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.adapter.RecycleViewAdapter;
import com.example.projectwork.localDatabase.FilmDB;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebService;
import com.example.projectwork.services.MovieResults;
import com.example.projectwork.services.WebService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IWebService {

    public static final String categoriaSelezionata = "ID";

    private String CATEGORY = "popular";
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    private String LANGUAGE = "it";
    private int PAGE = 1;

    private WebService webService;

    List<MovieResults.ResultsBean> cachedMovies;

    RecyclerView recyclerView;
    RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MOVIES");

        recyclerView = findViewById(R.id.recyclerviewFilm);

        if (controlloConnessione()) {
            webService = WebService.getInstance();
            internet();
        } else {
            noInternet();
        }
    }

    private boolean controlloConnessione() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void internet() {
        webService.getMovies(CATEGORY, API_KEY, LANGUAGE, PAGE, MainActivity.this, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {
                if (success) {
                    adapter = new RecycleViewAdapter(MainActivity.this, movies);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Qualcosa è andato storto " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void noInternet() {
        cachedMovies = new ArrayList<>();
        Cursor movies = MainActivity.this.getContentResolver().query(FilmProvider.FILMS_URI, null, null, null, null);

        if (movies != null) {
            while (movies.moveToNext()) {
                MovieResults.ResultsBean movie = new MovieResults.ResultsBean();

                movie.setId(movies.getColumnIndex(FilmTableHelper.ID_MOVIE));
                movie.setTitle(movies.getString(movies.getColumnIndex(FilmTableHelper.TITOLO)));
                movie.setOverview(movies.getString(movies.getColumnIndex(FilmTableHelper.DESCRIZIONE)));
                movie.setPosterPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMG_PRINCIPALE)));
                movie.setBackdropPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMG_DETTAGLIO)));

                cachedMovies.add(movie);
            }

            adapter = new RecycleViewAdapter(MainActivity.this, cachedMovies);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (controlloConnessione()) {
            webService = WebService.getInstance();
            internet();
        } else {
            noInternet();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucommons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.listaPreferiti) {
            startActivity(new Intent(this, FilmPreferiti.class));

        } else if (id == R.id.listaUltimiFilmUscitiAlCinema) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "ultimifilmuscitialcinema");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);

        } else if (id == R.id.listaPopolari) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmpopolari");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        } else if (id == R.id.listaFilmPiuVotati) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmvotati");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        } else if (id == R.id.filmInUscita) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "prossimifilm");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {

    }
}

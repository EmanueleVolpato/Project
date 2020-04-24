package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
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

    private String CATEGORY = "";
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    private String LANGUAGE = "it";
    private int PAGE = 1;
    private WebService webService;
    List<MovieResults.ResultsBean> cachedMovies;
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;


    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    String[] categorie ={"Novità","Prossime Uscite","Più votati","Popolari"};
    String categoriaSelect="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MOVIES");

        recyclerView = findViewById(R.id.recyclerviewFilm);

        if (controlloConnessione()) {
            CATEGORY = "popular";
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

                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        adapter = new RecycleViewAdapter(MainActivity.this, movies);
                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        recyclerView.setAdapter(adapter);                    }
                    else {
                        adapter = new RecycleViewAdapter(MainActivity.this, movies);
                        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        recyclerView.setAdapter(adapter);                    }


                } else {
                    Toast.makeText(MainActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
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
                movie.setReleaseDate(movies.getString(movies.getColumnIndex(FilmTableHelper.DATA)));
                movie.setOverview(movies.getString(movies.getColumnIndex(FilmTableHelper.DESCRIZIONE)));
                movie.setPosterPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMG_PRINCIPALE)));
                movie.setBackdropPath(movies.getString(movies.getColumnIndex(FilmTableHelper.IMG_DETTAGLIO)));

                cachedMovies.add(movie);
            }

            int orientation = getResources().getConfiguration().orientation;


            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                adapter = new RecycleViewAdapter(MainActivity.this, cachedMovies);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                adapter = new RecycleViewAdapter(MainActivity.this, cachedMovies);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (controlloConnessione()) {
            webService = WebService.getInstance();
            internet();
        } else {
            noInternet();
        }
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucommons, menu);
        MenuItem search = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView)search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.listaPreferiti) {
            startActivity(new Intent(this, FilmPreferiti.class));

        } else if (id == R.id.idCategorie) {

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Seleziona una categoria");

            builder.setSingleChoiceItems(categorie, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    categoriaSelect = categorie[i];
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(categoriaSelect=="Popolari")
                    {
                        CATEGORY = "popular";
                        webService = WebService.getInstance();
                        internet();
                    }else if(categoriaSelect=="Più votati")
                    {
                        CATEGORY = "top_rated";
                        webService = WebService.getInstance();
                        internet();
                    }else if(categoriaSelect=="Prossime Uscite")
                    {
                        CATEGORY = "upcoming";
                        webService = WebService.getInstance();
                        internet();
                    }else if(categoriaSelect=="Novità")
                    {
                        CATEGORY = "now_playing";
                        webService = WebService.getInstance();
                        internet();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog = builder.create();
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {

    }
}

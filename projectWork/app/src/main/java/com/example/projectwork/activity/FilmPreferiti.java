package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.adapter.RecyclerViewAdapterFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferitiProvider;
import com.example.projectwork.localDatabase.FilmPreferitiTableHelper;
import com.example.projectwork.services.MovieResults;

import java.util.ArrayList;
import java.util.List;

public class FilmPreferiti extends AppCompatActivity {

    List<MovieResults.ResultsBean> preferredMovie;
    RecyclerView recyclerView;
    RecyclerViewAdapterFilmPreferiti adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_preferiti);
        getSupportActionBar().setTitle("PREFERRED MOVIES");

        recyclerView = findViewById(R.id.recyclerViewFilmPreferiti);

        caricaPreferiti();
    }

    public void caricaPreferiti() {
        preferredMovie = new ArrayList<>();
        Cursor movies = FilmPreferiti.this.getContentResolver().query(
                FilmPreferitiProvider.FILMS_URI,
                null,
                null,
                null,
                null);

        if (movies != null) {
            while (movies.moveToNext()) {
                MovieResults.ResultsBean movie = new MovieResults.ResultsBean();

                Toast.makeText(FilmPreferiti.this, movies.getColumnIndex(FilmPreferitiTableHelper.ID_MOVIE) + " "
                        + movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.TITOLO)) , Toast.LENGTH_SHORT).show();


                String id = movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.ID_MOVIE));
                movie.setId(Integer.parseInt(id));
                movie.setTitle(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.TITOLO)));
                movie.setOverview(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.DESCRIZIONE)));
                movie.setPosterPath(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.IMG_PRINCIPALE)));
                movie.setBackdropPath(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.IMG_DETTAGLIO)));

                preferredMovie.add(movie);
            }
            adapter = new RecyclerViewAdapterFilmPreferiti(FilmPreferiti.this, preferredMovie);
            recyclerView.setLayoutManager(new GridLayoutManager(FilmPreferiti.this, 2));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        MenuItem search = menu.findItem(R.id.app_bar_search_preferred);
        SearchView searchView = (SearchView) search.getActionView();

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
}

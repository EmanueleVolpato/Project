package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.projectwork.R;
import com.example.projectwork.adapter.RecyclerViewAdapterFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.services.FilmResults;

import java.util.ArrayList;
import java.util.List;

public class FilmPreferiti extends AppCompatActivity {

    List<FilmResults.Data> preferredFilm;
    RecyclerView recyclerView;
    RecyclerViewAdapterFilmPreferiti adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_preferiti);
        getSupportActionBar().setTitle("PREFERRED MOVIES");

        recyclerView = findViewById(R.id.recyclerViewFilmPreferiti);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(FilmPreferiti.this, 4));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(FilmPreferiti.this, 2));

        caricaPreferiti();
    }

    public void caricaPreferiti() {
        preferredFilm = new ArrayList<>();
        Cursor movies = FilmPreferiti.this.getContentResolver().query(
                FilmPreferredProvider.FILMS_URI,
                null,
                null,
                null,
                null);

        if (movies != null) {
            while (movies.moveToNext()) {
                FilmResults.Data movie = new FilmResults.Data();
                String id = movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.ID_MOVIE));
                movie.setId(Integer.parseInt(id));
                movie.setTitle(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.TITOLO)));
                movie.setOverview(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.DESCRIZIONE)));
                movie.setPosterPath(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.IMG_PRINCIPALE)));
                movie.setReleaseDate(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.DATA)));
                movie.setVoteAverage(movies.getInt(movies.getColumnIndex(FilmPreferredTableHelper.VOTO)));
                movie.setBackdropPath(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.IMG_DETTAGLIO)));

                preferredFilm.add(movie);
            }
            adapter = new RecyclerViewAdapterFilmPreferiti(FilmPreferiti.this, preferredFilm);
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

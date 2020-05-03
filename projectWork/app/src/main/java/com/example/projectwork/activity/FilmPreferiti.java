package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.adapter.RecyclerViewAdapterFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.services.FilmResults;
import com.example.projectwork.services.WebService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FilmPreferiti extends AppCompatActivity {

    List<FilmResults.Data> preferredFilm;
    RecyclerView recyclerView;
    RecyclerViewAdapterFilmPreferiti adapter;
    FloatingActionButton btnGoOnTop;

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_preferiti);
        getSupportActionBar().setTitle("PREFERRED MOVIES");

        recyclerView = findViewById(R.id.recyclerViewFilmPreferiti);
        btnGoOnTop = findViewById(R.id.buttonGoOnTopPreferiti);

        preferredFilm = new ArrayList<>();
        adapter = new RecyclerViewAdapterFilmPreferiti(FilmPreferiti.this, preferredFilm);
        recyclerView.setAdapter(adapter);

        btnGoOnTop.hide();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(new GridLayoutManager(FilmPreferiti.this, 4));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(FilmPreferiti.this, 2));

        caricaPreferiti();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1))
                {
                    btnGoOnTop.hide();
                }

                if(recyclerView.computeVerticalScrollOffset() > 1000)
                    btnGoOnTop.show();
            }
        });


        btnGoOnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
                btnGoOnTop.hide();
            }
        });

    }

    public void caricaPreferiti() {
        Cursor movies = FilmPreferiti.this.getContentResolver().query(
                FilmPreferredProvider.FILMS_URI,
                null,
                null,
                null,
                null);

        if (movies != null) {
            while (movies.moveToNext()) {
                String id = movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.ID_MOVIE));
                if (!id.equals("key_session")) {
                    FilmResults.Data movie = new FilmResults.Data();
                    movie.setId(Integer.parseInt(id));
                    movie.setTitle(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.TITOLO)));
                    movie.setOverview(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.DESCRIZIONE)));
                    movie.setPosterPath(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.IMG_PRINCIPALE)));
                    movie.setReleaseDate(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.DATA)));
                    movie.setVoteAverage(movies.getInt(movies.getColumnIndex(FilmPreferredTableHelper.VOTO)));
                    movie.setBackdropPath(movies.getString(movies.getColumnIndex(FilmPreferredTableHelper.IMG_DETTAGLIO)));

                    preferredFilm.add(movie);
                }
            }
            adapter.setFilms(preferredFilm);
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.resetFilms();
        caricaPreferiti();
    }
}

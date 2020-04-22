package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.projectwork.R;
import com.example.projectwork.adapter.RecycleViewAdapter;
import com.example.projectwork.localDatabase.FilmPreferitiProvider;
import com.example.projectwork.localDatabase.FilmPreferitiTableHelper;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.MovieResults;

import java.util.ArrayList;
import java.util.List;

public class FilmPreferiti extends AppCompatActivity {

    List<MovieResults.ResultsBean> preferredMovie;
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_preferiti);
        getSupportActionBar().setTitle("PREFERRED MOVIES");
        recyclerView = findViewById(R.id.recyclerViewFilmPreferiti);
        caricaPreferiti();
    }



    private void caricaPreferiti() {
        preferredMovie = new ArrayList<>();
        Cursor movies = FilmPreferiti.this.getContentResolver().query(FilmPreferitiProvider.FILMS_URI, null, null, null, null);

        if (movies != null) {
            while (movies.moveToNext()) {
                MovieResults.ResultsBean movie = new MovieResults.ResultsBean();

                movie.setId(movies.getColumnIndex(FilmPreferitiTableHelper.ID_MOVIE));
                movie.setTitle(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.TITOLO)));
                movie.setOverview(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.DESCRIZIONE)));
                movie.setPosterPath(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.IMG_PRINCIPALE)));
                movie.setBackdropPath(movies.getString(movies.getColumnIndex(FilmPreferitiTableHelper.IMG_DETTAGLIO)));

                preferredMovie.add(movie);
            }

            adapter = new RecycleViewAdapter(FilmPreferiti.this, preferredMovie);
            recyclerView.setLayoutManager(new GridLayoutManager(FilmPreferiti.this, 2));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}

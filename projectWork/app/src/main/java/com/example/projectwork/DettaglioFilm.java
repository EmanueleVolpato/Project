package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;

public class DettaglioFilm extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    ListView listView;
    DettaglioFilmAdapter mAdapter;
    public static final int MY_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("DETTAGLIO FILM");

        listView = findViewById(R.id.listFilm);
        mAdapter = new DettaglioFilmAdapter(this, null);
        listView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, FilmProvider.FILMS_URI, null,null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }
}

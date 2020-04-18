package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projectwork.adapter.FilmPreferitiAdapter;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmProvider;

public class ListaFilmPreferiti extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView listViewFilmPreferiti;
    FilmPreferitiAdapter mAdapter;
    public static final int MY_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_film_preferiti);
        getSupportActionBar().setTitle("LISTA FILM PREFERITI");

        listViewFilmPreferiti = findViewById(R.id.listFilmPreferiti);
        mAdapter = new FilmPreferitiAdapter(this, null);
        listViewFilmPreferiti.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this);

        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucommons,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.listaPreferiti)
        {
            startActivity(new Intent(this, ListaFilmPreferiti.class));

        }else
        if(id == R.id.listaUltimiFilmUscitiAlCinema)
        {
            startActivity(new Intent(this, UltimiFilmUscitiAlCinema.class));

        }else
        if(id == R.id.listaPopolari)
        {
            startActivity(new Intent(this, FilmPopolari.class));

        }else
        if(id == R.id.listaFilmPiuVotati)
        {
            startActivity(new Intent(this, FilmPiuVotati.class));

        }else
        if(id == R.id.filmInUscita)
        {
            startActivity(new Intent(this, ProssimeUscite.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, FilmProvider.FILMS_URI, null, null, null, null);

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

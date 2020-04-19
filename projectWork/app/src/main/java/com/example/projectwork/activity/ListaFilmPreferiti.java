package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.projectwork.R;
import com.example.projectwork.adapter.FilmPreferitiAdapter;
import com.example.projectwork.localDatabase.FilmProvider;

public class ListaFilmPreferiti extends AppCompatActivity{

    ListView listViewFilmPreferiti;
    FilmPreferitiAdapter mAdapter;
    public static final int MY_LOADER_ID = 0;
    public static final String categoriaSelezionata = "ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_film_preferiti);
        getSupportActionBar().setTitle("LISTA FILM PREFERITI");

        listViewFilmPreferiti = findViewById(R.id.listFilmPreferiti);
       // mAdapter = new FilmPreferitiAdapter(this, null);
       // listViewFilmPreferiti.setAdapter(mAdapter);
        //getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this);

        
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
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(ListaFilmPreferiti.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "ultimifilmuscitialcinema");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);

        }else
        if(id == R.id.listaPopolari)
        {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(ListaFilmPreferiti.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmpopolari");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }else
        if(id == R.id.listaFilmPiuVotati)
        {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(ListaFilmPreferiti.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmvotati");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }else
        if(id == R.id.filmInUscita)
        {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(ListaFilmPreferiti.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "prossimifilm");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}

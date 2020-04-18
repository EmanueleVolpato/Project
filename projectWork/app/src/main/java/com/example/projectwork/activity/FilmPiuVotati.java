package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.projectwork.R;

public class FilmPiuVotati extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_piu_votati);
        getSupportActionBar().setTitle("FILM PIU' VOTATI");
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
}

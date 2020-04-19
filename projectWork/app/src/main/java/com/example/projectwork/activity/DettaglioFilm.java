package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmDB;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;

public class DettaglioFilm extends AppCompatActivity {

     TextView titolo,descrizione,categoria;
     ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        titolo = findViewById(R.id.titoloFilmDettaglio);
        descrizione = findViewById(R.id.descrizioneFilmDettaglio);
        categoria = findViewById(R.id.categoriaFilmDettaglio);
        imageView = findViewById(R.id.imageViewDettaglio);

        Intent intent = getIntent();
        String Titolo = intent.getExtras().getString("titoloFilm");
        String Descrizione = intent.getExtras().getString("descrizione");
        String Categoria = intent.getExtras().getString("categoria");
        int image = intent.getExtras().getInt("thumbnail");


        titolo.setText(Titolo);
        descrizione.setText(Descrizione);
        categoria.setText(Categoria);
        imageView.setImageResource(image);
    }
}

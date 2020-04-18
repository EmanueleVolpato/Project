package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.projectwork.R;

public class ActivityCategoria extends AppCompatActivity {

    TextView textViewCategoria;
    String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textViewCategoria = findViewById(R.id.textViewCategoria);


        if (getIntent().getExtras() != null) {
            categoria = getIntent().getExtras().getString(MainActivity.categoriaSelezionata);
        }

        textViewCategoria.setText(categoria);
    }


}

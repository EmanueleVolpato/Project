package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projectwork.ListaFilmAdapter;
import com.example.projectwork.ListaFilmPreferiti;
import com.example.projectwork.R;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ListaFilmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("LISTA FILM");
        listView = findViewById(R.id.list);


        mAdapter = new ListaFilmAdapter(this, null);
        listView.setAdapter(mAdapter);
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
            Toast.makeText(this,"listaPreferiti",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ListaFilmPreferiti.class));

        }

        return super.onOptionsItemSelected(item);
    }
}

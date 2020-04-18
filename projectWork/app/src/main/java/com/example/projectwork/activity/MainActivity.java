package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projectwork.adapter.ListaFilmAdapter;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebService;
import com.example.projectwork.services.MovieResults;
import com.example.projectwork.services.WebService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, IWebService {

    ListView listView;
    ListaFilmAdapter mAdapter;
    public static final int MY_LOADER_ID = 0;
    public static final String categoriaSelezionata = "ID";


    public static String CATEGORY = "popular";
    public static String LANGUAGE = "it";
    public static int PAGE = 1;

    private WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MOVIES");
        listView = findViewById(R.id.list);

        webService = WebService.getInstance(CATEGORY, LANGUAGE, PAGE);

        mAdapter = new ListaFilmAdapter(this, null);
        listView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this);



        loadMovies();
    }

    private void loadMovies() {
        webService.getFilms(new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {
                if (success) {
                    //fims = lista film
                    MovieResults.ResultsBean firstMovie = movies.get(0);
                    ContentValues vValue = new ContentValues();
                    vValue.put(FilmTableHelper.TITOLO, firstMovie.getTitle());
                    getContentResolver().insert(FilmProvider.FILMS_URI,vValue);
                } else {
                    Toast.makeText(MainActivity.this, "Qualcosa è andato storto " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
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
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "ultimifilmuscitialcinema");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);

        }else
        if(id == R.id.listaPopolari)
        {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmpopolari");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }else
        if(id == R.id.listaFilmPiuVotati)
        {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmvotati");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }else
        if(id == R.id.filmInUscita)
        {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "prossimifilm");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
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

    @Override
    public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {
        //films
    }
}

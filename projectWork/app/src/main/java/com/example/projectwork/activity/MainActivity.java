package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.adapter.RecycleViewAdapterInternet;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebService;
import com.example.projectwork.services.MovieResults;
import com.example.projectwork.services.WebService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IWebService {

    public static final String categoriaSelezionata = "ID";

    private String CATEGORY = "popular";
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    private String LANGUAGE = "it";
    private int PAGE = 1;

    private WebService webService;
    List<Film> listaMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MOVIES");

        //filmList.add(new Film("Dolittle","Avventura","Lorem Ipsum è un testo segnaposto utilizzato nel settore della tipografia e della stampa. Lorem Ipsum è considerato il testo segnaposto standard sin dal sedicesimo secolo, quando un anonimo tipografo prese una cassetta di caratteri e li assemblò per preparare un testo campione. È sopravvissuto non solo a più di cinque secoli, ma anche al passaggio alla videoimpaginazione, pervenendoci sostanzialmente inalterato. Fu reso popolare, negli anni ’60, con la diffusione dei fogli di caratteri trasferibili “Letraset”, che contenevano passaggi del Lorem Ipsum, e più recentemente da software di impaginazione come Aldus PageMaker, che includeva versioni del Lorem Ipsum.",R.drawable.dolittle,0));

        if (controlloConnessione()) {
            webService = WebService.getInstance();
            internet();
        } else {
            //se non c'è internet
        }
    }

    private boolean controlloConnessione() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void internet(){
        webService.getMovies(CATEGORY, API_KEY, LANGUAGE, PAGE, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {
                if (success) {
                    listaMovie = new ArrayList<>();
                    for (int i = 0; i < movies.size(); i++) {
                        MovieResults.ResultsBean movie = movies.get(i);
                        Film f = new Film();
                        f.setID(movie.getId());
                        f.setTitolo(movie.getTitle());
                        f.setImg(movie.getPosterPath());
                        f.setDescrizione(movie.getOverview());
                        listaMovie.add(f);
                    }

                    RecyclerView recyclerView = findViewById(R.id.recyclerviewFilm);
                    RecycleViewAdapterInternet adapter = new RecycleViewAdapterInternet(MainActivity.this, listaMovie);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(MainActivity.this, "Qualcosa è andato storto " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        internet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucommons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.listaPreferiti) {
            //startActivity(new Intent(this, ListaFilmPreferiti.class));

        } else if (id == R.id.listaUltimiFilmUscitiAlCinema) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "ultimifilmuscitialcinema");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);

        } else if (id == R.id.listaPopolari) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmpopolari");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        } else if (id == R.id.listaFilmPiuVotati) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "filmvotati");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        } else if (id == R.id.filmInUscita) {
            Bundle vBundle = new Bundle();
            Intent vIntent = new Intent(MainActivity.this, ActivityCategoria.class);
            vBundle.putString(categoriaSelezionata, "prossimifilm");
            vIntent.putExtras(vBundle);
            startActivity(vIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage) {

    }
}

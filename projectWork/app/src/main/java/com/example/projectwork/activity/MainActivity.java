package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.adapter.SalvataggioRecycleViewAdapter;
import com.example.projectwork.adapter.RecycleViewAdapter;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebService;
import com.example.projectwork.services.FilmResults;
import com.example.projectwork.services.WebService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String CATEGORY = "popular", API_KEY = "e6de0d8da508a9809d74351ed62affef", LANGUAGE = "it";
    int PAGE = 1;
    WebService webService;
    FloatingActionButton btnGoOnTop;
    List<FilmResults.Data> noInternetFilm, internetFilm, searchInternetFilm;
    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    SwipeRefreshLayout swipeRefreshLayout;
    String[] categorie = {"Novità", "Prossime Uscite", "Più votati", "Popolari"}, tema = {"Chiaro", "Scuro"};
    String categoriaSelect = "", temaSelect = "";
    SharedPref sharedPref;
    boolean searchAttivo = false, girato = false, inizializzato = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MOVIES");

        recyclerView = findViewById(R.id.recyclerviewFilm);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        btnGoOnTop = findViewById(R.id.buttonGoOnTop);
        btnGoOnTop.hide();

        if (savedInstanceState != null) {
            CATEGORY = savedInstanceState.getString("categoria");
            PAGE = savedInstanceState.getInt("page");
            girato = savedInstanceState.getBoolean("girato");
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (controlloConnessione()) {
                    if (inizializzato) {
                        internetFilm.clear();
                        adapter.resetFilms();
                        adapter.notifyDataSetChanged();
                        SalvataggioRecycleViewAdapter.getInstance().getmAdapter().setContext(MainActivity.this);
                        recyclerView.setAdapter(SalvataggioRecycleViewAdapter.getInstance().getmAdapter());
                        adapter = SalvataggioRecycleViewAdapter.getInstance().getmAdapter();
                        webService = WebService.getInstance();
                        internetFilm = SalvataggioRecycleViewAdapter.getInstance().getListFilms();
                        PAGE = 1;
                        internet();
                    } else {
                        PAGE = 1;
                        internetFilm = new ArrayList<>();
                        adapter = new RecycleViewAdapter(MainActivity.this, internetFilm);
                        recyclerView.setAdapter(adapter);
                        webService = WebService.getInstance();
                        internet();
                        SalvataggioRecycleViewAdapter.getInstance().setmAdapter(adapter);
                        SalvataggioRecycleViewAdapter.getInstance().setListFilms(internetFilm);
                    }
                } else {
                    noInternet();
                }
                Toast.makeText(MainActivity.this, "aggiornato", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1)) {
                    btnGoOnTop.hide();
                }

                if (recyclerView.computeVerticalScrollOffset() > 1000)
                    btnGoOnTop.show();

                if (!searchAttivo) {
                    if (controlloConnessione()) {
                        if (!recyclerView.canScrollVertically(1)) {
                            PAGE++;
                            internet();
                        }
                    }
                }
            }
        });

        btnGoOnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
                btnGoOnTop.hide();
            }
        });

        inizia();
    }

    private void inizia() {
        if (controlloConnessione()) {
            if (girato) {
                SalvataggioRecycleViewAdapter.getInstance().getmAdapter().setContext(MainActivity.this);
                recyclerView.setAdapter(SalvataggioRecycleViewAdapter.getInstance().getmAdapter());

                adapter = SalvataggioRecycleViewAdapter.getInstance().getmAdapter();
                webService = WebService.getInstance();
                internetFilm = SalvataggioRecycleViewAdapter.getInstance().getListFilms();

                girato = false;
                return;
            }
            internetFilm = new ArrayList<>();
            adapter = new RecycleViewAdapter(MainActivity.this, internetFilm);
            recyclerView.setAdapter(adapter);
            webService = WebService.getInstance();
            internet();
            SalvataggioRecycleViewAdapter.getInstance().setmAdapter(adapter);
            SalvataggioRecycleViewAdapter.getInstance().setListFilms(internetFilm);
            inizializzato = true;
        } else {
            noInternet();
        }
    }

    private boolean controlloConnessione() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void internet() {
        webService.getFilms(CATEGORY, API_KEY, LANGUAGE, PAGE, MainActivity.this, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
                if (success) {
                    internetFilm.addAll(films);
                    adapter.setFilms(internetFilm);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "connessione internet assente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void noInternet() {
        Cursor cFilms = MainActivity.this.getContentResolver().query(FilmProvider.FILMS_URI, null, null, null, null);
        noInternetFilm = new ArrayList<>();
        if (cFilms != null) {
            while (cFilms.moveToNext()) {
                FilmResults.Data film = new FilmResults.Data();
                String id = cFilms.getString(cFilms.getColumnIndex(FilmPreferredTableHelper.ID_MOVIE));
                film.setId(Integer.parseInt(id));
                film.setTitle(cFilms.getString(cFilms.getColumnIndex(FilmTableHelper.TITOLO)));
                film.setVoteAverage(cFilms.getInt(cFilms.getColumnIndex(FilmTableHelper.VOTO)));
                film.setReleaseDate(cFilms.getString(cFilms.getColumnIndex(FilmTableHelper.DATA)));
                film.setOverview(cFilms.getString(cFilms.getColumnIndex(FilmTableHelper.DESCRIZIONE)));
                film.setPosterPath(cFilms.getString(cFilms.getColumnIndex(FilmTableHelper.IMG_PRINCIPALE)));
                film.setBackdropPath(cFilms.getString(cFilms.getColumnIndex(FilmTableHelper.IMG_DETTAGLIO)));
                noInternetFilm.add(film);
            }
            RecycleViewAdapter adapterNoInternet = new RecycleViewAdapter(MainActivity.this, noInternetFilm);
            recyclerView.setAdapter(adapterNoInternet);
            adapterNoInternet.setFilms(noInternetFilm);
            adapterNoInternet.notifyDataSetChanged();
        }
    }

    private void searchFilms(String QUERY) {
        webService.searchFilms(QUERY, API_KEY, LANGUAGE, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
                if (success) {
                    adapter.resetFilms();
                    searchInternetFilm = new ArrayList<>();
                    searchInternetFilm.addAll(films);
                    adapter.setFilms(searchInternetFilm);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "connessione internet assente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucommons, menu);
        MenuItem search = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAttivo = true;
                if (controlloConnessione()) {
                    if (!newText.isEmpty()) {
                        searchFilms(newText);
                    } else {
                        PAGE = 1;
                        adapter.resetFilms();
                        internet();
                        searchAttivo = false;
                    }
                } else {
                    if (!newText.isEmpty()) {
                        adapter.getFilter().filter(newText);
                    } else {
                        adapter.resetSearchFilm();
                        noInternet();
                        adapter.notifyDataSetChanged();
                        searchAttivo = false;
                    }
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.listaPreferiti) {
            startActivity(new Intent(MainActivity.this, FilmPreferiti.class));
        } else if (id == R.id.temaApp) {
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Scegli il tema");

            builder.setSingleChoiceItems(tema, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    temaSelect = tema[i];
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (temaSelect == "Scuro") {
                        Toast.makeText(MainActivity.this, "modalità dark", Toast.LENGTH_SHORT).show();
                        sharedPref.setNightModeState(true);
                        restartApp();
                    } else {
                        Toast.makeText(MainActivity.this, "modalità default", Toast.LENGTH_SHORT).show();
                        sharedPref.setNightModeState(false);
                        restartApp();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        } else if (id == R.id.idCategorie) {

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Seleziona una categoria");

            builder.setSingleChoiceItems(categorie, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    categoriaSelect = categorie[i];
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (controlloConnessione()) {
                        if (categoriaSelect == "Popolari") {
                            PAGE = 1;
                            CATEGORY = "popular";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                            Toast.makeText(MainActivity.this, "popolari", Toast.LENGTH_SHORT).show();
                        } else if (categoriaSelect == "Più votati") {
                            PAGE = 1;
                            CATEGORY = "top_rated";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                            Toast.makeText(MainActivity.this, "più votati", Toast.LENGTH_SHORT).show();
                        } else if (categoriaSelect == "Prossime Uscite") {
                            PAGE = 1;
                            CATEGORY = "upcoming";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                            Toast.makeText(MainActivity.this, "prossime uscite", Toast.LENGTH_SHORT).show();
                        } else if (categoriaSelect == "Novità") {
                            PAGE = 1;
                            CATEGORY = "now_playing";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                            Toast.makeText(MainActivity.this, "novità", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(MainActivity.this, "connessione internet assente", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("girato", true);
        outState.putString("categoria", CATEGORY);
        outState.putInt("page", PAGE);
        super.onSaveInstanceState(outState);
    }
}

package com.example.projectwork.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.adapter.RecycleViewAdapter;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.GenresResults;
import com.example.projectwork.services.GuestSessionResults;
import com.example.projectwork.services.IWebService;
import com.example.projectwork.services.FilmResults;
import com.example.projectwork.services.IWebServiceGenres;
import com.example.projectwork.services.IWebServiceGuestSession;
import com.example.projectwork.services.WebService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER;

public class MainActivity extends AppCompatActivity implements IWebService {

    private String CATEGORY;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    private String LANGUAGE = "it";
    private int PAGE;
    private WebService webService;
    FloatingActionButton btnGoOnTop;

    List<FilmResults.Data> noInternetFilm;
    List<FilmResults.Data> internetFilm;
    List<FilmResults.Data> searchInternetFilm;

    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;


    SwipeRefreshLayout swipeRefreshLayout;

    String[] categorie = {"Novità", "Prossime Uscite", "Più votati", "Popolari"};
    String categoriaSelect = "";

    String[] tema = {"Chiaro", "Scuro"};
    String temaSelect = "";

    SharedPref sharedPref;

    boolean inizializzato = false;

    int firstVisiblePosition;

    int count;

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
            firstVisiblePosition = savedInstanceState.getInt("lastPosition");
            CATEGORY = savedInstanceState.getString("categoria");
            PAGE = savedInstanceState.getInt("page");
        } else {
            CATEGORY = "popular";
            PAGE = 1;
        }

        if (controlloConnessione()) {
            // setInizializzazioneInteret();
            searchInternetFilm = new ArrayList<>();
            internetFilm = new ArrayList<>();
            adapter = new RecycleViewAdapter(MainActivity.this, internetFilm);
            recyclerView.setAdapter(adapter);
            inizializzato = true;
            webService = WebService.getInstance();
            if (PAGE <= 1) {
                internet();
            } else {
                for (int i = 1; i <= PAGE; i++) {
                    internetPage(i);
                }
            }
        } else {
            noInternetFilm = new ArrayList<>();
            adapter = new RecycleViewAdapter(MainActivity.this, noInternetFilm);
            recyclerView.setAdapter(adapter);
            noInternet();
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 4);
            recyclerView.setLayoutManager(gridLayoutManager);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int posizione = firstVisiblePosition;
                    if (posizione % 4 == 0) {
                        recyclerView.scrollToPosition(posizione);
                    } else {
                        recyclerView.scrollToPosition(posizione - 2);
                    }
                }
            }, 500);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   /* int posizione = firstVisiblePosition;
                    if (posizione % 4 == 0) {
                        recyclerView.smoothScrollToPosition(posizione);
                    } else {
                        recyclerView.smoothScrollToPosition(posizione + 2);
                    }
                    recyclerView.smoothScrollToPosition(posizione + 2);*/
                    recyclerView.scrollToPosition(0);
                }
            }, 500);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (controlloConnessione()) {
                    if (inizializzato) {
                        searchInternetFilm = new ArrayList<>();
                        internetFilm = new ArrayList<>();
                        adapter.resetFilms();
                        PAGE = 1;
                        internet();
                    } else {
                        searchInternetFilm = new ArrayList<>();
                        internetFilm = new ArrayList<>();
                        adapter = new RecycleViewAdapter(MainActivity.this, internetFilm);
                        recyclerView.setAdapter(adapter);
                        inizializzato = true;
                        webService = WebService.getInstance();
                        internet();
                    }
                } else {
                    noInternetFilm = new ArrayList<>();
                    adapter = new RecycleViewAdapter(MainActivity.this, noInternetFilm);
                    recyclerView.setAdapter(adapter);
                    noInternet();
                }
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

                if (controlloConnessione()) {
                    if (!recyclerView.canScrollVertically(1)) {
                        PAGE++;
                        //  webService = WebService.getInstance();
                        internet();
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
                    Toast.makeText(MainActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void internetPage(int page) {
        webService.getFilms(CATEGORY, API_KEY, LANGUAGE, page, MainActivity.this, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
                if (success) {
                    internetFilm.addAll(films);
                    adapter.setFilms(internetFilm);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchFilms(String QUERY) {
        webService.searchFilms(QUERY, API_KEY, LANGUAGE, new IWebService() {
            @Override
            public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
                if (success) {
                    adapter.resetFilms();
                    searchInternetFilm.clear();
                    searchInternetFilm.addAll(films);
                    adapter.setFilms(searchInternetFilm);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void noInternet() {
        Cursor cFilms = MainActivity.this.getContentResolver().query(FilmProvider.FILMS_URI, null, null, null, null);

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
            adapter.setFilms(noInternetFilm);
            adapter.notifyDataSetChanged();
        }
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
                if (controlloConnessione()) {
                    if (!newText.isEmpty()) {
                        searchFilms(newText);
                    } else {
                        PAGE = 1;
                        adapter.resetFilms();
                        internet();
                    }
                } else {
                    if (!newText.isEmpty()) {
                        adapter.getFilter().filter(newText);
                    } else {
                        adapter.resetSearchFilm();
                        noInternet();
                        adapter.notifyDataSetChanged();
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
                        Toast.makeText(MainActivity.this, "TEMA SCURO ATTIVATO", Toast.LENGTH_SHORT).show();
                        sharedPref.setNightModeState(true);
                        restartApp();
                    } else {
                        Toast.makeText(MainActivity.this, "TEMA CHIARO ATTIVATO", Toast.LENGTH_SHORT).show();
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
                        } else if (categoriaSelect == "Più votati") {
                            PAGE = 1;
                            CATEGORY = "top_rated";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                        } else if (categoriaSelect == "Prossime Uscite") {
                            PAGE = 1;
                            CATEGORY = "upcoming";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                        } else if (categoriaSelect == "Novità") {
                            PAGE = 1;
                            CATEGORY = "now_playing";
                            adapter.resetFilms();
                            internetFilm.clear();
                            internet();
                        }
                    } else
                        Toast.makeText(MainActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage) {
        //films
    }

    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        View firstChild = recyclerView.getChildAt(0);
        firstVisiblePosition = recyclerView.getChildAdapterPosition(firstChild);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(MainActivity.this,"ciao",Toast.LENGTH_SHORT).show();
        recyclerView.smoothScrollToPosition(firstVisiblePosition);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastPosition", firstVisiblePosition);
        outState.putString("categoria", CATEGORY);
        outState.putInt("page", PAGE);
    }
}

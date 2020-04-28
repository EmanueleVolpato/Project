package com.example.projectwork.services;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {

    public String BASE_URL = "https://api.themoviedb.org";

    private static WebService instance;
    private ApiInterface apiInterface;

    private WebService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static WebService getInstance() {
        if (instance == null)
            instance = new WebService();
        return instance;
    }

    public void listGenres(String apiKey, String language, final Context context, final IWebServiceGenres iwebservice) {

        Call<GenresResults> request = apiInterface.genresList(apiKey, language);

        request.enqueue(new Callback<GenresResults>() {
            @Override
            public void onResponse(Call<GenresResults> call, Response<GenresResults> response) {
                if (response.code() == 200) {
                    GenresResults results = response.body();
                    List<GenresResults.Data> listOfGeneres = results.getGenres();
                    iwebservice.onGenresFetched(true, listOfGeneres, -1, null);
                } else {
                    try {
                        iwebservice.onGenresFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        iwebservice.onGenresFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<GenresResults> call, Throwable t) {
                iwebservice.onGenresFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }

    public void searchFilms(String query, String apiKey, String language, final Context context, final IWebService iwebservice) {

        Call<FilmResults> filmsRequest = apiInterface.searchFilm(apiKey, language, query);

        filmsRequest.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                if (response.code() == 200) {
                    FilmResults results = response.body();
                    List<FilmResults.Data> listOfMovies = results.getResults();
                    iwebservice.onFilmsFetched(true, listOfMovies, -1, null);

                } else {
                    try {
                        iwebservice.onFilmsFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        iwebservice.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                iwebservice.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }

    public void getFilms(String category, String apiKey, String language, int page, final Context context, final IWebService iwebservice) {

        Call<FilmResults> filmsRequest = apiInterface.listOfFilm(category, apiKey, language, page);

        filmsRequest.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                if (response.code() == 200) {
                    FilmResults results = response.body();
                    List<FilmResults.Data> listOfMovies = results.getResults();
                    iwebservice.onFilmsFetched(true, listOfMovies, -1, null);

                    context.getContentResolver().delete(FilmProvider.FILMS_URI, null, null);

                    for (FilmResults.Data movie : listOfMovies) {
                        ContentValues cv = new ContentValues();
                        cv.put(FilmTableHelper.ID_MOVIE, movie.getId());
                        cv.put(FilmTableHelper.TITOLO, movie.getTitle());
                        cv.put(FilmTableHelper.DESCRIZIONE, movie.getOverview());
                        cv.put(FilmTableHelper.IMG_PRINCIPALE, movie.getPosterPath());
                        cv.put(FilmTableHelper.DATA, movie.getReleaseDate());
                        cv.put(FilmTableHelper.VOTO, movie.getVoteAverage());
                        cv.put(FilmTableHelper.IMG_DETTAGLIO, movie.getBackdropPath());
                        context.getContentResolver().insert(FilmProvider.FILMS_URI, cv);
                    }

                } else {
                    try {
                        iwebservice.onFilmsFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        iwebservice.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                iwebservice.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }
}

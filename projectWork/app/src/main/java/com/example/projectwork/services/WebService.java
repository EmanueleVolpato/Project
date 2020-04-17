package com.example.projectwork.services;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {
    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    public static String CATEGORY;
    public static String LANGUAGE;
    public static int PAGE;

    private static WebService instance;
    private ApiInterface apiInterface;

    private WebService(String category, String language, int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.CATEGORY = category;
        this.LANGUAGE = language;
        this.PAGE = page;

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static WebService getInstance(String category, String language, int page) {
        if (instance == null)
            instance = new WebService(category, language, page);
        return instance;
    }

    public void getFilms(final IWebService callback) {
        Call<MovieResults> filmsRequest = apiInterface.listOfMovies(CATEGORY, API_KEY, LANGUAGE, PAGE);

        filmsRequest.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.code() == 200) {
                    MovieResults results = response.body();
                    List<MovieResults.ResultsBean> listOfMovies = results.getResults();
                    callback.onFilmsFetched(true, listOfMovies, -1, null);
                } else {
                    try {
                        callback.onFilmsFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        callback.onFilmsFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                callback.onFilmsFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }
}

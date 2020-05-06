package com.example.projectwork.services;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.google.gson.JsonObject;

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

    public void votaFilm(String movie_id, String apiKey, String guest_session_id, JsonObject jsonBody, final IWebServiceVoteFilm iwebservice) {
        Call<VoteFilmResults> request = apiInterface.votaFilm(movie_id, apiKey, guest_session_id, jsonBody);
        request.enqueue(new Callback<VoteFilmResults>() {
            @Override
            public void onResponse(Call<VoteFilmResults> call, Response<VoteFilmResults> response) {
                VoteFilmResults results = response.body();
                iwebservice.onVoteFetched(true, results, -1, null);
            }

            @Override
            public void onFailure(Call<VoteFilmResults> call, Throwable t) {
                iwebservice.onVoteFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }

    public void getGuestIdSession(String apiKey, final IWebServiceGuestSession iwebservice) {
        Call<GuestSessionResults> request = apiInterface.guestSsession(apiKey);

        request.enqueue(new Callback<GuestSessionResults>() {
            @Override
            public void onResponse(Call<GuestSessionResults> call, Response<GuestSessionResults> response) {
                if (response.code() == 200) {
                    GuestSessionResults results = response.body();
                    iwebservice.onGuestFetched(true, results, -1, null);
                } else {
                    try {
                        iwebservice.onGuestFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        iwebservice.onGuestFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<GuestSessionResults> call, Throwable t) {
                iwebservice.onGuestFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }

    public void listGenres(String apiKey, String language, final IWebServiceGenres iwebservice) {

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

    public void searchFilms(String query, String apiKey, String language, final IWebService iwebservice) {

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

    public void getVideoFilm(String idFilm, String apiKey, String language, final IWebServiceVideoFilm iwebservice) {
        Call<VideoResults> rquest = apiInterface.getVideoFilm(idFilm, apiKey, language);

        rquest.enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                if (response.code() == 200) {
                    VideoResults results = response.body();
                    List<VideoResults.Data> listofVideos = results.getResults();
                    iwebservice.onVideoFetched(true, listofVideos, -1, null);

                } else {
                    try {
                        iwebservice.onVideoFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        iwebservice.onVideoFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {
                iwebservice.onVideoFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }

    public void getSimilarFilms(String idFilm, String apiKey, String language, int page, final IWebService iwebservice) {

        Call<FilmResults> filmsRequest = apiInterface.getSimilarFilm(idFilm, apiKey, language, page);

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

                        List<Integer> genresFilmInput = movie.getGenreIds();
                        if (genresFilmInput != null) {
                            int[] genresFilmOutput = new int[genresFilmInput.size()];
                            for (int i = 0; i < genresFilmInput.size(); i++) {
                                genresFilmOutput[i] = genresFilmInput.get(i);
                            }
                            cv.put(FilmTableHelper.GENERI, convertArrayToString(genresFilmOutput));
                        }
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

    public static String strSeparator = "__,__";

    public static String convertArrayToString(int[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + Integer.toString(array[i]);
            if (i < array.length - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public void getSingleFilm(String idMovie, String apiKey, String language, final IWebServiceSingleFilm iwebservice) {

        Call<SingleFilmResults> filmsRequest = apiInterface.getSingleFilm(idMovie, apiKey, language);

        filmsRequest.enqueue(new Callback<SingleFilmResults>() {
            @Override
            public void onResponse(Call<SingleFilmResults> call, Response<SingleFilmResults> response) {
                if (response.code() == 200) {
                    SingleFilmResults results = response.body();
                    iwebservice.onSingleFilmFetched(true, results, -1, null);
                } else {
                    try {
                        iwebservice.onSingleFilmFetched(true, null, response.code(), response.errorBody().string());
                    } catch (IOException ex) {
                        Log.e("WebService", ex.toString());
                        iwebservice.onSingleFilmFetched(true, null, response.code(), "Generic error message");
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleFilmResults> call, Throwable t) {
                iwebservice.onSingleFilmFetched(false, null, -1, t.getLocalizedMessage());
            }
        });
    }

}

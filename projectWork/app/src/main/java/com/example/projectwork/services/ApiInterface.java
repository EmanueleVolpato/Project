package com.example.projectwork.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //https://api.themoviedb.org/3/movie/popular?api_key=e6de0d8da508a9809d74351ed62affef&language=en-US&page=1

    @GET("/3/movie/{category}")
    Call<FilmResults> listOfFilm(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("3/search/movie?")
    Call<FilmResults> searchFilm(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query);

    //https://api.themoviedb.org/3/genre/movie/list?api_key=e6de0d8da508a9809d74351ed62affef&language=it

    @GET("3/genre/movie/list?")
    Call<GenresResults> genresList(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    //https://api.themoviedb.org/3/authentication/guest_session/new?api_key=e6de0d8da508a9809d74351ed62affef

    @GET("3/authentication/guest_session/new?")
    Call<GuestSessionResults> guestSsession(
            @Query("api_key") String apiKey);

    //http://youtube.com/watch?v= k1dFqDhoS9A
}

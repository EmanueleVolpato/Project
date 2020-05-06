package com.example.projectwork.services;

public interface IWebServiceSingleFilm {
    void onSingleFilmFetched(boolean success, SingleFilmResults film, int errorCode, String errorMessage);
}

package com.example.projectwork.services;

import java.util.List;

public interface IWebService {
    void onFilmsFetched(boolean success, List<FilmResults.Data> films, int errorCode, String errorMessage);
}

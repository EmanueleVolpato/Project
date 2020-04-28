package com.example.projectwork.services;

import java.util.List;

public interface IWebServiceGenres {
    void onGenresFetched(boolean success, List<GenresResults.Data> genres, int errorCode, String errorMessage);
}

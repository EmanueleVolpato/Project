package com.example.projectwork.services;

import java.util.List;

public interface IWebService {
    void onFilmsFetched(boolean success, List<MovieResults.ResultsBean> movies, int errorCode, String errorMessage);

}

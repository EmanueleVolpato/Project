package com.example.projectwork.services;

import java.util.List;

public interface IWebServiceVideoFilm {
    void onVideoFetched(boolean success, List<VideoResults.Data> videos, int errorCode, String errorMessage);
}

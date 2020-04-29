package com.example.projectwork.services;

public interface IWebServiceVoteFilm {
    void onVoteFetched(boolean success, VoteFilmResults voteResult, int errorCode, String errorMessage);
}

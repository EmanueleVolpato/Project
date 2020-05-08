package com.example.projectwork.services;


public interface IWebServiceGuestSession {
    void onGuestFetched(boolean success, GuestSessionResults guest, int errorCode, String errorMessage);
}

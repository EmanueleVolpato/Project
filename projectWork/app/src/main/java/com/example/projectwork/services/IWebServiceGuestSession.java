package com.example.projectwork.services;

import java.util.List;

public interface IWebServiceGuestSession {
    void onGuestFetched(boolean success, GuestSessionResults guest, int errorCode, String errorMessage);
}

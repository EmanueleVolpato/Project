package com.example.projectwork.services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuestSessionResults {

    /*{
            "success": true,
            "guest_session_id": "84b3f258ccc0b73084b26bba7dd505d9",
            "expires_at": "2020-04-29 14:45:08 UTC"
    }*/


    @SerializedName("success")
    private String success = null;

    @SerializedName("guest_session_id")
    private String guest_session_id = null;

    @SerializedName("expires_at")
    private String expires_at = null;

    public String getSuccess() {
        return success;
    }

    public String getGuest_session_id() {
        return guest_session_id;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setGuest_session_id(String guest_session_id) {
        this.guest_session_id = guest_session_id;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }
}

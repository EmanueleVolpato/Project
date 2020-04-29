package com.example.projectwork.services;

import com.google.gson.annotations.SerializedName;

public class VoteFilmResults {

    /*
    {
        "status_code": 1,
         "status_message": "Success."
    }
     */

    @SerializedName("status_code")
    private int status_code;

    @SerializedName("status_message")
    private String status_message;

    public int getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
}
